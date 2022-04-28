package client.view.register;

import client.model.UserModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.UserType;
import transferobjects.User;

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

    if (firstName.equals("") || lastName.equals("") || username.equals("")
        || password.equals("")) // checks for empty fields
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
