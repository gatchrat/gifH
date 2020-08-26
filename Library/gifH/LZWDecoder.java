package gifH;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LZWDecoder {
    public LZWDecoder() {

    }

    public ArrayList<Integer> Decode(int initCode, byte[] value, int tableLength) {
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
            if (curCode == resetCode) {
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

                curCode = 0;
                // read reset
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
                curCode =0;
                // read a single code
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
                }
                prevCode = curCode;

            }
            if (curCode == endCode) {
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