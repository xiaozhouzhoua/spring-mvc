package com.spring.springmvc.lambda.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@Slf4j
public class ForkJoinPoolDemoTest {
    /**
     * 不推荐使用
     * 调用主线程main也会加入到工作线程中去执行分解任务
     * 对资源有竞争，不利于线程池集中管理
     */
    @Test
    public void commonForkJoinPool() {
        log.info("通用线程池并发数量: {}", ForkJoinPool.commonPool().getParallelism());

        IntStream.range(0, 1000).parallel().forEach(num -> {
            log.info("num: {}", num);
        });
    }

    /**
     * 推荐使用自定义的ForkJoinPool
     * 不会使用调用主线程main
     */
    @Test
    public void newForkJoinPool() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        forkJoinPool.submit(() -> {
            IntStream.range(0, 1000).parallel().forEach(num -> {
                log.info("num: {}", num);
            });
        });
    }
}
