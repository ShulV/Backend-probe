package counter_executor;

import counter.Counter;

public interface CounterExecutor {
    void execute(Boolean useSync, Integer iterations, Class<? extends Counter> counterImplClass);
}
