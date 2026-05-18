import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//PS Run → Edit Configurations → VM options → добавить: -ea
//для асертов!
public class FutureProbe {
    public static void main(String[] args) {
        //Feature probe
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Future<CustomObject> future = executor.submit(new CustomCallable());
            try {
                CustomObject result = future.get();
                System.out.println(result);
                assert result.test1 == 77;
                assert result.test2 == 0;
            } catch (ExecutionException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}