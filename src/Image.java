import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Image {

    private ReadWriteImage rWImage = new ReadWriteImage();
    public int width;
    public int height;
    public List<List<Integer>> originalList;
    List<List<Integer>> predictList;
    List<List<Integer>> differenceList;
    List<List<Integer>> quantizerList;
    List<List<Integer>> dequantizerList;
    List<List<Integer>> decodeList;

    List<Quantizer> quantizerRanges ;

    public List<List<Integer>> getImagePixels(String imagePath) {
        List<List<Integer>> pixels = new ArrayList<>();
        try {
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

    public void writeInFile(String fileName) {
        String binaryText = "", compressedText = "";
        int nBits = 8;
        //System.out.println(nBits);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(i == 0 || j == 0){
                    binaryText += String.format("%8s", Integer.toBinaryString(quantizerList.get(i).get(j) & 0xFF)).replace(' ', '0');
                }else{
                    binaryText += String.format("%" + nBits + "s", Integer.toBinaryString(quantizerList.get(i).get(j) & 0xFF)).replace(' ', '0');
                }
            }
        }

        int lastSubString = binaryText.length() % 8;
        if(lastSubString == 0)
            lastSubString = 8;
        for(int i = 0; i < binaryText.length(); i += 8) {
            String binaryString =  binaryText.substring(i, Math.min(i + 8, binaryText.length()));
            int intValue = Integer.parseInt(binaryString, 2);
            compressedText += (char) intValue;
        }
        try {
            Path filePath = Paths.get(fileName);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("File created successfully at: " + filePath.toAbsolutePath());
            }
        }
        catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
        }
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileName))) {
            /*System.out.println(binaryText);*/
            writeNeededInfoDecomp(lastSubString);
            writer.writeBytes(compressedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void print(){
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(originalList.get(i).get(j)+" ");
            }
            System.out.println();
        }
        System.out.println("pred List ");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(predictList.get(i).get(j)+ " ");
            }
            System.out.println();
        }
        System.out.println("diff List ");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(differenceList.get(i).get(j)+ " ");
            }
            System.out.println();
        }System.out.println("quan List ");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(quantizerList.get(i).get(j)+ " ");
            }
            System.out.println();
        }System.out.println("DEQuan List ");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(dequantizerList.get(i).get(j)+ " ");
            }
            System.out.println();
        }
        System.out.println("decoded List ");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(decodeList.get(i).get(j)+ " ");
            }
            System.out.println();
        }

        for(Quantizer q:quantizerRanges){
            q.printQuantizer();
            System.out.println("");
        }
    }
    public void compress(String imagePath, String outputFileName) {
        readImage(imagePath);
        originalList = getImagePixels(imagePath);
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
        //print();
        writeInFile(outputFileName);
    }
    void buildQuantizer(int min, double max, int step){
        quantizerRanges = new ArrayList<>();
        int start = min;
        int end =min + (step) - 1;
        int code = 0;
        int strQDash = (min + step / 2);
        while(end <= max){
            Quantizer q = new Quantizer();
            q.setStart(start);
            q.setEnd(end);
            q.setCode(code);
            q.setqDash(strQDash);
            quantizerRanges.add(q);
            strQDash += (step);
            start = end + 1;
            end = start + (step) - 1;
            code++;
        }

    }
    void readFile(String fileName){
        String text = "";
        File file = new File(fileName);
        try (FileInputStream reader = new FileInputStream(file)) {
            int content;
            while ((content = reader.read()) != -1) {
                char c = (char) content;
                text += c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int remainder = readNeededInfoComp();//this function also calls buildQuantizer function
        quantizerList = new ArrayList<>();
        fillList(quantizerList);
        convertAsciiToBinary(text,remainder);
//        quantizerList = new ArrayList<>();
//        fillList(quantizerList);
//        convertAsciiToBinary(text,remainder);

    }
    private void fillList(List<List<Integer>> ls) {
        for (int i = 0; i < height; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(0);
            }
            ls.add(row);
        }
    }
    void convertAsciiToBinary(String input, int lastSubString){
        String binaryText = "";
        for (int j = 0; j < input.length() - 1; j++) {
            binaryText += String.format("%8s", Integer.toBinaryString(input.charAt(j) & 0xFF)).replace(' ', '0');
        }
        binaryText += String.format("%" + lastSubString + "s", Integer.toBinaryString(input.charAt(input.length()-1) & 0xFF)).replace(' ', '0');
        int nBits = 8;
        //System.out.println(nBits);
        int start = 0;
        for(int i = 0; i < width && start < binaryText.length(); i++){
            for(int j = 0; j < height && start < binaryText.length(); j++){
                if(i == 0 || j == 0){
                    int val = Integer.parseInt(binaryText.substring(start, start + 8), 2);
                    quantizerList.get(i).set(j, val);
                    start += 8;
                } else {
                    int val = Integer.parseInt(binaryText.substring(start, start + nBits), 2);
                    quantizerList.get(i).set(j, val);
                    start += nBits;
                }
            }
        }


    }
    public void decompress(String inputFileName, String imagePath) {
        readFile(inputFileName);
        decodeList = new ArrayList<>();
        fillList(decodeList);
        for (int j = 0; j < width; j++) {
            decodeList.get(0).set(j, quantizerList.get(0).get(j));
        }
        for (int j = 0; j < height; j++) {
            decodeList.get(j).set(0, quantizerList.get(j).get(0));
        }
        dequantizerList = new ArrayList<>();
        fillList(dequantizerList);
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                dequantizerList.get(i).set(j, getDequantizedDiff(i, j));
                decodeList.get(i).set(j, (predictPixel(i, j, decodeList) + dequantizerList.get(i).get(j)));
            }
        }
        writeImage(imagePath);
    }

    void readImage(String imagePath){
        int[] rowsColomuns = new int[2];
        originalList = rWImage.convertImageTo2DArray(imagePath,rowsColomuns);
        height = rowsColomuns[0];
        width = rowsColomuns[1];
    }
    void writeImage(String imagePath){
        rWImage.convert2DArrayToImage(imagePath,decodeList);
    }
    void writeNeededInfoDecomp(int remender){
        String filePath = System.getProperty("user.dir") + File.separator + "quantizer.txt";
        try {
            File file = new File(filePath);
            if (file.exists()) {//delete its content(make it empty)
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.close();
            } else {// Create a new file
                file.createNewFile();
            }

            int min = 0, max = 0, step = 0;
            min = quantizerRanges.get(0).getStart();
            max = quantizerRanges.get(quantizerRanges.size() - 1).getEnd();
            step = (quantizerRanges.get(0).getEnd() - quantizerRanges.get(0).getStart() + 1);
            //System.out.println(height +" =h  w="+ width+" "+min + "=mn  mx="+max +"  step=" + step + " lasts" + remender);

            String contentToAppend = String.valueOf(height) + " " + String.valueOf(width) + " " + String.valueOf(min) + " " +
                    String.valueOf(max) + " " + String.valueOf(step) + " " + String.valueOf(remender) ;
            Files.write(Path.of(filePath), contentToAppend.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int readNeededInfoComp(){
        String filePath = System.getProperty("user.dir") + File.separator + "quantizer.txt";
        try {
            File file = new File(filePath);
            Scanner myReader = new Scanner(file);
            int min = 0, max = 0, step = 0, rem;
            height = myReader.nextInt();
            width = myReader.nextInt();
            min = myReader.nextInt();
            max = myReader.nextInt();
            step = myReader.nextInt();
            rem = myReader.nextInt();
            //System.out.println(height +" =h  w="+ width+" "+min + "=mn  mx="+max +"  step=" + step + " lasts" + rem);
            buildQuantizer(min,(double)max, step);
            myReader.close();
            return rem;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        return 0;
    }

}