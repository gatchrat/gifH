package gifH;

import java.io.File;
import java.io.FileOutputStream;
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
            data = ByteBuffer.allocate(11);
            //file format
            data.put((byte) 'G');
            data.put((byte) 'I');
            data.put((byte) 'F');
            //version
            data.put((byte) '8');
            data.put((byte) '9');
            data.put((byte) 'a');
            int width = 10;
            int height = 10;
            data.put(Util.intTo2Byte(width));
            data.put(Util.intTo2Byte(height));
            stream.write(data.array());
            // SCREEN DESCRIPTOR
            byte[] empty = new byte[1];
            empty[0] = 0;
            BitSet screenDescriptor = BitSet.valueOf(empty);
            boolean globalTable = false;
            int colorResolution = 0;
            screenDescriptor.set(7 - 0, true);
            //1-3 Colorresolution
            // 4 = sort
            int globalTableSize = 3;
            int globalTableBits = 2;
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
            //GLOBAL TABLE

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
}
