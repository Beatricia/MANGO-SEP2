package transferobjects;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class MyImage implements Serializable
{
  private int width;
  private int height;

  private int[] colors;

  public MyImage(BufferedImage image){
    width = image.getWidth(null);
    height = image.getHeight(null);

    colors = new int[width * height];

    int index = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int rgb = image.getRGB(i, j);
        colors[index++] = rgb;
      }
    }
  }

  public BufferedImage toImage(){
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    int index = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int rgb = colors[index++];
        image.setRGB(i, j, rgb);
      }
    }

    return image;
  }
}
