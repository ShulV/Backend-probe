package counter;

public class CounterWithSynchronizedBlock implements Counter {
    private int count = 0;

    public CounterWithSynchronizedBlock(Boolean useSync) {
        this.useSync = useSync;
    }

    private final boolean useSync;

    @Override
    public void increment() {
        if (useSync) {
            synchronized (this) {
                count++;
            }
        } else {
            count++;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}