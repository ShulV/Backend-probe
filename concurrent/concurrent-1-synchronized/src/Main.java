import counter.Counter;
import counter.CounterWithSynchronizedBlock;
import counter_executor.CounterExecutor;
import counter_executor.CounterExecutorImpl;
import thread_factory.ExtendedThreadFactory;
import thread_factory.RunnableFuncInterfaceThreadFactory;
import thread_factory.RunnableInterfaceThreadFactory;
import thread_factory.ThreadFactory;

public class Main {
    static int ITERATION_COUNT = 10_000_000;

    public static void main(String[] args) {
        testRaceCondition(new RunnableFuncInterfaceThreadFactory(), CounterWithSynchronizedBlock.class);
        testRaceCondition(new ExtendedThreadFactory(), CounterWithSynchronizedBlock.class);
        testRaceCondition(new RunnableInterfaceThreadFactory(), CounterWithSynchronizedBlock.class);
    }

    public static void testRaceCondition(ThreadFactory threadFactory,
                                         Class<? extends Counter> counterImpl) {
        CounterExecutor counterExecutor;
        counterExecutor = new CounterExecutorImpl(threadFactory);
        System.out.println("\n\nTEST: " + threadFactory.getClass() + ", " + counterImpl.getName());
        System.out.println("============ Без synchronized (race condition)");
        counterExecutor.execute(false, ITERATION_COUNT, counterImpl);

        System.out.println("============ С synchronized");
        counterExecutor.execute(true, ITERATION_COUNT, counterImpl);
    }

}