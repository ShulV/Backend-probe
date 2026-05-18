import java.util.concurrent.Callable;


public class CustomCallable implements Callable<CustomObject> {


    @Override
    public CustomObject call() throws Exception {

        System.out.println("start call");
        Thread.sleep(2000);
        System.out.println("end call");
        return new CustomObject();
    }
}
