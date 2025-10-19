package io.github.zhoujunlin94.meet.common.timetask;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhoujunlin
 * @date 2025/6/19 21:43
 */
@Slf4j
public class TestTimer {

    @SneakyThrows
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1001);

        AtomicInteger runCount = new AtomicInteger();
        int inCount = 0;

        Timer timer = new Timer();
        for (int i = 1; i <= 1000; i++) {
            TimerTask timerTask = new TimerTask(i, () -> {
                countDownLatch.countDown();
                int index = runCount.incrementAndGet();
                log.warn(index + "----------执行");
            }, "循环任务共1000个,任务延迟i");
            timer.addTask(timerTask);
            log.warn(i + "++++++++++加入");
            inCount++;
        }
        TimerTask timerTask = new TimerTask(5000, () -> {
            countDownLatch.countDown();
            int index = runCount.incrementAndGet();
            log.warn(index + "----------执行");
        }, "额外任务共1个,延迟5秒");
        timer.addTask(timerTask);
        inCount++;

        countDownLatch.await();
        log.warn("inCount:" + inCount);
        log.warn("runCount:" + runCount);

    }

}
