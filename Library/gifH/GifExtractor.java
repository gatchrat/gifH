package gifH;

import java.util.ArrayList;
import java.util.BitSet;

public class GifExtractor {
    public GifExtractor(byte[] gif, GIFSettings settings) {
        // HEAD

        if (checkIfValidGif(gif)) {
            settings.version = "" + (char)gif[3] + (char)gif[4] + (char)gif[5];
            settings.width = ((gif[7] & 0xff) << 8) | (gif[6] & 0xff);
            settings.height = ((gif[9] & 0xff) << 8) | (gif[8] & 0xff);
            System.out.println("Image is " + settings.width + "x" + settings.height);
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
            settings.colorResolution = colorResolution;
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
                System.out.println("A global Color Table with " + globalTableSize / 3 + " colors is being used");
            }
            // Skip 2 bytes
            int byteIndex = 13;
            // GLOBAL COLOR TABLE

            int[][] GlobalColorTable = new int[globalTableSize / 3][3];
            for (int i = 0; i < globalTableSize / 3; i++) {
                for (int j = 0; j < 3; j++) {
                    GlobalColorTable[i][j] = gif[byteIndex] & 0xFF;
                    byteIndex++;
                }
            }
            // BLOCKS
            int imgIndex = 1;
            int disposalMethod = 0;
            ImageGenerator imgG = new ImageGenerator();
            while (gif[byteIndex] != 0x3B) {
                switch (gif[byteIndex]) {
                    case 0x21:
                        byteIndex++;
                        switch (gif[byteIndex]) {
                            case (byte) 0xf9:
                                // Control Extension
                                byteIndex++;
                                // size (always 4)
                                byteIndex++;
                                // settings
                                BitSet controlSettings = BitSet.valueOf(new byte[] { gif[byteIndex] });
                                System.out.println(controlSettings.toString());
                                boolean transparency = false;
                                disposalMethod = 0;
                                if (controlSettings.get(7 - 0)) {
                                    // reserved
                                }
                                if (controlSettings.get(7 - 1)) {
                                    // reserved;
                                }
                                if (controlSettings.get(7 - 2)) {
                                    // reserved
                                }
                                if (controlSettings.get(7 - 3)) {
                                    disposalMethod += 4;
                                }
                                if (controlSettings.get(7 - 4)) {
                                    disposalMethod += 2;
                                }
                                if (controlSettings.get(7 - 5)) {
                                    disposalMethod += 1;
                                }
                                settings.disposalMethod = disposalMethod;
                                if (controlSettings.get(7 - 6)) {
                                    // User Input
                                }
                                if (controlSettings.get(7 - 7)) {
                                    // transparency
                                    transparency = true;
                                }
                                byteIndex++;
                                // delay time
                                int delay = ((gif[byteIndex + 1] & 0xff) << 8) | (gif[byteIndex] & 0xff);
                                settings.delayTime = delay;
                                byteIndex++;
                                byteIndex++;
                                // transparent color index
                                byteIndex++;
                                // terminator
                                byteIndex++;
                                break;
                            case (byte) 0xff:
                            System.out.println("A");
                                // Application
                                byteIndex += 14;
                                break;
                            case (byte) 0xfe:
                            System.out.println("C");
                                // Comment
                                byteIndex++;
                                // data
                                while (gif[byteIndex] != 0x00 ) {
                                    byteIndex++;
                                }
                                // ending
                                byteIndex++;
                                break;

                            default:
                                break;
                        }
                        break;

                    case 0x2c:

                        byteIndex++;
                        // IMAGE DESCRIPTOR
                        // 2 byte left pos
                        byteIndex += 2;
                        settings.leftPos.add(0);
                        // 2 byte right pos
                        byteIndex += 2;
                        settings.rightPos.add(0);
                        int localWidth = ((gif[byteIndex + 1] & 0xff) << 8) | (gif[byteIndex] & 0xff);
                        byteIndex += 2;
                        int localHeight = ((gif[byteIndex + 1] & 0xff) << 8) | (gif[byteIndex] & 0xff);
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
                            System.out.println("A Local Color Table with " + colorSize / 3 + " colors is being used");
                        }
                        // LOCAL COLOR TABLE
                        int[][] LocalColorTable = new int[colorSize][3];
                        if (colorSize != 0) {

                            for (int i = 0; i < colorSize / 3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    LocalColorTable[i][j] = gif[byteIndex] & 0xFF;
                                    byteIndex++;
                                }
                            }
                        }
                        // IMAGE DATA
                        LZWDecoder decoder = new LZWDecoder();
                        int initCode = gif[byteIndex];
                        byteIndex++;
                        ArrayList<Integer> indexList = new ArrayList<Integer>();
                        // count bytes
                        int size = 0;
                        int oldIndex = byteIndex;
                        while (gif[byteIndex] != 0) {
                            size += gif[byteIndex] & 0xff;
                            byteIndex += gif[byteIndex] & 0xff;
                            byteIndex++;
                        }
                        byteIndex = oldIndex;

                        // System.out.println("Image Data size is " + size + "bytes");
                        byte[] imageData = new byte[size];
                        int index = 0;
                        while (gif[byteIndex] != 0) {
                            // blocksize
                            int curSize = gif[byteIndex] & 0xff;

                            byteIndex++;
                            for (int i = 0; i < curSize; i++) {
                                imageData[index] = gif[byteIndex + i];
                                index++;
                            }

                            byteIndex = byteIndex + curSize;
                        }
                        // null block
                        byteIndex++;
                        imageData = Util.reverseByteArray(imageData);
                        if (localTable) {
                            indexList.addAll(decoder.Decode(initCode, imageData, LocalColorTable.length/3));
                        } else {
                            indexList.addAll(decoder.Decode(initCode, imageData, GlobalColorTable.length));
                        }

                        if (indexList.size() != localWidth * localHeight) {
                            System.out.println("Missing " + (localWidth * localHeight - indexList.size()) + "indexes");
                        }

                        if (localTable) {
                            imgG.generateImage(LocalColorTable, localWidth, localHeight, indexList, String.valueOf(imgIndex),settings);
                        } else {
                            imgG.generateImage(GlobalColorTable, localWidth, localHeight, indexList, String.valueOf(imgIndex),settings);
                        }

                        imgIndex++;
                        break;

                    default:
                        // calculated a block wrong, go to next block
                        byteIndex++;
                        System.out.println("skip");
                        break;
                }

            }
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