import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Image image = new Image();
        image.compress("D:\\img.jpg", "D:\\compressedFile.bin");

        image.decompress("D:\\compressedFile.bin", "D:\\res.jpg");


    }


}