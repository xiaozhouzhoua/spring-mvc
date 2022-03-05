package com.spring.springmvc.lambda.stream;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamNIODemoTest {
    /**
     * Files.lines会延迟读取，直到流真正被消费才加载
     * 注意try-with-resources使用
     */
    @Test
    public void filesLines() {
        Path path = new File(getClass().getResource("/stream/nio_data.txt").getFile()).toPath();

        try (Stream<String> contentStream = Files.lines(path, Charset.defaultCharset())) {
            List<String> uppercaseContents = contentStream.map(String::toUpperCase)
                    .collect(Collectors.toList());
            uppercaseContents.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileWalk() {
        Path rootPath = new File(getClass().getResource("/stream/a").getFile()).toPath();
        // 指定遍历深度，默认无穷大
        try(Stream<Path> matched = Files.walk(rootPath, 3)) {
            List<Path> matchedPathList = matched.collect(Collectors.toList());
            matchedPathList.forEach(System.out::println);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void filesFind() {
        Path rootPath = new File(getClass().getResource("/stream/a").getFile()).toPath();
        try(Stream<Path> matched = Files.find(rootPath, 3, (path, basicFileAttributes) -> path.endsWith("find.txt"))) {
            List<Path> matchedPathList = matched.collect(Collectors.toList());
            matchedPathList.forEach(System.out::println);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 很有用的工具方法
     */
    @Test
    public void otherFileTools() throws IOException {
        ClassPathResource resource = new ClassPathResource("/stream/nio_data.txt");
        String dataString = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(dataString);
    }
}
