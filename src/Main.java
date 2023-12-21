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
//        image.compress("D:\\im.jpg", "D:\\compressedFile.bin");
        //image.readFile("D:\\compressedFile.bin");
        list = new ArrayList<>();

        row1 = new ArrayList<>();
        row1.add(5);
        row1.add(7);
        row1.add(8);
        row1.add(10);
        list.add(row1);

        // Create the second row and add it to the list
        row2 = new ArrayList<>();
        row2.add(6);
        row2.add(1);
        row2.add(2);
        row2.add(1);
        list.add(row2);

        row3 = new ArrayList<>();
        row3.add(7);
        row3.add(2);
        row3.add(2);
        row3.add(2);
        list.add(row3);

        row4 = new ArrayList<>();
        row4.add(9);
        row4.add(2);
        row4.add(1);
        row4.add(2);

        list.add(row4);/*
        image.quantizerList = list;
        System.out.println(image.quantizerList);*/
        image.compress("im.jpg", "compressedFile.bin");
        List<List<Integer>> originalQuantizerList = image.quantizerList;

        //System.out.println("decompress");
        image.decompress("compressedFile.bin", "result.jpg");



    }


}