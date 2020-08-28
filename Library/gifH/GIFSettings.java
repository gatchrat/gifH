package gifH;

import java.util.ArrayList;

public class GIFSettings {
    //Settings with most standard settings set
    public String version = "89a";
    public int colorResolution = 7;
    public String comment = "";
    public String application = "";
    public String applicationData = "";

    //width and height of the finished GIF
    public int width;
    public int height;

    public int disposalMethod = 0;
    public int delayTime = 10;
    public  boolean hasTransparency = false;
    public boolean isInterlaced = false;
    public int transparencyIndex;
    public int backgroundIndex = 0;
    //For each image
    public int leftPos = 0;
    public int  topPos =0;
    public GIFSettings(){

    }
}
