
public class VolatileDemo {
    static volatile boolean flag = true;
    // Без volatile вложенный поток не узнает об изменении flag, тк хранит её в своём кэше

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            int i = 0;
            while (flag) i++;  // счётчик для нагрузки
            System.out.println("STOPPED after " + i + " iterations");
        }).start();

        Thread.sleep(2000);
        flag = false;
        System.out.println("Signal sent. Check if worker stopped...");
        Thread.sleep(3000);
    }
}

