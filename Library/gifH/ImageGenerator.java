package gifH;

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
    BufferedImage prevImg ;
    public ImageGenerator(){

    }
    public void generateImage(int[][] ColorTable, int width, int height, ArrayList<Integer> indexes,String name,int disposalMethod) {
        BufferedImage img;

        switch (disposalMethod) {
            //Replace all
            case 0:
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                break;
            //Replace with non transparent
            case 1:
                //needs transparency
                if (count != 0) {
                    //if size changed copy image and refit
                    if(prevImg.getWidth() != width || prevImg.getHeight() != height){
                        img =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                        for (int i = 0; i < Math.min(width,prevImg.getWidth()) ; i++) {
                            for (int j = 0; j < Math.min(height,prevImg.getHeight()) ; j++) {
                                img.setRGB(i, j, prevImg.getRGB(i,j));
                            }
                        }
                    }
                    else{
                        img = prevImg;
                    }


                }
                else{
                    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                }
                break;
            //Replace Background with non transparent
            case 2:
                //needs transparency and background
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                break;
            case 3:
                // Restore to previus (only works when size stays the same)
                if (count != 0) {
                    img = prevImg;
                }
                else{
                    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                }
                break;
            default:
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                break;
        }
         
        int pixelIndex = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j <width ; j++) {
                int curIndex = indexes.get(pixelIndex);
                Color curCol = new Color(ColorTable[curIndex][0], ColorTable[curIndex][1], ColorTable[curIndex][2]);
                img.setRGB(j, i, curCol.getRGB());
                pixelIndex++;
                if(pixelIndex == indexes.size() && pixelIndex != width*height){
                    j = 99999;
                    i = 99999;
                }
            }
        }
        prevImg = img;
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