package com.spring.springmvc.lambda.function;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

@Slf4j
public class PredicateDemoTest {
    @Test
    public void SimplePredicate() {
        Predicate<Integer> isAdult = age -> age >= 18;
        log.info(String.valueOf(isAdult.test(20)));
        log.info(String.valueOf(isAdult.test(16)));
    }

    @Test
    public void AndPredicate() {
        Predicate<Integer> isAdult = age -> {
            log.info("isAdult enter...");
            return age >= 18;
        };

        Predicate<Integer> isRetired = age -> {
            log.info("isRetired enter...");
            return age > 70;
        };

        // and有短路操作&&
        //log.info(String.valueOf(isAdult.and(isRetired).test(25)));
        log.info(String.valueOf(isRetired.and(isAdult).test(25)));
    }

    @Test
    public void OrPredicate() {
        Predicate<Integer> cannotRead = age -> {
            log.info("cannotRead enter...");
            return age < 4;
        };

        Predicate<Integer> cannotWrite = age -> {
            log.info("cannotWrite enter...");
            return age > 99;
        };

        log.info("退休了吗？{}", cannotRead.or(cannotWrite).test(35));
    }

    /**
     * 取反
     */
    @Test
    public void NegatePredicate() {
        Predicate<Integer> isAdult = age -> age > 18;
        log.info(String.valueOf(isAdult.negate().test(16)));
    }

    /**
     * 组合
     */
    @Test
    public void compositePredicate() {
        Predicate<Integer> cannotRead = age -> {
            log.info("cannotRead enter...");
            return age < 4;
        };

        Predicate<Integer> cannotWrite = age -> {
            log.info("cannotWrite enter...");
            return age > 99;
        };

        Predicate<Integer> composite = cannotRead.or(cannotWrite).negate();

        log.info("退休了吗？{}", composite.test(35));
    }
}