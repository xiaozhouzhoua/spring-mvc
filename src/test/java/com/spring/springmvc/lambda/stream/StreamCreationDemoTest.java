package com.spring.springmvc.lambda.stream;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class StreamCreationDemoTest {
    @Test
    public void streamGenerate() {
        Stream<Double> stream = Stream.generate(() -> Math.random());
        stream.limit(6).forEach(System.out::println);
    }

    @Test
    public void streamOf() {
        Stream<String> stream_1 = Stream.of("foo");
        Stream<String> stream_2 = Stream.of("foo", "bar", "baz");
        Stream<String> stream_3 = Arrays.stream(new String[]{"foo","bar", "baz"});
        stream_1.forEach(System.out::println);
        stream_2.forEach(System.out::println);
        stream_3.forEach(System.out::println);
    }

    @Test
    public void streamFromCollections() {
        List<String> stringList = Lists.newArrayList("foo", "bar", "baz");
        Stream<String> streamFromList = stringList.stream();
        Stream<Map.Entry<String, String>> streamFromMap = Maps.newHashMap("Key_1", "Value_1").entrySet().stream();
        streamFromList.forEach(System.out::println);
        streamFromMap.forEach(entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        });
    }

    @Test
    public void streamBuild() {
        Stream.Builder<String> builder = Stream.builder();
        builder.accept("foo");
        builder.accept("bar");
        builder.accept("baz");

        Stream<String> stringStream = builder.build();
        stringStream.forEach(System.out::println);
    }
}
