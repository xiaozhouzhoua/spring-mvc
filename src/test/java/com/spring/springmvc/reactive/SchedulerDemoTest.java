package com.spring.springmvc.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class SchedulerDemoTest {
    @Test
    public void threadDefined() {
        Mono<String> mono = Mono.just("foo");
        mono.map(s -> "线程名：")
                .subscribe(s -> System.out.println(s + Thread.currentThread().getName()));
    }

    @Test
    public void newThread() throws InterruptedException {
        Thread t = new Thread(() -> {
            Mono<String> mono = Mono.just("foo");
            mono.map(s -> "线程名：")
                    .subscribe(s -> System.out.println(s + Thread.currentThread().getName()));
        });
        t.start();
        t.join();
    }

    /**
     * 调用者线程
     */
    @Test
    public void schedulerImmediate() {
        Mono<String> mono = Mono.just("foo");
        mono.map(s -> "线程名：")
                .subscribeOn(Schedulers.immediate())
                .subscribe(s -> System.out.println(s + Thread.currentThread().getName()));
    }

    /**
     * 在一个单一的、可重用的线程中去执行订阅
     * 对所有的调用者重用相同的线程
     */
    @Test
    public void schedulerSingle() {
        Mono<String> mono = Mono.just("foo");
        mono.map(s -> "线程名：")
                .subscribeOn(Schedulers.single())
                .subscribe(s -> System.out.println(s + Thread.currentThread().getName()));
    }

    /**
     * 在从无界弹性线程池中拉取的工作者线程中执行订阅，根据需要创建新的工作线程
     * 并销毁空闲的工作线程-默认60秒
     */
    @Test
    public void schedulerElastic() {
        Mono<String> mono = Mono.just("foo");
        mono.map(s -> "线程名：")
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(s -> System.out.println(s + Thread.currentThread().getName()));
    }

    /**
     * 在从一个固定大小的线程池中拉取工作线程中执行订阅，
     * 该线程池的大小和CPU核心数一致
     */
    @Test
    public void schedulerParallel() {
        Mono<String> mono = Mono.just("foo");
        mono.map(s -> "线程名：")
                .subscribeOn(Schedulers.parallel())
                .subscribe(s -> System.out.println(s + Thread.currentThread().getName()));
    }
}
