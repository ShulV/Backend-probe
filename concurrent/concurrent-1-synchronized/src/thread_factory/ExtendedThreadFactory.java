package thread_factory;

import counter.Counter;

/**
 * Создание потоков ч/з наследование от класса Thread
 */
public class ExtendedThreadFactory implements ThreadFactory {
    @Override
    public Thread createThread(Counter counter, int iterations) {
        return new CustomThread(counter, iterations);
    }

    static class CustomThread extends Thread {

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
