package io.github.zhoujunlin94.meet.common.timetask;

import javax.annotation.Nonnull;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author zhoujunlin
 * @date 2025-06-19-15:21
 */
public class TimerTaskSlot implements Delayed {

    /**
     * 过期时间
     */
    private final AtomicLong expiration = new AtomicLong(-1L);

    /**
     * 根节点
     */
    private final TimerTask root = new TimerTask(-1L, null);

    {
        root.pre = root;
        root.next = root;
    }

    /**
     * 设置过期时间
     */
    public boolean setExpiration(long expire) {
        // 设置新值  返回老值
        return expiration.getAndSet(expire) != expire;
    }

    /**
     * 获取过期时间
     */
    public long getExpiration() {
        return expiration.get();
    }

    /**
     * 新增任务
     */
    public void addTask(TimerTask timerTask) {
        synchronized (this) {
            if (timerTask.timerTaskSlot == null) {
                timerTask.timerTaskSlot = this;
                // 形成环状结构
                TimerTask tail = root.pre;
                timerTask.next = root;
                timerTask.pre = tail;
                tail.next = timerTask;
                root.pre = timerTask;
            }
        }
    }

    /**
     * 移除任务
     */
    public void removeTask(TimerTask timerTask) {
        synchronized (this) {
            if (timerTask.timerTaskSlot.equals(this)) {
                timerTask.next.pre = timerTask.pre;
                timerTask.pre.next = timerTask.next;
                timerTask.timerTaskSlot = null;
                timerTask.next = null;
                timerTask.pre = null;
            }
        }
    }

    /**
     * 重新分配（移除任务，并对这个任务执行flush操作）
     * 操作：未过期重新放进去  过期立即执行 TImer#addTask
     */
    public synchronized void flush(Consumer<TimerTask> flush) {
        TimerTask timerTask = root.next;
        while (!timerTask.equals(root)) {
            this.removeTask(timerTask);
            flush.accept(timerTask);
            timerTask = root.next;
        }
        expiration.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public int compareTo(@Nonnull Delayed obj) {
        if (obj instanceof TimerTaskSlot) {
            return Long.compare(expiration.get(), ((TimerTaskSlot) obj).expiration.get());
        }
        return 0;
    }

}