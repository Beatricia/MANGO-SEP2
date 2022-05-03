package transferobjects;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Class to transfer images over the network with sockets. It has an extremely low noise, it is not
 * noticeable though.
 *
 * @author Gergo
 * @version 1
 */
public class SerializableImage implements Serializable
{
  private int width; //width of the image
  private int height; //height of the image

  private int[] colors; // each pixel color stored

  /**
   * Initializes a SerializableImage, and converts the BufferedImage object into the right
   * format, so that it can be sent by sockets
   * @param image the image object to convert
   */
  public SerializableImage(BufferedImage image){
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

  /**
   * Retrieves the converted image by converting it back to BufferedImage
   * @return the BufferedImage given in the constructor
   */
  public BufferedImage toImage(){
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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
