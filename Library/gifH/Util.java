package gifH;

public  class Util {
    public static byte[] reverseByteArray(byte[] arr){
        byte[] ret = new byte[arr.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[arr.length-1-i];
        }
        return ret;
    }
    public static byte[] intTo2Byte(int n){
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xFF);
        b[1] = (byte) ((n >> 8) & 0xFF);
        return b;
    }
    public static double log2(int N)
    {
        double result = Math.log(N) / Math.log(2);
        return result;
    }
    //(6,3) = [false,true,true] = 011 = 110
    public static boolean[] intToBit(int n, int BitSize){
        boolean[] bits = new boolean[BitSize];
        for (int i = BitSize-1; i >= 0; i--) {
            if (n >= Math.pow(2,i)) {
                bits[i] = true;
                n-=Math.pow(2,i);
            }
            else{
                bits[i] = false;
            }
        }
        return bits;
    }
}