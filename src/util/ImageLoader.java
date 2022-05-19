package util;

import client.networking.Client;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import transferobjects.ImageRequest;
import transferobjects.SerializableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageLoader
{
  private static final String IMAGE_FOLDER = "Resources/customer/menuImages/";
  private static ImageLoader imageLoader;
  public static void loadImage(String imageFileName, ImageView imageView) {
    loadImage(imageFileName, imageView, -1, -1);
  }
  public static void loadImage(String imageFileName, ImageView imageView, int width, int height) {
    imageLoader.loadImageFromFile(imageFileName, imageView, width, height);
  }


  private final Client client;
  private final HashMap<String, List<PendingImageRequest>> imagesOnWait;

  public ImageLoader(Client client) {
    this.client = client;
    imagesOnWait = new HashMap<>();

    if(imageLoader != null)
      return;

    ImageLoader.imageLoader = this;
    client.addListener(Client.IMAGE_RECEIVED, this::imageReceived);
  }


  private void loadImageFromFile(String imagePath, ImageView imageView, int width, int height){
    try{
      BufferedImage bufferedImage = ImageTools.loadImageFromFile(imagePath);
      loadImageIntoView(bufferedImage, imageView, width, height);

    } catch (IOException e){
      PendingImageRequest pendingImageRequest = new PendingImageRequest(width, height, imageView);
      requestImage(imagePath, pendingImageRequest);
    }
  }

  private void loadImageIntoView(BufferedImage bufferedImage, ImageView imageView, int width,
      int height) {

    if (width > 0 && height > 0) {
      bufferedImage = ImageTools.resizeImage(bufferedImage, width, height);
    }

    Image fxImage = ImageTools.convertToFXImage(bufferedImage);
    imageView.setImage(fxImage);
  }


  private void requestImage(String imagePath, PendingImageRequest pendingImageRequest){
    ImageRequest imageRequest = new ImageRequest(imagePath);

    synchronized (this){
      if(!imagesOnWait.containsKey(imagePath)) {
        client.sendImageRequest(imageRequest);
        imagesOnWait.put(imagePath, new ArrayList<>());
      }
      imagesOnWait.get(imagePath).add(pendingImageRequest);
    }
  }

  private void imageReceived(PropertyChangeEvent propertyChangeEvent) {
    ImageRequest imageRequest = (ImageRequest) propertyChangeEvent.getNewValue();

    String path = imageRequest.getPath();
    SerializableImage serializableImage = imageRequest.getSerializableImage();
    BufferedImage bufferedImage = serializableImage.toImage();

    try {
      ImageIO.write(bufferedImage, serializableImage.getFormat(), new File(IMAGE_FOLDER + path));
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    List<PendingImageRequest> pendingImageRequests;
    synchronized (this){
      pendingImageRequests = imagesOnWait.remove(path);
    }

    for (PendingImageRequest pendingImageRequest : pendingImageRequests){
      ImageView imageView = pendingImageRequest.imageView;
      int width = pendingImageRequest.width;
      int height = pendingImageRequest.height;

      loadImageIntoView(bufferedImage, imageView, width, height);
    }
  }





  private static class PendingImageRequest {
    private final int width;
    private final int height;
    private final ImageView imageView;

    public PendingImageRequest(int width, int height, ImageView imageView) {
      this.width = width;
      this.height = height;
      this.imageView = imageView;
    }
  }

  private static class ImageTools {
    public static Image convertToFXImage(BufferedImage image) {
      return SwingFXUtils.toFXImage(image, null);
    }

    public static BufferedImage loadImageFromFile(String name) throws IOException {
      File file = new File(IMAGE_FOLDER + name);
      return ImageIO.read(file);
    }

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
  }

}
