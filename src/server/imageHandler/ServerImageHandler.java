package server.imageHandler;

import util.ImageTools;
import shared.Log;
import transferobjects.SerializableImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Handling images on the server side, such as saving, deleting and reading images from the server
 * folder.
 *
 * @author Greg
 * @version 1
 */
public class ServerImageHandler
{
  private static final String IMAGE_FOLDER = "Resources/server/menuImages/";// base path to the server's image folder

  /**
   * Hashes the image to create a unique name and saves the image to the server folder by
   * its unique hash code.
   * @param image image to be saved
   * @return the file name the image was saved
   */
  public static String saveImage(SerializableImage image){
    Log.log("ServerImageHandler Saving image to file");

    BufferedImage bufferedImage = image.toImage();

    String format = image.getFormat();
    String imageName = null;
    try{
      imageName = ImageTools.hashImage(bufferedImage, format) + "." + format;
      ImageTools.writeImageToFile(IMAGE_FOLDER, imageName, format, bufferedImage);
    } catch (IOException e){
      Log.log("ServerImageHandler Failed to save image: " + e.getMessage());
    }

    return imageName;
  }

  /**
   * Gets an image from file, packed in a SerializableImage object.
   * @param imageFileName image to get from the file
   * @return a SerializableImage object containing the loaded image
   */
  public static SerializableImage getImage(String imageFileName){
    try{

      BufferedImage bufferedImage = ImageTools.loadImageFromFile(IMAGE_FOLDER, imageFileName);
      String format = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);
      return new SerializableImage(bufferedImage, format);

    } catch (IOException e) {
      Log.log("Failed to load image: " + e.getMessage());
    }

    return null;
  }

  /**
   * Deletes an image from the server folder
   * @param imageFileName image to delete
   */
  public static void deleteImage(String imageFileName){
    ImageTools.deleteImage(IMAGE_FOLDER + imageFileName);
  }
}

//1642595afd2d174d4ecc9d7658775922
//1642595afd2d174d4ecc9d7658775922
