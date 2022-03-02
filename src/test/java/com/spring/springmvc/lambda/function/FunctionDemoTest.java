package com.spring.springmvc.lambda.function;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class FunctionDemoTest {
    @Test
    public void ArithmeticFunction() {
        Function<Integer, Integer> doubled = x -> x*2;
        log.info(String.valueOf(doubled.apply(6)));

        Function<Integer, Integer> halved = x -> x/2;
        log.info(String.valueOf(halved.apply(6)));

        Function<Integer, Integer> added = x -> x+2;
        log.info(String.valueOf(added.apply(6)));
    }

    @Test
    public void LengthFunction() {
        Function<String, Integer> lengthFunction = String::length;
        log.info(String.valueOf(lengthFunction.apply("Good")));
    }

    @Test
    public void CompositeFunction() {
        Function<Integer, Integer> doubled = x -> x*2;
        Function<Integer, Integer> added = x -> x+2;

        // compose传入先处理的函数
        log.info(String.valueOf(doubled.compose(added).apply(6)));
        log.info(String.valueOf(doubled.andThen(added).apply(6)));
    }

    @Test
    public void IdentityFunction() {
        List<User> users = Lists.newArrayList(
                User.builder().name("U1").phone("1888888").address("UA").build(),
                User.builder().name("U2").phone("1999999").address("UM").build(),
                User.builder().name("U3").phone("1000000").address("UW").build()
        );

        Map<String, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getName, Function.identity()));

        userMap.forEach((key, value) -> {
            System.out.println(key);
            System.out.println(value);
        });
    }
}

@Data
@Builder
class User {
    private String name;
    private String phone;
    private String address;
}