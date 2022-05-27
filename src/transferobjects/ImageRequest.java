package transferobjects;

import java.io.Serializable;

/**
 * Class ImageRequest with a path and a SerializableImage
 *
 * @author Greg
 * @version 1
 */
public class ImageRequest implements Serializable
{

  private final String path;
  private SerializableImage serializableImage;

  /**
   * Initializing the image with a path
   * @param path the image's path
   */
  public ImageRequest(String path){
    this.path = path;
  }

  /**
   * Getting the image path
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Getting the serializable image
   * @return the image
   */
  public SerializableImage getSerializableImage(){
    return serializableImage;
  }

  /**
   * Setting the serializable image
   * @param serializableImage the image to be set
   */
  public void setSerializableImage(SerializableImage serializableImage){
    this.serializableImage = serializableImage;
  }
}
