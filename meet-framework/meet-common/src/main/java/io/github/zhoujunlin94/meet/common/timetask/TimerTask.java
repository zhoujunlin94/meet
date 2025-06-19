package io.github.zhoujunlin94.meet.common.timetask;

import lombok.Getter;

/**
 * @author zhoujunlin
 * @date 2025-06-19-15:20
 * 时间槽里的任务
 */
public class TimerTask {

    /**
     * 延迟时间
     */
    @Getter
    private final long delayMs;

    /**
     * 任务
     */
    @Getter
    private final Runnable task;

    /**
     * 时间槽
     */
    protected TimerTaskSlot timerTaskSlot;

    /**
     * 下一个节点
     */
    protected TimerTask next;

    /**
     * 上一个节点
     */
    protected TimerTask pre;

    /**
     * 描述
     */
    public String desc;

    public TimerTask(long delayMs, Runnable task) {
        this.delayMs = System.currentTimeMillis() + delayMs;
        this.task = task;
    }

    public TimerTask(long delayMs, Runnable task, String desc) {
        this.delayMs = System.currentTimeMillis() + delayMs;
        this.task = task;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }

}
