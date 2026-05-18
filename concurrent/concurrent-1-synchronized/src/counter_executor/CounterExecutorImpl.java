package counter_executor;


import counter.Counter;
import thread_factory.ThreadFactory;

import java.lang.reflect.InvocationTargetException;

public class CounterExecutorImpl implements CounterExecutor {
    protected int THREAD_SIZE = 20;

    private final ThreadFactory threadFactory;

    public CounterExecutorImpl(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    @Override
    public void execute(Boolean useSync, Integer iterations, Class<? extends Counter> counterImplClass) {
        Counter counter = getCounter(useSync, counterImplClass);
        testCounterWithTimer(counter, iterations);
    }


    protected final Counter getCounter(boolean useSync, Class<? extends Counter> counterImplClass) {
        try {
            return counterImplClass.getDeclaredConstructor(Boolean.class).newInstance(useSync);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    protected final int testCounter(Counter counter, Integer iterations) {
        Thread[] threads = new Thread[THREAD_SIZE];

        // Создаём и запускаем потоки
        for (int i = 0; i < threads.length; i++) {
            threads[i] = threadFactory.createThread(counter, iterations / THREAD_SIZE);
            threads[i].start();
        }

        // Ждём завершения всех потоков
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return counter.getCount();
    }

    protected final void testCounterWithTimer(Counter counter, Integer iterations) {
        long startTime = System.nanoTime();

        int resultCount = testCounter(counter, iterations);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        System.out.printf("Итераций: %d, Результат: %d, Время: %d мс%n",
                iterations, resultCount, durationMs);
    }
}
