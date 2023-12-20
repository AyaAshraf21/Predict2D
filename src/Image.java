import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Image {

    public int width;
    public int height;
    public List<List<Integer>> originalList;
    List<List<Integer>> predictList;
    List<List<Integer>> differenceList;
    List<List<Integer>> quantizerList;
    List<List<Integer>> dequantizerList;
    List<List<Integer>> decodeList;

    List<Quantizer> quantizerRanges ;

    public List<List<Integer>>  getImagePixels(String imagePath)
    {
        List<List<Integer>> pixels = new ArrayList<>();
        try{

            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            width = image.getWidth();
            height = image.getHeight();

            for (int y = 0; y < height; y++) {
                List<Integer> row = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    int grayValue = (pixel >> 16) & 0xFF;
                    row.add(grayValue);
                }
                pixels.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pixels;
    }



    public List<List<Integer>> putFirstRowColumn() {
        // Get first row and first column from the original image and put them in all lists
        List<List<Integer>> encodeImage = new ArrayList<>();
        // Initialize the encodeImage list
        for (int i = 0; i < height; i++) {
            encodeImage.add(new ArrayList<>());
        }

        // Put the first row
        for (int i = 0; i < width; i++) {
            encodeImage.get(0).add(originalList.get(0).get(i));
        }

        // Put the first column
        for (int i = 1; i < height; i++) {
            encodeImage.get(i).add(originalList.get(i).get(0));
        }

        // Fill the remaining cells with zeros
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                encodeImage.get(i).add(0);
            }
        }

        return encodeImage;
    }



    public int predictPixel(int x , int y , List<List<Integer>>decode){
        int predict;
        int A = decode.get(x).get(y-1);
        int C = decode.get(x-1).get(y);
        int B = decode.get(x-1).get(y-1);

        if(B >= Math.max(A, C)){
            predict = Math.min(A , C);
        }
        else if(B <= Math.min(A , C)){
            predict = Math.max(A, C);
        }
        else{
            predict = A+C-B;
        }
        return predict;
    }

    int getDifference(int x , int y , List<List<Integer>> anotherList){
        int originalPixel = originalList.get(x).get(y);
        int encodePixel = anotherList.get(x).get(y);
        int difference = originalPixel - encodePixel;

        return difference;
    }

    int getQuantizedDiff(int x , int y){
        //go to (x , y) in difference list and go to quantized range then return it
        int code = -1;
        int difference = differenceList.get(x).get(y);
        for(Quantizer q : quantizerRanges){
            if(difference >= q.getStart() && difference <= q.getEnd()){
                code = q.getCode();
                break;
            }
        }
        return code;
    }

    int getDequantizedDiff(int x , int y){
        //go to (x , y) in difference list and go to quantized range then return it
        int dequantizedDiff = 0;
        int quantizedDiff = quantizerList.get(x).get(y);
        for(Quantizer q : quantizerRanges){
            if(quantizedDiff == q.getCode()){
                dequantizedDiff = (q.getStart() + q.getEnd())/2;
                break;
            }
        }
        return dequantizedDiff;
    }

    int decode(int x , int y){
        //go to (x , y) in dequantizer and add it to the pixel in (x , y) in encode then return this sum
        int dequantizerPixel = dequantizerList.get(x).get(y);
        int encodePixel = predictList.get(x).get(y);

        int decode = dequantizerPixel + encodePixel;
        return decode;
    }


    public void getQuantizerRanges(){
        List<List<Integer>> predict = new ArrayList<>();
        List<List<Integer>> difference = new ArrayList<>();

        predict = putFirstRowColumn();
        difference = putFirstRowColumn();
        for(int i=1 ;i<width ;i++){
            for(int j=1 ; j<height ; j++){
                predict.get(i).set(j , predictPixel(i , j , originalList));
            }
        }
        for(int i=1 ;i<width ;i++){
            for(int j=1 ; j<height ; j++){
                difference.get(i).set(j , getDifference(i , j , predict));
            }
        }
        double max = -300;
        int min = 300;
        for(int i=1 ; i<width ; i++){
            for(int j=1 ; j<height ; j++){
                if(difference.get(i).get(j) > max){
                    max = difference.get(i).get(j);
                }
                if(difference.get(i).get(j) < min){
                    min = difference.get(i).get(j);
                }
            }
        }

        int step = 3;
        buildQuantizer(min,max,step);
    }

    public void writeInFile() {
        String fileName = "compressedFile.bin";
        int min = 0, max = 0, step = 0;
        min = quantizerRanges.get(0).getStart();
        max = quantizerRanges.get(quantizerRanges.size() - 1).getEnd();
        step = quantizerRanges.get(1).getStart() - quantizerRanges.get(0).getStart();
        String binaryText = "", compressedText = "";
        int nBits = (int)(Math.log(quantizerRanges.size())/ Math.log(2));


        try {
                Path filePath = Paths.get(fileName);
                Files.createFile(filePath);
                System.out.println("File created successfully at: " + filePath.toAbsolutePath());
                } catch (IOException e) {
                System.err.println("An error occurred while creating the file: " + e.getMessage());
                }
//                try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileName))) {
//                writer.writeBytes(width + " " +  + " " + compressedText);
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
    }
    public void compress() {
        getQuantizerRanges();
        decodeList = putFirstRowColumn();
        predictList = putFirstRowColumn();
        quantizerList = putFirstRowColumn();
        dequantizerList = putFirstRowColumn();
        differenceList = putFirstRowColumn();
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                predictList.get(i).set(j, predictPixel(i, j, decodeList));
                differenceList.get(i).set(j, getDifference(i, j, predictList));
                quantizerList.get(i).set(j, getQuantizedDiff(i, j));
                dequantizerList.get(i).set(j, getDequantizedDiff(i, j));
                decodeList.get(i).set(j, decode(i, j));
            }
        }
    }

    void buildQuantizer(int min, double max, int step){
        quantizerRanges.clear();
        double stepsNum = Math.ceil(max /step);
        int start = min;
        int end = step - 1;
        int code = 0;
        while(stepsNum > 0){
            Quantizer q = new Quantizer();
            q.setStart(start);
            q.setEnd(end);
            q.setCode(code);
            quantizerRanges.add(q);
            start += step;
            end += step;
            code++;
            stepsNum--;
        }

    }
    void readFile(String file){
        ArrayList<Integer> needed = new ArrayList<>();
        String text ="";
        try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
            int i=0;
            while(reader.available() > 0){
                 needed.add((int) reader.readByte());
                 i++;
                 if(i==6){
                     break;
                 }
            }
            while (reader.available() > 0) {
                char c = (char) reader.readByte();
                text += c;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        width = needed.get(0);
        height = needed.get(1);
        buildQuantizer(needed.get(2),(double)needed.get(3),needed.get(4));
        decodeList = new ArrayList<>();
        fillList(decodeList);
        quantizerList = new ArrayList<>();
        fillList(quantizerList);
        convertAsciiToBinary(text,needed.get(5));

    }
    void fillList(List<List<Integer>> ls){
        for(int i = 0; i < height; i++){
            List<Integer> row = new ArrayList<>(width);
            ls.add(row);
        }
    }
    void convertAsciiToBinary(String input, int lastSubString){
        String binaryText = "";
        for (int j = 0; j < input.length() - 1; j++) {
            binaryText += String.format("%8s", Integer.toBinaryString(input.charAt(j) & 0xFF)).replace(' ', '0');
        }
        binaryText += String.format("%" + lastSubString + "s", Integer.toBinaryString(input.charAt(input.length()-1) & 0xFF)).replace(' ', '0');
        int nBits = (int)(Math.log(quantizerRanges.size())/ Math.log(2));
        int start = 0;
        for(int i = 0; i < width && start < binaryText.length(); i++){
            for(int j = 0; j < height && start < binaryText.length(); j++){
                if(i == 0 || j == 0){
                    int val = Integer.parseInt(binaryText.substring(start, start + 8));
                    quantizerList.get(i).set(j, val);
                    start += 8;
                }else{
                    int val = Integer.parseInt(binaryText.substring(start, start + nBits));
                    quantizerList.get(i).set(j, val);
                    start += nBits;
                }
            }
        }

    }
    public void decompress(){
        int i = 0;
        for(int j = 0; j < width; j++){
            decodeList.get(0).set(j, quantizerList.get(0).get(j));
        }

    }
    static String readFromFileBinary(String pathName) {
        File file = new File(pathName);
        String text = "";
        try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
            while (reader.available() > 0) {
                char c = (char) reader.readByte();
                text += c;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

}
