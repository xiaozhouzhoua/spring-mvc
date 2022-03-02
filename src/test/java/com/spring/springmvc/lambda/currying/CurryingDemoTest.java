package com.spring.springmvc.lambda.currying;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public class CurryingDemoTest {
    @Test
    public void CurryingCreation() {
        // 定义的BiFunction，两个入参
        BiFunction<String, String, User> userBiFunction = (name, phone) -> new User(name, phone);

        // 拆解定义的BiFunction，使其只有一个入参
        Function<String, Function<String, User>> curryingFunction = new Function<String, Function<String, User>>() {
            @Override
            public Function<String, User> apply(String name) {
                return new Function<String, User>() {
                    @Override
                    public User apply(String phone) {
                        return new User(name, phone);
                    }
                };
            }
        };

        log.info(String.valueOf(userBiFunction.apply("U1", "1888888")));

        log.info(String.valueOf(curryingFunction.apply("U1").apply("1888888")));

        log.info(String.valueOf(curryingFunction.apply("U2").apply(String.valueOf(System.currentTimeMillis()))));
    }
}

@Data
@AllArgsConstructor
class User {
    private String name;
    private String phone;
}
