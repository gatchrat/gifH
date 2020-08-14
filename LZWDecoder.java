import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LZWDecoder {
    public LZWDecoder() {

    }

    public ArrayList<Integer> Decode(int initCode, byte[] value, int tableLength) {
        //AB 5 Farben rip -> 2bit -> 3bit = rip
        int codeSize = initCode + 1;
        int resetCode = (int) Math.pow(2, initCode);
        int bits =value.length*8; 

        int endCode = (int) Math.pow(2, initCode) + 1;
        ArrayList<ArrayList<Integer>> codeTable = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> codeList = new ArrayList<ArrayList<Integer>>();
        // init table
        // indexes für colortable
        // setupColorTable

        int byteIndex = 0;
        int bitOffset = 0;
        for (int i = 0; i < tableLength; i++) {
            codeTable.add(new ArrayList<Integer>());
            codeTable.get(codeTable.size() - 1).add(i);
        }
        // fill empty space
        while (codeTable.size() < resetCode) {
            codeTable.add(new ArrayList<Integer>());
        }
        // add 2 special values
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size()-1).add(resetCode);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size()-1).add(endCode);
        System.out.println("C " + codeTable.size());
        ByteBuffer bb = ByteBuffer.wrap(value);
          BitSet dBits = BitSet.valueOf(bb);
        // init
        // read reset code
        int curCode = 0;
         System.out.println("CODES:");
         System.out.println("CodeSize:" + codeSize);
        for (int i = 0; i < codeSize; i++) {
            boolean curBit = dBits.get(bits -(byteIndex * 8 + 8 - bitOffset));
            bitOffset++;
            if (bitOffset == 8) {
                bitOffset = 0;
                byteIndex++;
            }
            if (curBit) {
                curCode += Math.pow(2, i);
            }

        }
        //System.out.println("reset:" + curCode);
        int prevCode = 0;
        prevCode = curCode;
        
        // read a single code
        curCode = 0;

        for (int i = 0; i < codeSize; i++) {
            boolean curBit = dBits.get(bits - (byteIndex * 8 + 8 - bitOffset));
            // System.out.println(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
            System.out.println(curBit);
            bitOffset++;
            if (bitOffset == 8) {
                bitOffset = 0;
                byteIndex++;
            }
            if (curBit) {
                curCode += Math.pow(2, i);
            }

        }
        System.out.println("first code:" + curCode);
        if (curCode != resetCode) {
            codeList.add(codeTable.get(curCode));
        }

        prevCode = curCode;
        
        // while code reading
        while (byteIndex < value.length) {

            // read code
            curCode = 0;
            for (int i = 0; i < codeSize; i++) {
                boolean curBit = dBits.get(bits - (byteIndex * 8 + 8 - bitOffset));
                bitOffset++;
                if (bitOffset == 8) {
                    bitOffset = 0;
                    byteIndex++;
                }
                if (curBit) {
                    curCode += Math.pow(2, i);
                }
            }
            // handle code
            System.out.println(curCode);
            if (curCode == resetCode) {
                System.out.println("Found Reset");
                byteIndex = 0;
                bitOffset = 0;
                codeTable.clear();
                for (int i = 0; i < value.length; i++) {
                    codeTable.add(new ArrayList<Integer>());
                    codeTable.get(codeTable.size() - 1).add(i);
                }
                codeTable.set(resetCode, new ArrayList<Integer>());
                codeTable.get(resetCode).add(resetCode);
                codeTable.set(endCode, new ArrayList<Integer>());
                codeTable.get(endCode).add(endCode);
            }
            if (curCode != resetCode && curCode != endCode) {

                if (curCode >= codeTable.size()) {
                    int k = codeTable.get(prevCode).get(0);
                    codeList.add(new ArrayList<>(codeTable.get(prevCode)));
                    codeList.get(codeList.size() - 1).add(k);
                    codeTable.add(new ArrayList<>(codeTable.get(prevCode)));
                    codeTable.get(codeTable.size() - 1).add(k);
                } else {
                    int k = codeTable.get(curCode).get(0);
                    codeList.add(new ArrayList<>(codeTable.get(curCode)));
                    codeTable.add(new ArrayList<>(codeTable.get(prevCode)));
                    codeTable.get(codeTable.size() - 1).add(k);
                }

                if (codeTable.size() - 1 == Math.pow(2, codeSize) - 1) {
                    codeSize++;
                    // System.out.println("CodeSize:" + codeSize);
                }
                prevCode = curCode;
                /*
                 * for (int i = 0; i < codeTable.size(); i++) { System.out.print(i+":"); for
                 * (int j = 0; j < codeTable.get(i).size(); j++) {
                 * System.out.print(codeTable.get(i).get(j)); System.out.print(','); }
                 * System.out.println(""); }
                 */

            } else {
                System.out.println("Ending");
                byteIndex = 99999;
            }
        }
        ArrayList<Integer> finalList = new ArrayList<Integer>();
        for (int i = 0; i < codeList.size(); i++) {
            for (int j = 0; j < codeList.get(i).size(); j++) {
                finalList.add(codeList.get(i).get(j));
            }
        }

        return finalList;
    }
}