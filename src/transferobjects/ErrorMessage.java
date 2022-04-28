package transferobjects;

import java.io.Serializable;

/**
 * The class representing a ErrorMessage object to transport a error message.
 * @author Grego
 * @version 1
 */

public class ErrorMessage implements Serializable
{
  private final String message;

  /**
   * Construct the ErrorMessage object
   * @param message message value
   */

  public ErrorMessage(String message){
    this.message = message;
  }

  /**
   * Gets the message
   * @return the value of message
   */

  public String getMessage(){
    return message;
  }



  public String toString()
  {
    return message;
  }
}
