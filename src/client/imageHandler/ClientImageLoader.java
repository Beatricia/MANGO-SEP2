package client.imageHandler;

import client.networking.Client;
import util.ImageTools;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import transferobjects.ImageRequest;
import transferobjects.SerializableImage;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ImageLoader is a class to dynamically load image into an ImageView object. It checks if the
 * image is downloaded to the customer's computer, and if yes, the loads it into the imageview.
 * If it is not downloaded, it sends an ImageRequest object to the server, which sends the requested
 * image back. This class then saves that image to the local computer, and finishes loading the image.
 *
 * @author Greg
 * @version 1
 */
public class ClientImageLoader
{

  private static final String IMAGE_FOLDER = "Resources/customer/menuImages/";// base path to the customer's image folder
  private static ClientImageLoader imageLoader;
  public static void setImageLoader(Client client){
    if(imageLoader != null)
      return;

    imageLoader = new ClientImageLoader(client);
  }


  /**
   * Loads the specified image with the original size into the image view
   * @param imageFileName image file name and extension to load
   * @param imageView image view to load into the image
   * @param width width of the image (if width < 0, both width and height parameter is ignored)
   * @param height height of the image (if height < 0, both width and height parameter is ignored)
   */
  public static void loadImage(String imageFileName, ImageView imageView, int width, int height) {
    imageLoader.loadImageFromFile(imageFileName, imageView, width, height);
  }





  private final Client client; // client to send the image request to the server
  private final HashMap<String, List<PendingImageRequest>> imagesOnWait; // waiting image views to load the images into

  /**
   * Constructs and ImageLoader instance, and assigns it to the static field imageLoader if and only
   * if the imageLoader is null
   * @param client client to send the image request to the server
   */
  private ClientImageLoader(Client client) {
    this.client = client;
    imagesOnWait = new HashMap<>();

    ClientImageLoader.imageLoader = this; // set the current ImageLoader instance to the static field
    client.addListener(Client.IMAGE_RECEIVED, this::imageReceived); // add listener on the client image received
  }

  /**
   * Loads image from the file. If the image is not downloaded, it puts the ImageView into the waiting list,
   * and sends the image request to the server.
   * @param imageFileName image file to load
   * @param imageView image view to put into the loaded image
   * @param width width of the image (if width < 0, both width and height parameter is ignored)
   * @param height height of the image (if height < 0, both width and height parameter is ignored)
   */
  private void loadImageFromFile(String imageFileName, ImageView imageView, int width, int height){
    try{
      BufferedImage bufferedImage = ImageTools.loadImageFromFile(IMAGE_FOLDER, imageFileName);
      loadImageIntoView(bufferedImage, imageView, width, height);

    } catch (IOException e){
      PendingImageRequest pendingImageRequest = new PendingImageRequest(width, height, imageView);
      requestImage(imageFileName, pendingImageRequest);
    }
  }

  /**
   * Loads an already imported buffered image to the image view.
   * @param bufferedImage buffered image to load
   * @param imageView image view to put into the loaded image
   * @param width width of the image (if width < 0, both width and height parameter is ignored)
   * @param height height of the image (if height < 0, both width and height parameter is ignored)
   */
  private void loadImageIntoView(BufferedImage bufferedImage, ImageView imageView, int width,
      int height) {

    if (width > 0 && height > 0) {
      bufferedImage = ImageTools.resizeImage(bufferedImage, width, height);
    }

    Image fxImage = ImageTools.convertToFXImage(bufferedImage);
    imageView.setImage(fxImage);
  }

  /**
   * Request an image from the server, and put the local image load into the waiting list
   * @param imageFileName image file name to request from the server
   * @param pendingImageRequest pending request to put into the waiting list
   */
  private void requestImage(String imageFileName, PendingImageRequest pendingImageRequest){
    ImageRequest imageRequest = new ImageRequest(imageFileName);

    synchronized (this){
      if(!imagesOnWait.containsKey(imageFileName)) {
        client.sendImageRequest(imageRequest);
        imagesOnWait.put(imageFileName, new ArrayList<>());
      }
      imagesOnWait.get(imageFileName).add(pendingImageRequest);
    }
  }

  /**
   * On image received from the server, save the received image to the file for later use,
   * and load the image into the waiting image views.
   * @param propertyChangeEvent event data
   */
  private void imageReceived(PropertyChangeEvent propertyChangeEvent) {
    ImageRequest imageRequest = (ImageRequest) propertyChangeEvent.getNewValue();

    // unpack the image request
    String imageFileName = imageRequest.getPath();
    SerializableImage serializableImage = imageRequest.getSerializableImage();
     BufferedImage bufferedImage = serializableImage.toImage();

    // try to save the image to file
    try {
      ImageTools.writeImageToFile(IMAGE_FOLDER, imageFileName, serializableImage.getFormat(), bufferedImage);
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    // get all the pending image request for this specific image
    List<PendingImageRequest> pendingImageRequests;
    synchronized (this){
      pendingImageRequests = imagesOnWait.remove(imageFileName);
    }

    if(pendingImageRequests == null)
      return;

    // load the image into the image views
    for (PendingImageRequest pendingImageRequest : pendingImageRequests){
      ImageView imageView = pendingImageRequest.imageView;
      int width = pendingImageRequest.width;
      int height = pendingImageRequest.height;

      loadImageIntoView(bufferedImage, imageView, width, height);
    }
  }



  // Private helper class


  /**
   * Pending image request information. This class is just for storing the request data, so it can be used
   * later on when the image has been received from the server.
   */
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
}
