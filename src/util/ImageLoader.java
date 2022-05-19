package util;

import client.networking.Client;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader
{
  private static ImageLoader imageLoader;
  public static void loadImage(String imagePath, ImageView imageView) {
    loadImage(imagePath, imageView);
  }


  private final Client client;

  public ImageLoader(Client client) {
    this.client = client;

    if(imageLoader != null)
      return;

    ImageLoader.imageLoader = this;
  }

  private void loadImageIntoView(String imagePath, ImageView imageView){

  }







  static class ImageTools {
    private static Image convertToFXImage(BufferedImage image) {
      return SwingFXUtils.toFXImage(image, null);
    }

    private static BufferedImage loadImageFromFile(String path) {
      BufferedImage image = null;

      try{
        image = ImageIO.read(new File(path));
      } catch (IOException ignored) {
      }

      return image;
    }

    private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
      //resize image
      java.awt.Image resizedImage = image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);

      return toBufferedImage(resizedImage);
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private static BufferedImage toBufferedImage(java.awt.Image img)
    {
      if (img instanceof BufferedImage)
      {
        return (BufferedImage) img;
      }

      // Create a buffered image with transparency
      BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

      // Draw the image on to the buffered image
      Graphics2D bGr = bimage.createGraphics();
      bGr.drawImage(img, 0, 0, null);
      bGr.dispose();

      // Return the buffered image
      return bimage;
    }
  }

}
