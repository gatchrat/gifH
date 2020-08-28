package gifH;

import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class ImageGenerator {
    private int count =0;
    BufferedImage prevImg = null;
    public ImageGenerator(){

    }
    public void generateImage(int[][] ColorTable, int width, int height, ArrayList<Integer> indexes,String name,GIFSettings settings) {
        BufferedImage img;

        if (prevImg != null){
            img = prevImg;
        }
        else{
            img = new BufferedImage(width,height, ColorSpace.TYPE_RGB);
        }

        int pixelIndex = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j <width ; j++) {
                int curIndex = indexes.get(pixelIndex);
                if(!settings.hasTransparency || ( settings.transparencyIndex == curIndex)){
                    Color curCol = new Color(ColorTable[curIndex][0], ColorTable[curIndex][1], ColorTable[curIndex][2]);
                    img.setRGB(j+settings.leftPos, i+settings.topPos, curCol.getRGB());
                    pixelIndex++;
                    if(pixelIndex == indexes.size() && pixelIndex != width*height){
                        j = 99999;
                        i = 99999;
                    }
                }

            }
        }
        switch (settings.disposalMethod) {
            case 0:
                prevImg = null;
                break;
            case 1:
                //do not dispose
               prevImg = img;
                break;

            case 2:
                //restore to background
                Graphics2D t = img.createGraphics();
                t.setColor(new Color(ColorTable[settings.backgroundIndex][0], ColorTable[settings.backgroundIndex][1], ColorTable[settings.backgroundIndex][2]));
                t.fillRect(0,0,img.getWidth(),img.getHeight());
                prevImg = img;
                break;
            case 3:
                // Restore to previus
                break;
            default:
                System.out.println("Unkown Disposal Method");
                break;
        }
        count++;
        try {
            Files.createDirectories(Paths.get("extracted/"));
            File file = new File("extracted/"+name+".png");
            ImageIO.write(img, "png", file);
            //file = new File("myimage.jpg");
            //ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}