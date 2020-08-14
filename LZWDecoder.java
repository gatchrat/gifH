import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LZWDecoder {
    public LZWDecoder() {

    }

    public ArrayList<Integer> Decode(int initCode, byte[] value, int tableLength) {
        // AB 5 Farben rip -> 2bit -> 3bit = rip
        int codeSize = initCode + 1;
        int resetCode = (int) Math.pow(2, initCode);
        int bits = value.length * 8;

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
        codeTable.get(codeTable.size() - 1).add(resetCode);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(endCode);
        ByteBuffer bb = ByteBuffer.wrap(value);
        BitSet dBits = BitSet.valueOf(bb);
        // init
        // read reset code
        int curCode = 0;
        System.out.println("CODES:");
        System.out.println("CodeSize:" + codeSize);
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
        System.out.println("reset:" + curCode);
        int prevCode = 0;
        prevCode = curCode;

        // read a single code
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
        // System.out.println("first code:" + curCode);
        codeList.add(new ArrayList<>(codeTable.get(curCode)));
        prevCode = curCode;

        // while code reading
        while (byteIndex < value.length - 1) {

            // read code
            curCode = 0;
            for (int i = 0; i < codeSize; i++) {
                boolean curBit = dBits.get(bits - (byteIndex * 8 + 8 - bitOffset));
                bitOffset++;
                if (bitOffset == 8) {
                    bitOffset = 0;
                    byteIndex++;
                    // System.out.println(byteIndex);
                }
                if (curBit) {
                    curCode += Math.pow(2, i);
                }
            }
            // handle code
            // System.out.println(curCode);
            if (curCode == resetCode) {
                System.out.println("Found Reset at " + byteIndex);
                System.out.println(prevCode);
                codeTable.clear();
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
                codeTable.get(codeTable.size() - 1).add(resetCode);
                codeTable.add(new ArrayList<Integer>());
                codeTable.get(codeTable.size() - 1).add(endCode);
                codeSize = initCode + 1;
                // read a single code
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
                System.out.println("first code:" + curCode);
                codeList.add(new ArrayList<>(codeTable.get(curCode)));
                prevCode = curCode;
                continue;

            }
            if (curCode != resetCode && curCode != endCode && codeTable.size() < 4096) {

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

                if (codeTable.size() - 1 == Math.pow(2, codeSize) - 1 && codeSize < 12) {
                    codeSize++;
                    if (codeSize == 13) {
                        System.out.println("Manual reset at" + byteIndex);
                        // codeSize =initCode+1;
                    }
                    System.out.println("CodeSize:" + codeSize + " at " + byteIndex);
                }
                prevCode = curCode;
                /*
                 * for (int i = 0; i < codeTable.size(); i++) { System.out.print(i+":"); for
                 * (int j = 0; j < codeTable.get(i).size(); j++) {
                 * System.out.print(codeTable.get(i).get(j)); System.out.print(','); }
                 * System.out.println(""); }
                 */

            }
            if (codeTable.size() == 4096) {
                System.out.println("full");
            }
            if (curCode == endCode) {
                System.out.println("Found Ending Code at byte " + byteIndex);
                System.out.println("Table size:" + codeTable.size());
                System.out.println("List fold size:" + codeList.size());
                byteIndex = 999999;
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