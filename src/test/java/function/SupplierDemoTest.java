package function;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class SupplierDemoTest {
    @Test
    public void RandomSupplier() {
        Supplier<Double> doubleSupplier = Math::random;

        Consumer<Double> logConsumer = num -> log.info(String.valueOf(num));

        logConsumer.accept(doubleSupplier.get());
    }
}
