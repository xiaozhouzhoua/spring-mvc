package com.spring.springmvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * JDK8反射增强
 */
@RestController
public class Jdk8Controller {
    @GetMapping("/{p1}/{p2}")
    public String get(@PathVariable("p1")String path1, @PathVariable("p2")String path2) {
        return "/" + path1 + "/" + path2;
    }

    /**
     * 无法获取到参数名称
     */
    private static void beforeJdk8() throws NoSuchMethodException {
        Method method = Jdk8Controller.class.getMethod("get", String.class, String.class);
        Arrays.stream(method.getParameterTypes()).forEach(System.out::println);
    }

    /**
     * method.getParameters()可以获取到参数名称
     */
    private static void afterJdk8() throws NoSuchMethodException {
        Method method = Jdk8Controller.class.getMethod("get", String.class, String.class);
        Arrays.stream(method.getParameters()).forEach(parameter -> {
            System.out.println(parameter.getType() + "-" + parameter.getName());
        });
    }

    public static void main(String[] args) throws NoSuchMethodException {
        afterJdk8();
    }
}
