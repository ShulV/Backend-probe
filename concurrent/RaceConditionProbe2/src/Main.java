import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        CommonCounter commonCounter = new CommonCounter(0);
        Lock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                commonCounter.count(lock);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                commonCounter.count(lock);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        commonCounter.show();
    }

}