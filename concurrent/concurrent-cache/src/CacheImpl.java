
public class CacheImpl implements Cache {

    private Updater updater;
    private long[] cache;

    public CacheImpl(Updater updater) {
        this.cache = new long[10];
        this.updater = updater;
    }

    /**
     * Метод для обновления кэша через мутацию
     */
    @Override
    public void bulkUpdate() {
        long[] copy = cache.clone();

        updater.updateCurrentState(copy);

        cache = copy;
    }

    /**
     * @param indices new int[]{1, 3, 5}
     * @return cache[1], cache[3], cache[5]
     * Метод, который принимает индексы для чтения
     */
    @Override
    public long[] bulkRead(int[] indices) {
        long[] snapshotState = new long[indices.length];
        for (int i = 0; i < indices.length; i++) {
            snapshotState[i] = cache[indices[i]];
        }
        return snapshotState;
    }

}
