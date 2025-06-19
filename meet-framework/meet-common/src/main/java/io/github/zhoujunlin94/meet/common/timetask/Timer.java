package io.github.zhoujunlin94.meet.common.timetask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhoujunlin
 * @date 2025-06-19-15:29
 */
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

    /**
     * 构造函数
     */
    public Timer() {
        timeWheel = new TimeWheel(1, 20, System.currentTimeMillis(), delayQueue);
        workerThreadPool = Executors.newFixedThreadPool(100);
        // 轮询delayQueue获取过期任务线程
        ExecutorService bossThreadPool = Executors.newFixedThreadPool(1);
        // 20ms获取一次过期任务
        bossThreadPool.submit(() -> {
            while (true) {
                this.advanceClock(20);
            }
        });
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
                //执行过期任务（包含降级操作）
                timerTaskSlot.flush(this::addTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        AtomicInteger runCount = new AtomicInteger();
        int inCount = 0;
        Timer timer = new Timer();
        for (int i = 1; i <= 1000; i++) {
            TimerTask timerTask = new TimerTask(i, () -> {
                countDownLatch.countDown();
                int index = runCount.incrementAndGet();
                System.out.println(index + "----------执行");
            });
            timer.addTask(timerTask);
            System.out.println(i + "++++++++++加入");
            inCount++;
        }
        TimerTask timerTask = new TimerTask(5000, () -> {
            countDownLatch.countDown();
            int index = runCount.incrementAndGet();
            System.out.println(index + "----------执行");
        });
        timer.addTask(timerTask);
        try {
            countDownLatch.await();
            System.out.println("inCount" + inCount);
            System.out.println("runCount" + runCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
