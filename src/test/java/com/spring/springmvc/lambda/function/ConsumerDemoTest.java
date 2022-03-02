package com.spring.springmvc.lambda.function;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
public class ConsumerDemoTest {
    @Test
    public void LogConsumer() {
        Consumer<Object> logConsumer = logs -> log.info(logs.toString());
        logConsumer.accept("打印日志喽🤗");
        logConsumer.accept(System.currentTimeMillis());
    }

    @Test
    public void AndThen() {
        // 注意Consumer内部可以改变元素的值!!!
        Consumer<List<String>> upperCaseConsumer = strList -> {
            for (int i = 0; i < strList.size(); i++) {
                strList.set(i, strList.get(i).toUpperCase(Locale.ROOT));
            }
        };

        Consumer<List<String>> logConsumer = strList -> strList.forEach(System.out::println);

        List<String> strList = Lists.newArrayList("foo", "bar", "baz");

        upperCaseConsumer.andThen(logConsumer).accept(strList);
    }
}
