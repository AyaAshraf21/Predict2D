import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ReadWriteImage {

    public List<List<Integer>> convertImageTo2DArray(String imagePath, int[] rowsColumns) {
        try {
            // Load the image using ImageIO
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Get the dimensions of the image
            rowsColumns[0] = image.getHeight();
            rowsColumns[1] = image.getWidth();

            // Convert the image to a 2D array
            List<List<Integer>> result = new ArrayList<>();
            for (int i = 0; i < rowsColumns[0]; i++) {
                List<Integer> row = new ArrayList<>();
                for (int j = 0; j < rowsColumns[1]; j++) {
                    row.add(image.getRGB(j, i) & 0xFF);
                }
                result.add(row);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

        public void convert2DArrayToImage(String imageName, List<List<Integer>> image){

            BufferedImage theImage = new BufferedImage(image.size(), image.get(0).size(), BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < image.get(0).size(); i++) {
                for (int j = 0; j < image.size(); j++) {
                    int value = image.get(j).get(i) << 16 | image.get(j).get(i) << 8 | image.get(j).get(i);
                    theImage.setRGB(i, j, value);
                }
            }


            File outputfile = new File(imageName);
            try {
                ImageIO.write(theImage, "jpg", outputfile);
            } catch (IOException e1) {

            }
        }
}
