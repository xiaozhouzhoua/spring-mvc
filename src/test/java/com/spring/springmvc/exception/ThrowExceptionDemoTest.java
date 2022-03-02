package com.spring.springmvc.exception;

import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ThrowExceptionDemoTest {
    @Test
    public void ForLoopThrowTest() throws GeneralSecurityException, InterruptedException {
        List<String> items = Lists.newArrayList("foo", "bar", "baz");
        for (String item : items) {
            encrypt(item);
        }
    }

    @Test
    public void CannotThrowException() {
        List<String> items = Lists.newArrayList("foo", "bar", "baz");
        // 匿名内部类接收的Consumer并没有对外抛出异常，需要我们在accept方法内部处理
        items.forEach(new Consumer<String>() {
           @Override
           public void accept(String item) {
               try {
                   encrypt(item);
               } catch (GeneralSecurityException | InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });
    }

    @Test
    public void AnnotationException() {
        List<String> items = Lists.newArrayList("foo", "bar", "baz");
        // 利用Lombok注解声明，无需try catch
        items.forEach(new Consumer<String>() {
            @SneakyThrows
            @Override
            public void accept(String item) {
                encrypt(item);
            }
        });
    }

    private static String encrypt(String input) throws GeneralSecurityException, InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "encrypted";
    }
}
