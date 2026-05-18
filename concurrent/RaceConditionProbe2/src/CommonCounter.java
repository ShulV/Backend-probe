import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 18.05.2026 17:27
 */
public class CommonCounter {
    private int counter;

    public CommonCounter(int counter) {
        this.counter = counter;
    }

    public void count(Lock lock) {

        lock.lock();
        counter++;
        lock.unlock();

    }

    public void show() {
        System.out.println(counter);
    }
}
