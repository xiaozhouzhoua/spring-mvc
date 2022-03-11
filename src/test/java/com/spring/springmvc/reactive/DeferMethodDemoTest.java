package com.spring.springmvc.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * defer方法创建数据源属于懒汉型，just创建数据源则是恶汉型
 */
@Slf4j
public class DeferMethodDemoTest {
    /**
     * 调用Mono.just(new Date())将立即调用该new Date()方法并捕获结果
     * 所述结果仅在订阅即发出Mono。多次订阅也不会更改该值
     */
    @Test
    public void noDeferFlux() throws InterruptedException {
        Mono<LocalDateTime> dateMono = Mono.just(LocalDateTime.now());
        System.out.println(dateMono.block());
        TimeUnit.SECONDS.sleep(1);

        dateMono.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);

        System.out.println(dateMono.block());
    }

    /**
     * Mono.just会在声明阶段构造Date对象，只创建一次，
     * 但是Mono.defer却是在subscribe阶段才会创建对应的Date对象，
     * 每次调用subscribe(block也是一种订阅)方法都会创建Date对象
     */
    @Test
    public void deferFlux() throws InterruptedException {
        Mono<LocalDateTime> dateMono = Mono.defer(() -> Mono.just(LocalDateTime.now()));
        System.out.println(dateMono.block());
        TimeUnit.SECONDS.sleep(1);

        dateMono.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);

        System.out.println(dateMono.block());
    }

    @Test
    public void whenUsingMonoJust_thenEagerEvaluation() throws InterruptedException {
        Mono<String> msg = sampleMsg("Eager Publisher");

        log.debug("Intermediate Test Message....");

        StepVerifier.create(msg)
                .expectNext("Eager Publisher")
                .verifyComplete();

        TimeUnit.SECONDS.sleep(1);

        StepVerifier.create(msg)
                .expectNext("Eager Publisher")
                .verifyComplete();
    }

    /**
     *  当运行Mono.just()时，它会立即创建一个Observable(Mono)并重用它，
     *  但是当使用defer()时，它不会立即创建它，它会在每个订阅中创建一个新的Observable
     */
    @Test
    public void whenUsingMonoDefer_thenLazyEvaluation() throws InterruptedException {
        Mono<String> deferMsg = Mono.defer(() -> sampleMsg("Lazy Publisher"));

        log.debug("Intermediate Test Message....");

        StepVerifier.create(deferMsg)
                .expectNext("Lazy Publisher")
                .verifyComplete();

        TimeUnit.SECONDS.sleep(1);

        StepVerifier.create(deferMsg)
                .expectNext("Lazy Publisher")
                .verifyComplete();
    }

    private Mono<String> sampleMsg(String str) {
        log.debug("Call to Retrieve Sample Message!! --> {} at: {}", str, new Date());
        return Mono.just(str);
    }

    /**
     * 值的变更对应的subscribe区别
     */
    int a = 1;
    @Test
    public void intDeferAndJust() {
        Mono<Integer> monoJust = Mono.just(a);
        Mono<Integer> monoDefer = Mono.defer(() -> Mono.just(a));

        monoJust.subscribe(System.out::println);
        monoDefer.subscribe(System.out::println);

        a = 2;
        monoJust.subscribe(System.out::println);
        monoDefer.subscribe(System.out::println);
    }

    /**
     * 希望在真正subscribe的时候才调用generateIterable方法
     * 不会去阻塞主线程的处理任务，真正在给定的Scheduler线程池中执行
     */
    @Test
    public void threadLazy() throws InterruptedException {
        log.info("time to get before flux");
        Flux<String> flux = Flux.defer(() -> Flux.fromIterable(generateIterable()))
                .subscribeOn(Schedulers.newParallel("new-parallel"));
        log.info("time to get after flux");
        flux.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * fromIterable在主线程上就已经调用了generateIterable方法生成了数据
     * 无法起到在给定的Scheduler线程池中执行任务的要求
     */
    @Test
    public void threadEager() throws InterruptedException {
        log.info("time to get before flux");
        Flux<String> flux = Flux.fromIterable(generateIterable())
                .subscribeOn(Schedulers.newParallel("new-parallel"));
        log.info("time to get after flux");
        flux.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(10);
    }

    private static List<String> generateIterable() {
        log.info("start generateIterable");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> result = new ArrayList<>();
        result.add("one");
        result.add("two");
        result.add("three");
        log.info("end generateIterable");
        return result;
    }
}
