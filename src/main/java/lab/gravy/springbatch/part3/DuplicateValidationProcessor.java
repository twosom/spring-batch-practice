package lab.gravy.springbatch.part3;


import org.springframework.batch.item.ItemProcessor;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;

public class DuplicateValidationProcessor<T> implements ItemProcessor<T, T> {

    private final Set<String> keyPool = new ConcurrentSkipListSet<>();
    private final Function<T, String> keyExtractor;
    private final boolean allowDuplicate;

    public DuplicateValidationProcessor(Function<T, String> keyExtractor,
                                        boolean allowDuplicate) {
        this.keyExtractor = keyExtractor;
        this.allowDuplicate = allowDuplicate;
    }

    @Override
    public T process(T item) throws Exception {
        if (allowDuplicate) {
            return item;
        }

        String key = keyExtractor.apply(item);
        if (isDuplicateKey(key)) {
            return null;
        }

        keyPool.add(key);
        return item;
    }

    private boolean isDuplicateKey(String key) {
        return keyPool.contains(key);
    }
}
