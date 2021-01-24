package util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author starforge
 */
public class Container<T> implements Consumer<T>, Supplier<T> {

    private T value;

    @Override
    public void accept(T t) {
        value = t;
    }

    @Override
    public T get() {
        return value;
    }
}
