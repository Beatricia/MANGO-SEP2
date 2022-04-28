package client.view.Login;

import client.model.UserModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.UserType;
import transferobjects.User;

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
      error.setValue("Empty field");
    }
    else
    {
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
