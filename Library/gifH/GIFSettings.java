package gifH;

import java.util.ArrayList;

public class GIFSettings {
    /**
     * Used Gif Version
     */
    public String version = "89a";
    /**
     *  Bits-1 used for color
     */
    public int colorResolution = 7;
    /**
     *  Saved Comment
     */
    public String comment = "";
    /**
     * Application name
     */
    public String application = "";
    /**
     * Application text data
     */
    public String applicationData = "";

    /**
     * Width of the Gif
     */
    public int width;
    /**
     * Height of the Gif
     */
    public int height;
    /**
     * Disposal Method
     */
    public int disposalMethod = 0;
    /**
     * Delay in 10ms between images
     */
    public int delayTime = 10;
    /**
     * If there are any transparent pixels
     */
    public  boolean hasTransparency = false;
    /**
     * If the image is interlaced
     */
    public boolean isInterlaced = false;
    /**
     * The Color index of Transparency
     */
    public int transparencyIndex;
    /**
     * The Color index of the Background
     */
    public int backgroundIndex = 0;
    /**
     *  Offset from the left side
     */
    public int leftPos = 0;
    /**
     * Offset from the top
     */
    public int  topPos =0;
    public GIFSettings(){

    }
}
