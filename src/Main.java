import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Image image = new Image();

        image.compress("im.jpg", "compressedFile.bin");

        image.decompress("compressedFile.bin", "result.jpg");



    }


}