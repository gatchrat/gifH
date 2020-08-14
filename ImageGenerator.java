import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageGenerator {
    public ImageGenerator(int[][] ColorTable, int width, int height, ArrayList<Integer> indexes) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixelIndex = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j <width ; j++) {
                int curIndex = indexes.get(pixelIndex);
                Color curCol = new Color(ColorTable[curIndex][0], ColorTable[curIndex][1], ColorTable[curIndex][2]);
                img.setRGB(j, i, curCol.getRGB());
                pixelIndex++;
            }
        }

        File file = new File("myimage.png");
        try {
            ImageIO.write(img, "png", file);
            //file = new File("myimage.jpg");
            //ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}