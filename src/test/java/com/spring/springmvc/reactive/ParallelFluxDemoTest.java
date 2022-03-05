package com.spring.springmvc.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ParallelFluxDemoTest {
    @Test
    public void parallelFlux() {
        Flux<Integer> flux = Flux.range(1, 5);
        ParallelFlux<Integer> parallelFlux = flux.parallel().runOn(Schedulers.parallel());

        parallelFlux.subscribe(i -> log.info("数字 {} 在线程 {}", i, Thread.currentThread().getName()));
    }

    /**
     * subscribe(new Subscriber<Integer>())的情况下，源码中定义的
     * Operators.onLastAssembly(sequential()).subscribe(Operators.toCoreSubscriber(subscriber));
     * sequential()方法会把ParallelFlux转换成正常的Flux，导致Schedulers.parallel()不生效
     */
    @Test
    public void parallelFluxWithSubscriber() {
        Flux<Integer> flux = Flux.range(1, 20);
        ParallelFlux<Integer> parallelFlux = flux.parallel().runOn(Schedulers.parallel());

        parallelFlux.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.info("数字 {} 在线程 {}", integer, Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 调用sequential()把ParallelFlux转换成正常的Flux，
     */
    @Test
    public void sequentialParallelFlux() {
        Flux<Integer> flux = Flux.range(1, 20);
        ParallelFlux<Integer> parallelFlux = flux.parallel();

        Flux<Integer> normalized = parallelFlux
                .runOn(Schedulers.parallel())
                .map(i -> i * 2)
                .sequential();

        normalized.subscribe(i -> log.info("数字 {} 在线程 {}", i, Thread.currentThread().getName()));
    }
}
