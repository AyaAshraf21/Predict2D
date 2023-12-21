import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<List<Integer>> encode = new ArrayList<>();
        Image image = new Image();
        image.width = 4;
        image.height = 4;
        List<List<Integer>> list = new ArrayList<>();

        List<Integer> row1 = new ArrayList<>();
        row1.add(5);
        row1.add(7);
        row1.add(8);
        row1.add(10);
        list.add(row1);

        // Create the second row and add it to the list
        List<Integer> row2 = new ArrayList<>();
        row2.add(6);
        row2.add(6);
        row2.add(9);
        row2.add(11);
        list.add(row2);

        List<Integer> row3 = new ArrayList<>();
        row3.add(7);
        row3.add(8);
        row3.add(11);
        row3.add(13);
        list.add(row3);

        List<Integer> row4 = new ArrayList<>();
        row4.add(9);
        row4.add(10);
        row4.add(11);
        row4.add(14);

        list.add(row4);
        image.originalList = list;


        //System.out.println(image.predictPixel(1,1 , list));
//        List<Quantizer> q = image.getQuantizerRanges();
//        for(Quantizer quantizer : q){
//            quantizer.printQuantizer();
//        }
//        System.out.println(image.putFirstRowColumn(list , encode));
        //image.readImage("D:\\im.jpg");
        //image.compress("D:\\im.jpg", "D:\\compressedFile.bin");
        image.decompress("D:\\compressedFile.bin","D:\\result.jpg");

    }
}