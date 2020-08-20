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
}