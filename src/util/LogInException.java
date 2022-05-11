package util;

import shared.Log;

public class LogInException extends Exception
{
  /**
   * Class that is a custom-made exception for when there is an error during the logging process
   * (the password did not match when logging in and user does not exist)
   * @param message the message that is shown
   */
  public LogInException(String message)
  {
    super(message);
    Log.log("LogInException: Exception in Log-In hhh I dont think we need this one tho");
  }
}

