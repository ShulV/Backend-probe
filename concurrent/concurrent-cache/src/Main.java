import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

void main() {
    final int N_THREADS = 8;
    Cache cache = new CacheImpl(new UpdaterImpl());
    try (ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS)) {
        //todo
    }
}
