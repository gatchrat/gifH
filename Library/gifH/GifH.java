package gifH;
public class GifH {
    public static void main(String[] args) {
    }
    public void extractFilesFromGif(byte[] gif){
        GifExtractor e = new GifExtractor(gif);
    }
}
