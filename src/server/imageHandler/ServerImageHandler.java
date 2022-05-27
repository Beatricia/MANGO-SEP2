package server.imageHandler;

import util.ImageTools;
import shared.Log;
import transferobjects.SerializableImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ServerImageHandler
{
  private static final String IMAGE_FOLDER = "Resources/server/menuImages/";// base path to the server's image folder


  public static String saveImage(SerializableImage image){
    Log.log("ServerImageHandler Saving image to file");

    BufferedImage bufferedImage = image.toImage();
    bufferedImage = ImageTools.toBufferedImage(bufferedImage);

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

  public static void deleteImage(String imageFileName){
    ImageTools.deleteImage(IMAGE_FOLDER + imageFileName);
  }
}

//1642595afd2d174d4ecc9d7658775922
//1642595afd2d174d4ecc9d7658775922
