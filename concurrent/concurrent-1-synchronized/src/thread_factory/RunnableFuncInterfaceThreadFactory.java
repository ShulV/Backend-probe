package thread_factory;

import counter.Counter;

/**
 * Создание потоков ч/з Runnable-лямбду
 */
public class RunnableFuncInterfaceThreadFactory implements ThreadFactory {
    @Override
    public Thread createThread(Counter counter, int iterations) {
        return new Thread(() -> {
            for (int j = 0; j < iterations; j++) {
                counter.increment();
            }
        });
    }
}

