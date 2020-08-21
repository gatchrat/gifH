package gifH;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class LZWEncoder {
    public static byte[] encodeImage(File f, int colorTableSize, ArrayList<Color> colorTable) throws IOException {
        //[code,bitLength],[...],...
        colorTableSize = Math.max(4,colorTableSize);
        ArrayList<Integer[]> codes = new ArrayList<Integer[]>();
        int bitsToRead = (int)Util.log2(colorTableSize)+1;
        System.out.println("Start with " +bitsToRead + " bits");
        ArrayList<ArrayList<Integer>> codeTable = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> indexBuffer = new ArrayList<Integer>();
        int resetCode = colorTableSize;
        int endCode = colorTableSize+1;
        //init codetable
        for (int i = 0; i < resetCode; i++) {
            codeTable.add(new ArrayList<Integer>());
            codeTable.get(codeTable.size() - 1).add(i);
        }
        // fill empty space
        while (codeTable.size() < resetCode) {
            codeTable.add(new ArrayList<Integer>());
        }
        // add 2 special values
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(resetCode);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(endCode);
        //start with reset code
        codes.add(new Integer[] {resetCode, bitsToRead});
        //add rest of data
        BufferedImage image = ImageIO.read(f);
        int height = image.getHeight();
        int width = image.getWidth();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = new Color(image.getRGB(col, row));
                int index = colorTable.indexOf(color);
                indexBuffer.add(index);
                //if not first code
                if(!(row == col && col == 0)) {
                    if (codeTable.contains(indexBuffer)) {
                        //keep buffer
                    } else {
                        codeTable.add(new ArrayList<>(indexBuffer));
                        indexBuffer.remove(indexBuffer.size() - 1);
                        int code = codeTable.indexOf(indexBuffer);
                        codes.add(new Integer[]{code, bitsToRead});
                        System.out.println(code);
                        if(codeTable.size()-1 == (int)Math.pow(2,bitsToRead)){
                            bitsToRead++;
                           System.out.println("CodeSize:"+bitsToRead);
                        }
                        indexBuffer.clear();
                        indexBuffer.add(index);
                    }
                }
            }
        }
        //add last code
        codes.add(new Integer[] {codeTable.indexOf(indexBuffer), bitsToRead});
        if(codeTable.indexOf(indexBuffer) == (int)Math.pow(2,bitsToRead)-1){
            bitsToRead++;
        }
        System.out.println(codeTable.indexOf(indexBuffer));
        //END
        codes.add(new Integer[] {endCode, bitsToRead});
        return LZWEncoder.codeToData(codes);
    }
    private static <Bitset> byte[] codeToData(ArrayList<Integer[]> codes){
        //[Size][second Code,First Code][Third Code, Second Code] [0x00]

        int curNumBytes = 0;
        int bitsNeeded = 0;
        for (int i = 0; i < codes.size(); i++) {
            bitsNeeded = bitsNeeded + codes.get(i)[1];
        }
        int length = (int) Math.ceil(bitsNeeded/8);
        System.out.println("Will be " + length + " bytes");
        if(length > 255){
            System.out.println("Too much Data, cant handle");
        }
        int byteIndex = 0;
        int bitOffset = 0;
        //Start with last bit, work backwards
        BitSet bits = new BitSet(length*8);
        int index = length*8-1;
        for (int i = 0; i <codes.size() ; i++) {
            boolean[] codeBits = Util.intToBit(codes.get(i)[0],codes.get(i)[1]);
            for (int j = 0; j < codeBits.length; j++) {
                if(codeBits[j]){
                    bits.set(length*8 -(byteIndex * 8 + 8 - bitOffset));
                }
                bitOffset++;
                if(bitOffset==8){
                    bitOffset =0;
                    byteIndex++;
                }
            }
        }
        byte[] data = new byte[length+2];
        data[0] = (byte)length;
        byte[] codeBytes = bits.toByteArray();
        for (int i = 1; i <=length ; i++) {
            data[i] = codeBytes[codeBytes.length-i];
        }
        data[length+1] = 0x00;
        return data;
    }
}
