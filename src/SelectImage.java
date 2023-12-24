import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

public class SelectImage {
    private JPanel panel1;
    private JButton compressButton;
    private JButton selectImageButton;
    private JButton newImageButton;
    private JLabel selectedPath;
    private JLabel newPath;
    private JLabel fitstLabel;
    private JLabel secondLabel;
    private JLabel mainLabel;
    private ImageIcon selectedImageIcon;
    private JTextField blockWidthField;
    private JTextField blockHeightField;
    private JTextField blocksNumField;

    private String imagePath;



//    JPanel getPanel1(){
//        return panel1;
//    }
//
//    public SelectImage(){
//        newImageButton.setPreferredSize(new Dimension(500, 400));
//        selectImageButton.setPreferredSize(new Dimension(500 , 75));
//        Color bg = new Color(242 , 136 , 255);
//        selectImageButton.setBackground(bg);
//        mainLabel.setText(" Select image and press Decompress");
//        fitstLabel.setText("Selected File");
//        secondLabel.setText("Decompressed Image");
//        compressButton.setText("Decompress");
//        selectImageButton.setText("Select File");
//        newImageButton.setText("Decompressed Image");
//        selectImageButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectFile();
//            }
//        });
//
//
//        compressButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                try {
//                    Random random = new Random();
//                    int randomThreeDigit = random.nextInt(900) + 100;
//                    String imageName = "newImage" + randomThreeDigit + ".jpg";
//                    VectorQuantization.decompress(imagePath, imageName);
//
//                    newPath.setText(imageName);
//                    imagePath = imageName;
//
//                    newImageButton.setText("");
//                    ImageIcon imageIcon = new ImageIcon(imagePath);
//                    Image image = imageIcon.getImage().getScaledInstance(
//                            newImageButton.getWidth(),
//                            newImageButton.getHeight(),
//                            Image.SCALE_SMOOTH
//                    );
//
//                    newImageButton.setIcon(new ImageIcon(image));
//                    newPath.setText(imagePath);
//                } catch (NumberFormatException a) {
//                    a.printStackTrace();
//                }
//
//            }
//        });
//    }
//
//
//
//
//    public SelectImage(JTextField blockWidthField, JTextField blockHeightField , JTextField blocksNumField) {
//        this.blockWidthField = blockWidthField;
//        this.blockHeightField= blockHeightField;
//        this.blocksNumField = blocksNumField;
//
//
//        selectImageButton.setPreferredSize(new Dimension(500, 400));
//        newImageButton.setPreferredSize(new Dimension(500 , 75));
//        Color bg = new Color(242 , 136 , 255);
//        newImageButton.setBackground(bg);
//        mainLabel.setText(" Select image and press Compress");
//        fitstLabel.setText("Selected Image");
//        secondLabel.setText("Compressed File");
//        newImageButton.setText("Compressed File");
//        selectImageButton.setText("Select Image");
//
//
//        selectImageButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectImage();
//            }
//        });
//
//        compressButton.setEnabled(false);
//        compressButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(canCompress()){
//                    try {
//                        int blocksNum = Integer.parseInt(blocksNumField.getText());
//                        int blockWidth = Integer.parseInt(blockWidthField.getText());
//                        int blockHeight = Integer.parseInt(blockHeightField.getText());
//
//                        Random random = new Random();
//                        int randomThreeDigit = random.nextInt(900) + 100;
//                        String imageName = "newImage" + randomThreeDigit + ".jpg";
//                        VectorQuantization.compress(VectorQuantization.getFinalBlocks(blocksNum,blockWidth,blockHeight,imagePath),
//                                ImageLoad.divideIntoBlocks(blockWidth,blockHeight,imagePath));
//
//                        newPath.setText(imageName);
//                        imagePath = imageName;
//
//                        newImageButton.setText("");
//                        ImageIcon imageIcon = new ImageIcon(imagePath);
//                        Image image = imageIcon.getImage().getScaledInstance(
//                                newImageButton.getWidth(),
//                                newImageButton.getHeight(),
//                                Image.SCALE_SMOOTH
//                        );
//
//                        newImageButton.setText(imagePath);
//                        newPath.setText(imagePath);
//                    } catch (NumberFormatException a) {
//                        a.printStackTrace();
//                    }
//
//                }
//                else{
//                    JFrame frame = new JFrame("BlockInfo");
//                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                    BlockInfo blockInfo = new BlockInfo();
//                    frame.setContentPane(blockInfo.getPanel1());
//                    frame.setSize(700, 400);
//                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//                    int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
//                    int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
//                    frame.setLocation(centerX, centerY);
//                    SwingUtilities.getWindowAncestor(panel1).dispose();
//                    frame.setVisible(true);
//
//                    blockInfo.getBlockWidth().setText(blockWidthField.getText());
//                    blockInfo.getBlockHeight().setText(blockHeightField.getText());
//                    blockInfo.getBlocksNum().setText(blocksNumField.getText());
//
//                }
//            }
//        });
//    }
//
//
//    boolean canCompress(){
//
//
//        try {
//            int blockWidth = Integer.parseInt(blockWidthField.getText());
//            int blockHeight = Integer.parseInt(blockHeightField.getText());
//            int imageWidth;
//            int imageHeight;
//
//            ImageLoad.getImagePixels(imagePath);
//            imageWidth = ImageLoad.width;
//            imageHeight = ImageLoad.height;
//
//            return imageWidth % blockWidth == 0 && imageHeight % blockHeight == 0;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    private void selectImage() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//
//        int result = fileChooser.showOpenDialog(panel1);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            String selectedImagePath = selectedFile.getAbsolutePath();
//            selectedPath.setText(selectedImagePath);
//            imagePath = selectedImagePath;
//
//            selectImageButton.setText("");
//            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
//            Image image = imageIcon.getImage().getScaledInstance(
//                    selectImageButton.getWidth(), selectImageButton.getHeight(), Image.SCALE_SMOOTH);
//
//            selectImageButton.setIcon(new ImageIcon(image));
//            compressButton.setEnabled(true);
//        }
//    }
//
//    private void selectFile() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//
//         FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary Files", "bin");
//         fileChooser.setFileFilter(filter);
//
//        int result = fileChooser.showOpenDialog(panel1);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            String selectedFilePath = selectedFile.getAbsolutePath();
//
//            imagePath = selectedFilePath;
//            selectedPath.setText(selectedFilePath);
//            selectImageButton.setText(selectedFilePath);
//
//             compressButton.setEnabled(true);
//        }
//    }

}
