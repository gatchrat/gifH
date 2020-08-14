 public  class Util {
    public static byte[] reverseByteArray(byte[] arr){
        byte[] ret = new byte[arr.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[arr.length-1-i];
        }
        return ret;
    }
}