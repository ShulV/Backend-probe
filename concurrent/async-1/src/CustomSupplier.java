import java.util.function.Supplier;

public class CustomSupplier implements Supplier<CustomObject> {
    @Override
    public CustomObject get() {
        return new CustomObject();
    }
}
