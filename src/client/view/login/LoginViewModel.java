package client.view.login;

import client.model.UserModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.Log;
import transferobjects.ErrorMessage;
import transferobjects.User;

import java.beans.PropertyChangeEvent;

/**
 * The class which providing methods for LoginViewModel.
 * @author Agata
 * @version 1
 */

public class LoginViewModel
{
  private UserModel userModel;
  private StringProperty username;
  private StringProperty password;
  private StringProperty error;

  /**
   * Construct the LoginViewModel object, initial base variable.
   * @param userModel User model to forward data to.
   */

  public LoginViewModel(UserModel userModel)
  {
    this.userModel = userModel;
    username = new SimpleStringProperty();
    password = new SimpleStringProperty();
    error = new SimpleStringProperty();

    userModel.addListener(UserModel.LOGGED_IN_RECEIVED, this::loggedInReceived);
    userModel.addListener(UserModel.ERROR_RECEIVED, this::errorReceived);
  }

  private void loggedInReceived(PropertyChangeEvent propertyChangeEvent)
  {
    User user = (User) propertyChangeEvent.getNewValue();
    Log.log("LoginViewModel received an user object from the model");
    System.out.println("Message from LoginViewModel: "+user);
  }

  private void errorReceived(PropertyChangeEvent propertyChangeEvent)
  {
    ErrorMessage errorMes =(ErrorMessage) propertyChangeEvent.getNewValue();
    Log.log("LoginViewModel received an error message object from the model");
    displayError(errorMes.getMessage());
  }

  private void displayError(String message){
    Log.log("LoginViewModel the error from the model is displayed");
    Platform.runLater(() -> error.setValue(message));
  }

  /**
   * call the login method from UserModel class
   * @param username user's username
   * @param password user's password
   */

  public void login(String username, String password)
  {
    if (username.equals("") || password.equals(""))
    {
      Log.log("LoginViewModel the user did not complete the username and password fields");
      displayError("Empty field");
    }
    else if (password.length() <= 8) // checks for password's length
    {
      Log.log("LoginViewModel the user wrote a password with 9<characters");
      displayError("Password must contain minimum of 9 characters");
    }
    else
    {
      Log.log("LoginViewModel the new login object is sent to the userModel");
      error.setValue("");
      userModel.login(username,password);
    }
  }

  /**
   * Gets the username StringProperty
   * @return username StringProperty
   */

  public StringProperty getUsername()
  {
    return username;
  }

  /**
   * Gets the password StringProperty
   * @return password StringProperty
   */

  public StringProperty getPassword()
  {
    return password;
  }

  /**
   * Gets the error StringProperty
   * @return error StringProperty
   */

  public StringProperty getError()
  {
    return error;
  }
}
