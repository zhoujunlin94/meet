package io.github.zhoujunlin94.meet.common.timetask;

import java.util.concurrent.DelayQueue;

/**
 * @author zhoujunlin
 * @date 2025-06-19-15:26
 */
public class TimeWheel {

    /**
     * 一个时间槽的范围
     */
    private final long tickMs;

    /**
     * 时间槽个数
     */
    private final int wheelSize;

    /**
     * 时间跨度
     */
    private final long interval;

    /**
     * 时间槽
     */
    private final TimerTaskSlot[] timerTaskSlots;

    /**
     * 当前时间
     */
    private long currentTime;

    /**
     * 上层时间轮
     */
    private volatile TimeWheel overflowWheel;

    /**
     * 一个Timer只有一个delayQueue
     */
    private final DelayQueue<TimerTaskSlot> delayQueue;

    public TimeWheel(long tickMs, int wheelSize, long currentTime, DelayQueue<TimerTaskSlot> delayQueue) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.timerTaskSlots = new TimerTaskSlot[wheelSize];
        //currentTime为tickMs的整数倍 这里做取整操作
        this.currentTime = currentTime - (currentTime % tickMs);
        this.delayQueue = delayQueue;
        for (int i = 0; i < wheelSize; i++) {
            timerTaskSlots[i] = new TimerTaskSlot();
        }
    }

    /**
     * 创建或者获取上层时间轮
     * tickMs  1  10  100  wheelSize倍
     */
    private TimeWheel getOverflowWheel() {
        if (overflowWheel == null) {
            synchronized (this) {
                if (overflowWheel == null) {
                    overflowWheel = new TimeWheel(interval, wheelSize, currentTime, delayQueue);
                }
            }
        }
        return overflowWheel;
    }

    /**
     * 添加任务到时间轮
     */
    public boolean addTask(TimerTask timerTask) {
        long expiration = timerTask.getDelayMs();
        // 过期任务直接执行
        if (expiration < currentTime + tickMs) {
            return false;
        } else if (expiration < currentTime + interval) {
            // 当前时间轮可以容纳该任务 加入时间槽
            long virtualId = expiration / tickMs;
            int index = (int) (virtualId % wheelSize);
            TimerTaskSlot timerTaskList = timerTaskSlots[index];
            timerTaskList.addTask(timerTask);
            if (timerTaskList.setExpiration(virtualId * tickMs)) {
                //添加到delayQueue中
                delayQueue.offer(timerTaskList);
            }
        } else {
            // 放到上一层的时间轮
            TimeWheel timeWheel = getOverflowWheel();
            timeWheel.addTask(timerTask);
        }
        return true;
    }

    /**
     * 推进时间
     */
    public void advanceClock(long timestamp) {
        if (timestamp >= currentTime + tickMs) {
            // 取整
            currentTime = timestamp - (timestamp % tickMs);
            if (overflowWheel != null) {
                //推进上层时间轮时间
                this.getOverflowWheel().advanceClock(timestamp);
            }
        }
    }
}