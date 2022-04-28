package client.view.register;

import client.model.UserModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.UserType;
import transferobjects.ErrorMessage;
import transferobjects.User;

import java.beans.PropertyChangeEvent;

/**
 * Class responsible for connecting the RegisterViewController and UserModel interface.
 * @author Simon
 * @version 1
 */
public class RegisterViewModel
{
  private UserModel userModel;
  private StringProperty errorMessage;

  /**
   * Constructor called when new instance created, initializes userModel and errorMessage
   * @param userModel model providing logic on the client side
   */
  public RegisterViewModel(UserModel userModel)
  {
    this.userModel = userModel;
    errorMessage = new SimpleStringProperty("");

    userModel.addListener(UserModel.LOGGED_IN_RECEIVED, this::loggedInReceived);
    userModel.addListener(UserModel.ERROR_RECEIVED, this::errorReceived);
  }

  private void errorReceived(PropertyChangeEvent event) {
    ErrorMessage errorMess = (ErrorMessage) event.getNewValue();

    printErrorMessage(errorMess.getMessage());
  }

  private void printErrorMessage(String message){
    Platform.runLater(() -> errorMessage.setValue("Error: " + message));
  }

  private void loggedInReceived(PropertyChangeEvent event) {
    System.out.println("Login received");
  }

  /**
   * Checks for empty fields and matching passwords, if correct sends a request for creating a new User object
   * @param firstName first name of the user
   * @param lastName last name of the user
   * @param username username of the user
   * @param password password of the user
   * @param passwordRepeat to check the password
   * @param userType type of the user
   * @return User object
   */
  public User register(String firstName, String lastName, String username,
      String password, String passwordRepeat, UserType userType)
  {

    if (firstName.isBlank() || lastName.isBlank() || username.isBlank()
        || password.isBlank()) // checks for empty fields
    {
      errorMessage.setValue("Empty field");
    }
    else if (!password.equals(
        passwordRepeat))  //checks if the password and passwordRepeat do match
    {
      errorMessage.setValue("Passwords do not match");
    }
    else // everything is correct
    {
      errorMessage.setValue("");

      userModel.register(firstName, lastName, username, password, userType);

      return new User(username, userType, firstName, lastName);
    }

    return null;
  }

  /**
   * returns bound StringProperty, which will be automatically updated in the RegisterViewController and GUI
   * @return StringProperty bound with the RegisterViewController
   */
  public StringProperty getErrorMessage()
  {
    return errorMessage;
  }
}
