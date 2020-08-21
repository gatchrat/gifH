package gifH;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;

public class GifCreator {
    public GifCreator(ArrayList<File> images, int waitTime, String name) {
        byte[] d;
        ByteBuffer data;

        try {
            Files.createDirectories(Paths.get("generated/"));
            File file = new File("generated/" + name + ".gif");
            FileOutputStream stream = new FileOutputStream(file, false);
            //HEAD
            data = ByteBuffer.allocate(10);
            //file format
            data.put((byte) 'G');
            data.put((byte) 'I');
            data.put((byte) 'F');
            //version
            data.put((byte) '8');
            data.put((byte) '9');
            data.put((byte) 'a');
            // SCREEN DESCRIPTOR
            int width = 10;
            int height = 10;
            data.put(Util.intTo2Byte(width));
            data.put(Util.intTo2Byte(height));
            stream.write(data.array());

            byte[] empty = new byte[1];
            empty[0] = 0;
            BitSet screenDescriptor = BitSet.valueOf(empty);
            boolean globalTable = true;
            screenDescriptor.set(7 - 0, globalTable);
            //1-3 Colorresolution
            // 4 = sort
            ArrayList<Color> colors = getDifferentColors(images);
            int globalTableSize = colors.size();
            if(globalTableSize == 1){
                globalTableSize++;
            }
            //round to next bigger power of 2
            while(Util.log2(globalTableSize) != Math.floor(Util.log2(globalTableSize))){
                globalTableSize++;
            }
            System.out.println("New Global Table size is " +globalTableSize);
            int globalTableBits = (int)Util.log2(globalTableSize)-1;
            if (globalTableBits >= 4) {
                screenDescriptor.set(7 - 5, true);
                globalTableBits -= 4;
            }
            if (globalTableBits >= 2) {
                screenDescriptor.set(7 - 6, true);
                globalTableBits -= 2;
            }
            if (globalTableBits >= 1) {
                screenDescriptor.set(7 - 7, true);
                globalTableBits -= 1;
            }
            stream.write(screenDescriptor.toByteArray());
            //background color index
            empty = new byte[1];
            empty[0] = 0;
            stream.write(empty);
            //pixel aspect ration
            empty = new byte[1];
            empty[0] = 0;
            stream.write(empty);
            //GLOBAL TABLE
            if(globalTable){
                byte[] globalTableBytes = new byte[globalTableSize*3];
                int numColors = globalTableSize;
                for (int i = 0; i < globalTableSize; i++) {
                    Color c;
                    if(i < colors.size()){
                        c = colors.get(i);
                    }
                    else{
                        c = colors.get(0);
                    }
                    globalTableBytes[i*3] =(byte)c.getRed();
                    globalTableBytes[i*3+1] =(byte)c.getGreen();
                    globalTableBytes[i*3+2] =(byte)c.getBlue();
                }
                stream.write(globalTableBytes);
            }
            //Custom Comment

            //LOOP
            //ImageDescriptor
            //ImageData


            // END
            stream.write(new byte[]{0x3b});
            stream.close();
        } catch (Exception e) {

        }
    }

    ArrayList<Color> getDifferentColors(ArrayList<File> files) throws IOException {
        ArrayList<Color> c = new ArrayList<Color>();
        for (File f : files) {
            BufferedImage image = ImageIO.read(f);
            int height = image.getHeight();
            int width = image.getWidth();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Color color = new Color(image.getRGB(col, row));
                    if (!c.contains(color)) {
                        c.add(color);
                    }
                }
            }

        }
        return c;
    }

    Color[][] getColorData(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int height = image.getHeight();
        int width = image.getWidth();
        Color[][] c = new Color[width][height];


        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                c[col][row] = new Color(image.getRGB(col, row));
            }
        }
        return c;
    }
}
