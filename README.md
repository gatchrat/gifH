# gifH
gifH is a simple Java-Library, which can be used to create .gif Files from single images, or extract images from .gif files.
It supports a big array of settings and can extract a multitude of information from a Gif.

## Example-Usage:
1. Combine the Files 1.png and 2.png in the input folder into test.gif. The Comment "Created with GifH" will be added and there will 100*10ms delay between the single images.
```java
GifH g = new GifH();
ArrayList<File> i = new ArrayList<>();
i.add(new File("input/1.png"));
i.add(new File("input/2.png"));
GIFSettings s = new GIFSettings();
s.delayTime = 100;
s.comment = "Created with GifH";
g.combineIntoGif(i,s,"test");
```
2. Extracts all images from the file test.gif in the generated folder and outputs the comment data from the gif.
```java
GifH g = new GifH();
File f = new File("generated/test.gif");
GIFSettings s = new GIFSettings();
try {
    s = g.extractFilesFromGif(Files.readAllBytes(Path.of(f.getPath())),s);
    System.out.println(s.comment);
} catch (IOException e) {
    e.printStackTrace();
}
```
3. GUI-Example
In the Example folder is a complete Example Projekt with a GUI through which Files can be choosen, combined, data read, settings set and images extracted.
## Warning:
This was created as a free time project. Performance is not really good and it isnt thoughrully testet, so you should probalby not use this library in any capacity.

## Known Bugs:
When opening a generated Gif in Gimp, it isnt properly recognized and only a single colored layer will be created. Opening/Viewing the file in other programms works fine though.