package com.spring.springmvc.lambda.stream;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamParallelDemoTest {
    public static int counter = 1;

    public static AtomicInteger atomicCounter = new AtomicInteger();

    @Test
    public void simpleParallel() {
        List<Integer> integerList = IntStream.range(0, 1000)
                .parallel()
                .peek(num -> counter++)
                .boxed()
                .collect(Collectors.toList());
        System.out.println(integerList.size());
        System.out.println(counter);
    }

    @Test
    public void betterParallel() {
        List<Integer> integerList = IntStream.range(0, 1000)
                .parallel()
                .peek(num -> atomicCounter.getAndIncrement())
                .boxed()
                .collect(Collectors.toList());
        System.out.println(integerList.size());
        System.out.println(atomicCounter);
    }
}