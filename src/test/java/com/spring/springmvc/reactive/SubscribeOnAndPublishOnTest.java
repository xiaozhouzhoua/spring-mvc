package com.spring.springmvc.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * 两种方法改变订阅的执行方式
 * subscribeOn(scheduler)
 * publishOn(scheduler)
 */
@Slf4j
public class SubscribeOnAndPublishOnTest {
    /**
     * 上下游全程生效
     */
    @Test
    public void subscribeOn() throws InterruptedException {
        Scheduler scheduler = Schedulers.newElastic("subscribeOn-demo-elastic");
        Flux<Integer> flux = Flux.range(1, 5).filter(i -> {
            log.info("filter in thread {}", Thread.currentThread().getName());
            return i % 2 == 0;
        }).subscribeOn(scheduler).map(i -> {
            log.info("map in thread {}", Thread.currentThread().getName());
            return i + 2;
        });

        Thread t = new Thread(() -> {
            log.info("begin enter thread");
            flux.subscribe(i -> log.info(String.valueOf(i)));
            log.info("end enter thread");
        });

        t.start();
        t.join();
    }

    /**
     * 定义多个的情况下，只会对第一个生效
     */
    @Test
    public void subscribeOnMany() throws InterruptedException {
        Scheduler scheduler_1 = Schedulers.newElastic("subscribeOn-1-demo-elastic");
        Scheduler scheduler_2 = Schedulers.newElastic("subscribeOn-2-demo-elastic");
        Flux<Integer> flux = Flux.range(1, 5).filter(i -> {
            log.info("filter in thread {}", Thread.currentThread().getName());
            return i % 2 == 0;
        }).subscribeOn(scheduler_1).map(i -> {
            log.info("map in thread {}", Thread.currentThread().getName());
            return i + 2;
        });

        Thread t = new Thread(() -> {
            log.info("begin enter thread");
            // 覆盖的情况
            flux.subscribeOn(scheduler_2).subscribe(i -> log.info(String.valueOf(i)));
            log.info("end enter thread");
        });

        t.start();
        t.join();
    }

    /**
     * 只对下游生效
     */
    @Test
    public void publishOn() throws InterruptedException {
        Scheduler scheduler = Schedulers.newElastic("subscribeOn-demo-elastic");
        Flux<Integer> flux = Flux.range(1, 5).filter(i -> {
            log.info("filter in thread {}", Thread.currentThread().getName());
            return i % 2 == 0;
        }).publishOn(scheduler).map(i -> {
            log.info("map in thread {}", Thread.currentThread().getName());
            return i + 2;
        });

        Thread t = new Thread(() -> {
            log.info("begin enter thread");
            flux.subscribe(i -> log.info(String.valueOf(i)));
            log.info("end enter thread");
        });

        t.start();
        t.join();
    }

    /**
     * 多个的情况下也只对下游生效(到下一个publishOn为止)
     */
    @Test
    public void publishOnMany() throws InterruptedException {
        Scheduler scheduler_1 = Schedulers.newElastic("subscribeOn-1-demo-elastic");
        Scheduler scheduler_2 = Schedulers.newElastic("subscribeOn-2-demo-elastic");
        Flux<Integer> flux = Flux.range(1, 5)
                .publishOn(scheduler_2)
                .filter(i -> {
                    log.info("filter in thread {}", Thread.currentThread().getName());
                    return i % 2 == 0;
                 })
                .publishOn(scheduler_1)
                .map(i -> {
                    log.info("map in thread {}", Thread.currentThread().getName());
                    return i + 2;
        });

        Thread t = new Thread(() -> {
            log.info("begin enter thread");
            flux.subscribe(i -> log.info(String.valueOf(i)));
            log.info("end enter thread");
        });

        t.start();
        t.join();
    }

    /**
     * 混合使用-作用对应范围
     */
    @Test
    public void mixed() throws InterruptedException {
        Scheduler subscribe = Schedulers.newElastic("subscribe-demo-elastic");
        Scheduler publish = Schedulers.newElastic("publish-demo-elastic");
        Flux<Integer> flux = Flux.range(1, 5)
                .subscribeOn(subscribe)
                .filter(i -> {
                    log.info("filter in thread {}", Thread.currentThread().getName());
                    return i % 2 == 0;
                })
                .publishOn(publish)
                .map(i -> {
                    log.info("map in thread {}", Thread.currentThread().getName());
                    return i + 2;
                });

        Thread t = new Thread(() -> {
            log.info("begin enter thread");
            flux.subscribe(i -> log.info(String.valueOf(i)));
            log.info("end enter thread");
        });

        t.start();
        t.join();
    }
}
