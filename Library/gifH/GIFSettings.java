package gifH;

import java.util.ArrayList;

public class GIFSettings {
    //Settings with most standard settings set
    public String version = "89a";
    public int colorResolution = 7;
    public String comment = "";
    public String applicationData = "";

    //width and height of the finished GIF
    public int width;
    public int height;

    public int disposalMethod = 0;
    public int delayTime = 10;
    //For each image
    public ArrayList<Integer> leftPos = new ArrayList<Integer>();
    public ArrayList<Integer> rightPos = new ArrayList<Integer>();
    public GIFSettings(){

    }
}
