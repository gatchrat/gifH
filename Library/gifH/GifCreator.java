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
    public GifCreator(ArrayList<File> images, GIFSettings settings, String name) {
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
            int[] sizes = getMaxSize(images);
            settings.width = sizes[0];
            settings.height = sizes[1];
            data.put(Util.intTo2Byte(settings.width));
            data.put(Util.intTo2Byte(settings.height));
            stream.write(data.array());

            byte[] empty = new byte[1];
            BitSet screenDescriptor = BitSet.valueOf(empty);
            boolean globalTable = false;
            screenDescriptor.set(7 - 0, globalTable);
            int colorresolution = settings.colorResolution;
            //1-3 Colorresolution
            if (colorresolution >= 4) {
                screenDescriptor.set(7 - 1, true);
                colorresolution -= 4;
            }
            if (colorresolution >= 2) {
                screenDescriptor.set(7 - 2, true);
                colorresolution -= 2;
            }
            if (colorresolution >= 1) {
                screenDescriptor.set(7 - 3, true);
                colorresolution -= 1;
            }
            // 4 = sort
            ArrayList<Color> colors = getDifferentColors(images.get(0));
            int globalTableSize = colors.size();
            if (globalTableSize == 1) {
                globalTableSize++;
            }
            //round to next bigger power of 2
            while (Util.log2(globalTableSize) != Math.floor(Util.log2(globalTableSize))) {
                globalTableSize++;
            }
            int globalTableBits = (int) Util.log2(globalTableSize) - 1;
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
            stream.write(empty);
            //pixel aspect ratio
            empty = new byte[1];
            stream.write(empty);
            //GLOBAL TABLE
            if (globalTable) {
                byte[] globalTableBytes = new byte[globalTableSize * 3];
                int numColors = globalTableSize;
                for (int i = 0; i < globalTableSize; i++) {
                    Color c;
                    if (i < colors.size()) {
                        c = colors.get(i);
                    } else {
                        c = colors.get(0);
                    }
                    globalTableBytes[i * 3] = (byte) c.getRed();
                    globalTableBytes[i * 3 + 1] = (byte) c.getGreen();
                    globalTableBytes[i * 3 + 2] = (byte) c.getBlue();
                }
                stream.write(globalTableBytes);
            }
            //Custom Comment

            //LOOP
            for (File f : images) {
                //Graphic Control Extension
                byte[] graphicControlExtension = new byte[8];
                graphicControlExtension[0] = (byte) 0x21;
                graphicControlExtension[1] = (byte) 0xF9;
                graphicControlExtension[2] = (byte) 0x04;
                //packed field
                BitSet GCEBitset = new BitSet(8);
                int disposalMethod = settings.disposalMethod;
                if (disposalMethod >= 4) {
                    GCEBitset.set(7 - 3, true);
                    disposalMethod -= 4;
                }
                if (disposalMethod >= 2) {
                    GCEBitset.set(7 - 4, true);
                    disposalMethod -= 2;
                }
                if (disposalMethod >= 1) {
                    GCEBitset.set(7 - 5, true);
                }
                //1 bit userinput flag
                // 1 bit transparency flag
                if(GCEBitset.toByteArray().length >0){
                    graphicControlExtension[3] = GCEBitset.toByteArray()[0];
                }
                //delay time
                int delayTime = settings.delayTime;
                graphicControlExtension[4] = Util.intTo2Byte(delayTime)[0];
                graphicControlExtension[5] = Util.intTo2Byte(delayTime)[1];
                //transparent color index
                int transparentIndex = 0;
                graphicControlExtension[6] = (byte) transparentIndex;
                //terminator
                stream.write(graphicControlExtension);



                //ImageDescriptor
                byte[] imageDescriptor = new byte[10];
                BufferedImage image = ImageIO.read(f);
                imageDescriptor[0] = (byte) 0x2c;
                //2 byte left pos
                //2 byte right pos
                //width
                imageDescriptor[5] = Util.intTo2Byte(image.getWidth())[0];
                imageDescriptor[6] = Util.intTo2Byte(image.getWidth())[1];
                //height
                imageDescriptor[7] = Util.intTo2Byte(image.getHeight())[0];
                imageDescriptor[8] = Util.intTo2Byte(image.getHeight())[1];
                //settings
                empty = new byte[1];
                BitSet localSettings = BitSet.valueOf(empty);

                boolean localTable = true;
                ArrayList<Color> localColors = getDifferentColors(f);
                int localTableSize = localColors.size();
                if (localTableSize == 1) {
                    localTableSize++;
                }
                //round to next bigger power of 2
                while (Util.log2(localTableSize) != Math.floor(Util.log2(localTableSize))) {
                    localTableSize++;
                }

                System.out.println("New Local Table size is " + globalTableSize);
                int localTableBits = (int) Util.log2(localTableSize) - 1;
                localSettings.set(7 - 0, localTable);
                //interlace
                //sort
                //2xreserved
                //Table Size
                if (localTable) {
                    if (localTableBits >= 4) {
                        localSettings.set(7 - 5, true);
                        localTableBits -= 4;
                    }
                    if (localTableBits >= 2) {
                        localSettings.set(7 - 6, true);
                        localTableBits -= 2;
                    }
                    if (localTableBits >= 1) {
                        localSettings.set(7 - 7, true);
                        localTableBits -= 1;
                    }
                } else {
                    localTableBits = globalTableBits;
                    localTableSize = globalTableSize;
                }
                if (localSettings.size() == 0) {
                    imageDescriptor[9] = 0x00;
                } else {
                    imageDescriptor[9] = localSettings.toByteArray()[0];
                }
                stream.write(imageDescriptor);
                //LocalColorTable
                if (localTable) {
                    byte[] localTableBytes = new byte[localTableSize * 3];
                    int numColors = globalTableSize;
                    for (int i = 0; i < globalTableSize; i++) {
                        Color c;
                        if (i < localColors.size()) {
                            c = localColors.get(i);
                        } else {
                            c = localColors.get(0);
                        }
                        localTableBytes[i * 3] = (byte) c.getRed();
                        localTableBytes[i * 3 + 1] = (byte) c.getGreen();
                        localTableBytes[i * 3 + 2] = (byte) c.getBlue();
                    }
                    stream.write(localTableBytes);
                }
                //ImageData
                //initial size
                byte[] initSize = new byte[1];
                initSize[0] = (byte) ((int) Util.log2(localTableSize));
                stream.write(initSize);
                //pure data
                stream.write(LZWEncoder.encodeImage(f, localTableSize, localColors));
                //0 byte block
                empty = new byte[1];
                empty[0] = 0x00;
                stream.write(empty);
            }


            // END
            stream.write(new byte[]{0x3b});
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<Color> getDifferentColors(File file) throws IOException {
        ArrayList<Color> c = new ArrayList<Color>();

        BufferedImage image = ImageIO.read(file);
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


        return c;
    }

    int[] getMaxSize(ArrayList<File> images) throws IOException {
        int[] size = new int[2];
        for (File f : images) {
            BufferedImage image = ImageIO.read(f);
            int height = image.getHeight();
            int width = image.getWidth();
            size[0] = Math.max(size[0], width);
            size[1] = Math.max(size[1], height);
        }
        return size;
    }
}
