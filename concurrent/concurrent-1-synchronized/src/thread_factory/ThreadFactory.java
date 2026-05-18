package thread_factory;

import counter.Counter;

/**
 *
 */
public interface ThreadFactory {
    Thread createThread(Counter counter, int iterations);
}

