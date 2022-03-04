package com.spring.springmvc.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Slf4j
public class ReactiveDemoTest {
    @Test
    public void createFluxByJust() {
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");
        fruitFlux.log().subscribe(fruit -> System.out.println("我的水果：" + fruit));

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void withErrorConsumer() {
        Flux<Integer> intsFlux = Flux.range(1, 5)
                .map(i -> {
                   if (i <= 3) {
                       return i;
                   } else {
                       throw new RuntimeException("i > 3");
                   }
                });

        intsFlux.subscribe(
                i -> log.info(String.valueOf(i)),
                err -> log.error("error: " + err.getMessage())
        );
    }

    @Test
    public void withErrorConsumerAndComplete() {
        Flux<Integer> intsFlux = Flux.range(1, 5)
                .map(i -> {
                    if (i <= 3) {
                        return i;
                    } else {
                        throw new RuntimeException("i > 3");
                    }
                });

        intsFlux.subscribe(
                i -> log.info(String.valueOf(i)),
                err -> log.error("error: " + err.getMessage()),
                () -> log.info("Completed!")
        );
    }

    @Test
    public void withSubscription() {
        Flux<Integer> intsFlux = Flux.range(1, 5);

        Consumer<? super Subscription> subscriptionConsumer_1 = null;
        // 只返回给定的请求数量
        Consumer<? super Subscription> subscriptionConsumer_2 = sub -> sub.request(3);
        // 超出直接结束
        Consumer<? super Subscription> subscriptionConsumer_3 = sub -> sub.request(6);
        // 直接取消
        Consumer<? super Subscription> subscriptionConsumer_4 = sub -> sub.cancel();
        // 没有请求
        Consumer<? super Subscription> subscriptionConsumer_5 = sub -> sub.getClass();
        intsFlux.log()
                .subscribe(
                    i -> log.info(String.valueOf(i)),
                    null,
                    () -> log.info("Completed!"),
                    subscriptionConsumer_5
        );
    }

    @Test
    public void subscribeWithBaseSubscriber() {
        Flux<Integer> intsFlux = Flux.range(1, 5);
        intsFlux.subscribe(new SampleSubscriber<>());
    }

    public class SampleSubscriber<T> extends BaseSubscriber<T> {
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            log.info("已订阅");
            request(1);
        }

        @Override
        protected void hookOnNext(T value) {
            log.info(String.valueOf(value));
            // 控制消费速度
            request(1);
        }
    }

    @Test
    public void mapFlux() {
        Flux<Integer> intsFlux = Flux.range(1, 5);
        Flux<Integer> mapFlux = intsFlux.map(i -> i * 2);

        mapFlux.log().subscribe();

        StepVerifier.create(mapFlux)
                .expectNext(2)
                .expectNext(4)
                .expectNext(6)
                .expectNext(8)
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    public void filterFlux() {
        Flux<Integer> intsFlux = Flux.range(1, 5);
        Flux<Integer> filterFlux = intsFlux.filter(i -> i % 2 == 0);

        filterFlux.log().subscribe();

        StepVerifier.create(filterFlux)
                .expectNext(2)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void bufferFlux() {
        Flux<Integer> intsFlux = Flux.range(1, 32);
        Flux<List<Integer>> bufferedFlux = intsFlux.buffer(3);

        bufferedFlux.log().subscribe();
    }

    /**
     * 调用外部网络请求失败重试
     */
    @Test
    public void retryFlux() {
        Mono<String> client = Mono.fromSupplier(() -> {
            double num = Math.random() * 10;
            if (num > 5) {
                throw new Error("网络错误!");
            }
            return "https://www.google.com";
        });
        client.log()
                .retry(3)
                .subscribe();
    }

    /**
     * 重复消费的问题
     */
    @Test
    public void retryFluxReConsumer() throws InterruptedException {
        Flux<Long> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next(i);
                    if (i == 9) sink.error(new RuntimeException("到九了!"));
                    return state;
                },
                (state) -> System.out.println("Completed!")
        );
        // retry会导致重复消费之前的元素
        flux.log().retry(1).subscribe();
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void zipFlux() {
        Flux<Integer> flux_1 = Flux.range(1, 5);
        Flux<Integer> flux_2 = Flux.range(1, 5);

        Flux.zip(flux_1, flux_2, (a, b) -> a + b)
                .log()
                .subscribe();
    }

    @Test
    public void zipWith() {
        Flux<String> nameFlux = Flux.just("张三", "李四", "王五");
        Flux<String> hobbyFlux = Flux.just("🏀", "⚽️", "🏸");

        nameFlux.zipWith(hobbyFlux, (name, hobby) -> name + "喜欢打" + hobby)
                .log()
                .subscribe();
    }
}
