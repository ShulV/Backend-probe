package thread_factory;

import counter.Counter;

/**
 * Создание потоков ч/з реализацию интерфейса Runnable
 */
public class RunnableInterfaceThreadFactory implements ThreadFactory {
    @Override
    public Thread createThread(Counter counter, int iterations) {
        return new Thread(new CustomThread(counter, iterations));
    }

    static class CustomThread implements Runnable {

        private final Counter counter;
        private final int iterations;

        public CustomThread(Counter counter, int iterations) {
            this.counter = counter;
            this.iterations = iterations;
        }

        @Override
        public void run() {
            for (int j = 0; j < iterations; j++) {
                counter.increment();
            }
        }
    }
}
