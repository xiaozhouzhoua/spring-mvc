package function;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.IntFunction;

@Slf4j
public class TypeFunctionDemoTest {
    @Test
    public void IntFunctionDemo() {
        // 避免频繁地拆箱-装箱
        IntFunction intFunction = x -> x + 1;

        // 数据量大的情况下会频繁地拆箱装箱
        Function<Integer, Integer> integerFunction = x -> x + 1;
    }
}
