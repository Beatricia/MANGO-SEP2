package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import shared.Log;
import transferobjects.SerializableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tool class with useful helping method collection for working with images.
 */
public class ImageTools {
  /**
   * Convert a buffered image to fx image
   * @param image image to convert
   * @return converted javafx image
   */
  public static Image convertToFXImage(BufferedImage image) {
    return SwingFXUtils.toFXImage(image, null);
  }

  /**
   * Load an image from the local computer
   * @param name name of the file (from the IMAGE_FOLDER folder)
   * @return loaded image
   * @throws IOException if an error occurs during reading or when not able to create required ImageInputStream.
   */
  public static BufferedImage loadImageFromFile(String imageFolder, String name) throws IOException {
    File file = new File(imageFolder + name);
    return ImageIO.read(file);
  }

  /**
   * Save an image to a file. If the folder is not created, it is automatically created.
   * @param name name of the image with format
   * @param format format of the image
   * @param bufferedImage image to save to file
   * @throws IOException if an error occurs during writing or when not able to create required ImageOutputStream.
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void writeImageToFile(String imageFolder, String name, String format, BufferedImage bufferedImage)
      throws IOException {
    //check if the image folder exists (if not, then create it
    File directory = new File(imageFolder);
    if(!directory.exists())
      directory.mkdirs();

    File file = new File(imageFolder + name);
    if(file.exists())
      file.delete();

    ImageIO.write(bufferedImage, format, file);
  }

  /**
   * Resize a buffered image
   * @param image image to resize
   * @param width new width
   * @param height new height
   * @return resized image
   */
  public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
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
  public static BufferedImage toBufferedImage(java.awt.Image img)
  {
    if (img instanceof BufferedImage)
    {
      return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bImage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bImage;
  }

  /**
   * Delete an image file from the disk
   * @param imageFilePath image file path to delete
   */
  public static void deleteImage(String imageFilePath){
    File file = new File(imageFilePath);
    if(file.delete()){
      Log.log("ImageTools Image " + imageFilePath + " was deleted");
    }
    else {
      Log.log("ImageTools Could not delete image " + imageFilePath);
    }
  }

  public static String hashImage(BufferedImage image, String format){
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    byte[] data;
    MessageDigest md;
    try {
      ImageIO.write(image, format, outputStream);
      data = outputStream.toByteArray();
      md = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException | IOException e) {
      return null;
    }
    md.update(data);
    byte[] hash = md.digest();
    return returnHex(hash);
  }

  private static String returnHex(byte[] inBytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte inByte : inBytes) { //for loop ID:1
      hexString.append(Integer.toString((inByte & 0xff) + 0x100, 16).substring(1));
    }                                   // Belongs to for loop ID:1
    return hexString.toString();
  }
}