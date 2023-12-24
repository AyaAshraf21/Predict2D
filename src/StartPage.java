import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPage {
    private JPanel panel1;
    private JButton compressImageButton;
    private JButton decompressImageButton;

//    JPanel getPanel1(){
//        return panel1;
//    }
//    public StartPage() {
//
//    compressImageButton.addActionListener(new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("BlockInfo Demo");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            BlockInfo blockInfo = new BlockInfo();
//            frame.setContentPane(blockInfo.getPanel1());
//            frame.setSize(700, 400);
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
//            int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
//            frame.setLocation(centerX, centerY);
//            frame.setVisible(true);
//        });
//        }
//    });
//    decompressImageButton.addActionListener(new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            SelectImage selectImage = new SelectImage();
//            JFrame frame = new JFrame("Select Image");
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.setContentPane(selectImage.getPanel1());
//            frame.setSize(1200, 800);
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
//            int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
//            frame.setLocation(centerX, centerY);
//            frame.setVisible(true);
//            SwingUtilities.getWindowAncestor(panel1).dispose();
//        }
//    });
//    }
}
