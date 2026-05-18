
public class CustomObject {
    int test1;
    int test2;

    public CustomObject() {
        this.test1 = 77;
    }

    @Override
    public String toString() {
        return "CustomObject{" +
                "test1=" + test1 +
                ", test2=" + test2 +
                '}';
    }
}
