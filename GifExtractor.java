import java.util.ArrayList;
import java.util.BitSet;

public class GifExtractor {
    public GifExtractor(byte[] gif) {
        // HEAD

        if (checkIfValidGif(gif)) {
            System.out.println("The Version is:" + (char) gif[3] + (char) gif[4] + (char) gif[5]);
            int width = ((gif[7] & 0xff) << 8) | (gif[6] & 0xff);
            int height = ((gif[9] & 0xff) << 8) | (gif[8] & 0xff);
            System.out.println("Image is " + width + "x" + height);
            // SCREEN DESCRIPTOR
            BitSet screenDescriptor = BitSet.valueOf(new byte[] { gif[10] });
            boolean globalTable = false;
            int colorResolution = 0;
            if (screenDescriptor.get(7 - 0)) {
                globalTable = true;
            }
            if (screenDescriptor.get(7 - 1)) {
                colorResolution += 4;
            }
            if (screenDescriptor.get(7 - 2)) {
                colorResolution += 2;
            }
            if (screenDescriptor.get(7 - 3)) {
                colorResolution += 1;
            }
            colorResolution += 1;
            // 4 = sort
            int globalTableSize = 0;
            if (globalTable) {
                if (screenDescriptor.get(7 - 5)) {
                    globalTableSize += 4;
                }
                if (screenDescriptor.get(7 - 6)) {
                    globalTableSize += 2;
                }
                if (screenDescriptor.get(7 - 7)) {
                    globalTableSize += 1;
                }
                globalTableSize = 3 * (int) Math.pow(2, globalTableSize + 1);
                System.out.println("Eine Globale Farbentabelle mit " + globalTableSize + " bytes wird genutzt");
            }
            // Skippe noch 2 bytes wegen unwichtige info
            int byteIndex = 13;
            // GLOBAL COLOR TABLE

            int[][] GlobalColorTable = new int[globalTableSize / 3][3];
            for (int i = 0; i < globalTableSize / 3; i++) {
                for (int j = 0; j < 3; j++) {
                    GlobalColorTable[i][j] = gif[byteIndex] & 0xFF;
                    byteIndex++;
                }
            }
            // Suche nach image descriptor
            while (gif[byteIndex] != 0x2c) {
                byteIndex++;
            }
            // IMAGE DESCRIPTOR
            // 2 byte left pos
            byteIndex += 2;
            // 2 byte right pos
            byteIndex += 2;
            int localWidth = ((gif[byteIndex] & 0xff) << 8) | (gif[byteIndex + 1] & 0xff);
            byteIndex += 2;
            int localHeight = ((gif[byteIndex] & 0xff) << 8) | (gif[byteIndex + 1] & 0xff);
            byteIndex += 2;
            System.out.println("Cur. Image is " + localWidth + "x" + localHeight);
            // bits
            BitSet imageDescriptor = BitSet.valueOf(new byte[] { gif[byteIndex] });
            byteIndex += 1;
            boolean localTable = false;
            boolean interlace = false;
            if (imageDescriptor.get(7 - 0)) {
                localTable = true;

            }
            if (imageDescriptor.get(7 - 1)) {
                interlace = true;
                System.out.println("The image is interlaced");
            }
            if (imageDescriptor.get(7 - 2)) {
                // sort
            }
            // 3+4 reserved
            int colorSize = 0;

            if (imageDescriptor.get(7 - 5)) {
                colorSize += 4;
            }
            if (imageDescriptor.get(7 - 6)) {
                colorSize += 2;
            }
            if (imageDescriptor.get(7 - 7)) {
                colorSize += 1;
            }

            if (localTable) {
                colorSize = 3 * (int) Math.pow(2, colorSize + 1);
                System.out.println("A Local Color Table with " + colorSize + " bytes is being used");
            }
            // LOCAL COLOR TABLE
            if (colorSize != 0) {
                int[][] LocalColorTable = new int[colorSize][3];

                for (int i = 0; i < colorSize / 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        LocalColorTable[i][j] = gif[byteIndex];
                        byteIndex++;
                    }
                }
            }
            byteIndex++;
            // IMAGE DATA
            LZWDecoder decoder = new LZWDecoder();
            int initCode = gif[byteIndex];
            byteIndex++;
            ArrayList<Integer> indexList = new ArrayList<Integer>();
            //count bytes
            int size = 0;
            int oldIndex = byteIndex;
            while (gif[byteIndex] != 0) {
                size += gif[byteIndex] & 0xff;
                byteIndex += gif[byteIndex] & 0xff;
                byteIndex++;
            }
            byteIndex = oldIndex;

            System.out.println("Image Date is " + size + "bytes");
            byte[] imageData = new byte[size];
            int index = 0;
            while (gif[byteIndex] != 0) {
                // blocksize gucken
                int curSize = gif[byteIndex] & 0xff;
                
                byteIndex++;
                for (int i = 0; i <curSize; i++) {
                    imageData[index] = gif[byteIndex + i];
                    index++;
                }
                
                byteIndex = byteIndex + curSize;
            }
            imageData = Util.reverseByteArray(imageData);
            indexList.addAll(  decoder.Decode(initCode, imageData, GlobalColorTable.length));
                System.out.println("Extracted " + indexList.size() + " indexes");
            ImageGenerator img = new ImageGenerator(GlobalColorTable, width, height, indexList);
            // REPEAT BLOCKS TILL END

            // END
        } else {
            System.out.println("This doesnt seem to be a valid file");
        }
    }

    private boolean checkIfValidGif(byte[] gif) {
        if ((char) gif[0] == 'G' && (char) gif[1] == 'I' && (char) gif[2] == 'F') {
            return true;
        }
        return false;
    }
}