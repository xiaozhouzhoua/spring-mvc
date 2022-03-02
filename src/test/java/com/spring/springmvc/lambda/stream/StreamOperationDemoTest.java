package com.spring.springmvc.lambda.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamOperationDemoTest {

    private List<Student> getStudents() {
        return Lists.newArrayList(
                Student.builder().name("S1").classNo(136).store(80).build(),
                Student.builder().name("S2").classNo(136).store(90).build(),
                Student.builder().name("S3").classNo(138).store(90).build(),
                Student.builder().name("S4").classNo(138).store(100).build()
        );
    }

    @Test
    public void streamMap() {
        Stream<String> stringStream = Stream.of("foo", "bar", "baz");
        Stream<String> mapStream = stringStream.map(s -> s.toUpperCase(Locale.ROOT));
        mapStream.forEach(System.out::println);
    }

    @Test
    public void streamCollectTo() {
        Stream<String> stringStream = Stream.of("foo", "bar", "baz");
        Stream<String> mapStream = stringStream.map(s -> s.toUpperCase(Locale.ROOT));
        List<String> stringList = mapStream.collect(Collectors.toList());
        stringList.forEach(System.out::println);
    }

    @Test
    public void streamJoining() {
        Stream<String> stringStream = Stream.of("foo", "bar", "baz");
        String result = stringStream.collect(Collectors.joining("|"));
        System.out.println(result);
    }

    @Test
    public void streamCollectGroupBy() {
        List<Student> students = getStudents();

        Map<Integer, List<Student>> groupByClassNoMap = students.stream()
                .collect(Collectors.groupingBy(Student::getClassNo));

        groupByClassNoMap.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }

    /**
     * 不使用默认的toList
     * 使用自定义的downStream覆盖返回的list
     */
    @Test
    public void streamCollectGroupByWithDownStream() {
        List<Student> students = getStudents();

        Map<Integer, Integer> groupByClassNoWithSumMap = students.stream()
                .collect(Collectors.groupingBy(Student::getClassNo, Collectors.summingInt(Student::getStore)));

        groupByClassNoWithSumMap.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });

        System.out.println("|||||||||||||||||||||||");

        Map<Integer, Optional<Student>> groupByClassNoWithMaxMap = students.stream()
                .collect(Collectors.groupingBy(Student::getClassNo, Collectors.maxBy(Comparator.comparing(Student::getStore))));

        groupByClassNoWithMaxMap.forEach((k, v) -> {
            System.out.println(k);
            v.ifPresent(System.out::println);
        });
    }

    @Test
    public void streamFilter() {
        List<Student> students = getStudents();
        Stream<Student> studentFilterStream = students.stream().filter(student -> student.getStore() > 80);
        studentFilterStream.forEach(System.out::println);
    }

    @Test
    public void streamFindFirst() {
        List<Student> students = getStudents();
        Student findFirstStudent = students.stream()
                .filter(student -> student.getStore() > 80)
                .findFirst()
                .orElse(new Student());
        System.out.println(findFirstStudent);
    }

    /**
     * peek与forEach方法的区别在于：
     * peek是中间操作，若没有终结操作，那么根据流的惰性加载，中间操作的最后不会执行
     * .peek(student -> student.setStore(student.getStore() + 10))
     * .peek(System.out::println) 这里就不会执行打印操作
     */
    @Test
    public void streamPeek() {
        List<Student> students = getStudents();
        List<Student> addScoreStudents = students.stream()
                .peek(student -> student.setStore(student.getStore() + 10))
                .peek(student -> student.setName(student.getName() + "+10"))
                .collect(Collectors.toList());
        addScoreStudents.forEach(System.out::println);
    }

    @Test
    public void streamFlatmap() {
        String graph = "this is a graph content";
        String[] words = graph.split(" ");

        Map<String, Long> letterCountMap = Arrays.stream(words)
                .map(str -> str.split(""))
                .flatMap(strArray -> Arrays.stream(strArray))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        letterCountMap.forEach((k, v) -> {
            System.out.println(k + "出现次数: " + v);
        });
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Student {
    private String name;
    private Integer classNo;
    private Integer store;
}
