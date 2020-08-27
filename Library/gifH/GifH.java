package gifH;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class GifH {
    public static void main(String[] args) {

    }

    public GIFSettings extractFilesFromGif(byte[] gif,GIFSettings s) {
        GifExtractor e = new GifExtractor(gif, s);
        return  e.importetSettings;
    }

    public void combineIntoGif(ArrayList<File> i, GIFSettings s, String n) {
        GifCreator g = new GifCreator(i,s,n);
    }
}
