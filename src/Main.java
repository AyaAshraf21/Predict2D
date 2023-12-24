import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Image image = new Image();
        //image.readImage("D:\\img.jpg");
        image.compress("D:\\im.jpg", "D:\\compressedFile.bin");

        image.decompress("D:\\compressedFile.bin", "D:\\result.jpg");


    }


}