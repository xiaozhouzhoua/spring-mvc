package com.spring.springmvc.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;

public class StepVerifierDemoTest {
    @Test
    public void stepVerifierOperation() {
        Flux<Integer> intsFlux = Flux.range(1, 5);
        Flux<Integer> mapFlux = intsFlux.filter(i -> i % 2 == 0).map(i -> i * 2);

        mapFlux.log().subscribe();

        StepVerifier.create(mapFlux)
                .expectNext(4)
                .expectNext(8)
                .verifyComplete();

        StepVerifier.create(mapFlux)
                .expectNext(4)
                .thenConsumeWhile(i -> i <= 10)
                .verifyComplete();

        // 最后结果是否抛弃了元素
        StepVerifier.create(mapFlux)
                .expectNext(4)
                .thenConsumeWhile(i -> i <= 10)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDiscarded(1, 3, 5);

        // 花了多少时间
        StepVerifier.create(mapFlux)
                .expectNext(4)
                .thenConsumeWhile(i -> i <= 10)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDiscarded(1, 3, 5)
                .tookLessThan(Duration.ofSeconds(3));
    }

    @Test
    public void withVirtualTime() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(5)).take(3))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(5))
                .expectNext(0L)
                .thenAwait(Duration.ofSeconds(5))
                .expectNext(1L)
                .thenAwait(Duration.ofSeconds(5))
                .expectNext(2L)
                .verifyComplete();
    }

    /**
     * 时间跨度大于定义的时间，仍然从第一个元素开始验证
     */
    @Test
    public void withVirtualOverTime() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(5)).take(3))
                .thenAwait(Duration.ofSeconds(20))
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    public void useTestPublisher() {
        TestPublisher<String> testPublisher = TestPublisher.create();
        StepVerifier.create(testPublisher.flux().map(String::toUpperCase).log())
                .then(() -> testPublisher.emit("foo", "bar"))
                .expectNext("FOO", "BAR")
                .verifyComplete();
    }
}
