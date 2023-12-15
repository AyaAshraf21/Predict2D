import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Image {

    int width;
    int height;
    List<List<Integer>> originalList;
    List<List<Integer>> encodeList;
    List<List<Integer>> differenceList;
    List<List<Integer>> quantizerList;
    List<List<Integer>> dequantizerList;
    List<List<Integer>> decodeList;

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



    void putFirstRowColumn(List<List<Integer>> originalImage){
        //get first row and first column that in original image and put them in all lists
    }


    int predictPixel(int x , int y , List<List<Integer>>decode){
        // go to (x , y) in decode list and predict this pixel and then return the predict pixel
        return 0;
    }

    int getDifference(int x , int y){
        //go to (x , y) in originl image and mince it from the pixel in (x , y) in encode then return it
        return 0;
    }

    int getQuantizer(int x , int y){
        //go to (x , y) in difference list and go to quantizer range then return it
        return 0;
    }

    int getDequantizer(int x , int y){
        //go to (x , y) in difference list and go to quantizer range then return it
        return 0;
    }

    int decode(int x , int y){
        //go to (x , y) in dequantizer and add it to the pixel in (x , y) in encode then return this sum
        return 0;
    }



    public void compress(){

    }

    public void decompress(){

    }





}