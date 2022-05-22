package transferobjects;

import java.io.Serializable;
//TODO javadocs
public class ImageRequest implements Serializable
{
  private final String path;
  private SerializableImage serializableImage;

  public ImageRequest(String path){
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public SerializableImage getSerializableImage(){
    return serializableImage;
  }

  public void setSerializableImage(SerializableImage serializableImage){
    this.serializableImage = serializableImage;
  }
}
