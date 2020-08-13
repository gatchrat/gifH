import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LZWDecoder {
    public LZWDecoder() {

    }

    public ArrayList<Integer> Decode(int initCode, byte[] value) {
        int index = 0;
        int codeSize = initCode + 1;
        int resetCode = (int) Math.pow(2, initCode);
        int endCode = (int) Math.pow(2, initCode) + 1;
        ArrayList<ArrayList<Integer>> codeTable = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> codeList = new ArrayList<ArrayList<Integer>>();
        // init table
        // indexes für colortable
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(0);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(1);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(2);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(3);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(resetCode);
        codeTable.add(new ArrayList<Integer>());
        codeTable.get(codeTable.size() - 1).add(69);

        int byteIndex = 0;
        int bitOffset = 0;
        BitSet dBits = BitSet.valueOf(ByteBuffer.wrap(value));
        // init
        // read reset code
        int curCode = 0;
        //System.out.println("CODES:");
        //System.out.println("CodeSize:" + codeSize);
        for (int i = 0; i < codeSize; i++) {
            boolean curBit = dBits.get(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
            // System.out.println(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
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
        //System.out.println("reset:" + curCode);
        // read a single code
        curCode = 0;

        for (int i = 0; i < codeSize; i++) {
            boolean curBit = dBits.get(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
            // System.out.println(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
            bitOffset++;
            if (bitOffset == 8) {
                bitOffset = 0;
                byteIndex++;
            }
            if (curBit) {
                curCode += Math.pow(2, i);
            }

        }
        if (curCode != resetCode) {
            codeList.add(codeTable.get(curCode));
        }

        prevCode = curCode;
        //System.out.println(curCode);
        // while code reading
        while (byteIndex < value.length) {

            // read code
            curCode = 0;
            for (int i = 0; i < codeSize; i++) {
                boolean curBit = dBits.get(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
                // System.out.println(dBits.length() - (byteIndex * 8 + 8 - bitOffset));
                bitOffset++;
                if (bitOffset == 8) {
                    bitOffset = 0;
                    byteIndex++;
                    /*
                     * if(curBit){ System.out.println(1); } else{ System.out.println(0); }
                     */
                }
                /*
                 * else{ if(curBit){ System.out.print(1); } else{ System.out.print(0); } }
                 */
                if (curBit) {
                    curCode += Math.pow(2, i);
                }
            }
            // handle code
            //System.out.println(curCode);
           //TODO ende is hier gehardcoded
            if (curCode != 5) {
               
                if (curCode >= codeTable.size()) {
                    int k = codeTable.get(prevCode).get(0);
                    codeList.add(new ArrayList<>(codeTable.get(prevCode)));
                    codeList.get(codeList.size() - 1).add(k);
                    codeTable.add(new ArrayList<>(codeTable.get(prevCode)));
                    codeTable.get(codeTable.size() - 1).add(k);
                } 
                else {
                    int k = codeTable.get(curCode).get(0);
                    //System.out.println("ai"+ curCode + "  " + k);
                    codeList.add(new ArrayList<>(codeTable.get(curCode)));
                    codeTable.add(new ArrayList<>(codeTable.get(prevCode)));
                    codeTable.get(codeTable.size() - 1).add(k);
                }

                if (codeTable.size() - 1 == Math.pow(2, codeSize) - 1) {
                    codeSize++;
                   // System.out.println("CodeSize:" + codeSize);
                }
                prevCode = curCode;
                /*for (int i = 0; i < codeTable.size(); i++) {
                    System.out.print(i+":");
                    for (int j = 0; j < codeTable.get(i).size(); j++) {
                        System.out.print(codeTable.get(i).get(j));
                        System.out.print(',');
                    }
                    System.out.println("");
                }*/
                
            }
            else{
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