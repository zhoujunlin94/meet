package io.github.zhoujunlin94.meet.common.timetask;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujunlin
 * @date 2025-06-19-15:29
 */
@Slf4j
public class Timer {

    /**
     * 时间轮
     */
    private final TimeWheel timeWheel;

    /**
     * 一个Timer只有一个delayQueue
     */
    private final DelayQueue<TimerTaskSlot> delayQueue = new DelayQueue<>();

    /**
     * 过期任务执行线程
     */
    private final ExecutorService workerThreadPool;


    public Timer(int tickMs, int wheelSize, int workerThreads, int delayQueuePollTimeout) {
        timeWheel = new TimeWheel(tickMs, wheelSize, System.currentTimeMillis(), delayQueue);
        workerThreadPool = Executors.newFixedThreadPool(workerThreads);
        // 轮询delayQueue获取过期任务线程
        ExecutorService bossThreadPool = Executors.newFixedThreadPool(1);
        // 20ms获取一次过期任务
        bossThreadPool.submit(() -> {
            while (true) {
                this.advanceClock(delayQueuePollTimeout);
            }
        });
    }

    public Timer() {
        this(1, 20, 100, 20);
    }

    /**
     * 添加任务
     */
    public void addTask(TimerTask timerTask) {
        // 添加失败任务直接执行
        if (!timeWheel.addTask(timerTask)) {
            workerThreadPool.submit(timerTask.getTask());
        }
    }

    /**
     * 获取过期任务
     */
    private void advanceClock(long timeout) {
        try {
            TimerTaskSlot timerTaskSlot = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (timerTaskSlot != null) {
                //推进时间
                timeWheel.advanceClock(timerTaskSlot.getExpiration());
                // 执行过期任务（包含降级操作）
                timerTaskSlot.flush(this::addTask);
            }
        } catch (Exception e) {
            log.error("advanceClock error", e);
        }
    }

}
