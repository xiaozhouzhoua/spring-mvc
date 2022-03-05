package com.spring.springmvc.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.TimeUnit;

/**
 * 实时流
 */
public class SinkStreamDemoTest {
    @Test
    public void simpleSinksCreation() {
        Sinks.Many<Integer> sinks = Sinks.unsafe().many().multicast().directBestEffort();

        Flux<Integer> flux = sinks.asFlux();
        flux.subscribe(i -> System.out.println("subscribe-1 get " + i));

        sinks.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        sinks.tryEmitNext(2).orThrow();

        flux.subscribe(i -> System.out.println("subscribe-2 get " + i));

        sinks.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);
        sinks.emitNext(4, Sinks.EmitFailureHandler.FAIL_FAST);
        sinks.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }

    /**
     * 手动触发所有订阅发送
     */
    @Test
    public void connectableFlux() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 5);
        ConnectableFlux<Integer> connectableFlux = source.publish();

        connectableFlux.subscribe(i -> System.out.println("subscribe-1 get " + i));
        connectableFlux.subscribe(i -> System.out.println("subscribe-2 get " + i));
        System.out.println("所有订阅者订阅完成!");

        TimeUnit.SECONDS.sleep(3);

        System.out.println("connect触发消费");
        connectableFlux.connect();
    }

    /**
     * 设置自动触发订阅发送
     */
    @Test
    public void connectableAutoFlux() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 5);
        Flux<Integer> connectableFlux = source.publish().autoConnect(2);

        connectableFlux.subscribe(i -> System.out.println("subscribe-1 get " + i));
        connectableFlux.subscribe(i -> System.out.println("subscribe-2 get " + i));
    }
}
