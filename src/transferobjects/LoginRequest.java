package transferobjects;

import shared.UserType;

import java.io.Serializable;

/**
 * The class representing a LoginRequest object with firstName, lastName, username, password, userType, isRegister.
 * @author Grego
 * @version 1
 */

public class LoginRequest implements Serializable
{
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private UserType userType;
  private boolean isRegister;

  /**
   * 2-argument Construct the LoginRequest object
   * needed to log in
   * @param username user's username
   * @param password user's password
   */

  public LoginRequest(String username, String password)
  {
    this.username = username;
    this.password = password;
    isRegister = false;
  }

  /**
   * 5-argument Construct the LoginRequest object
   * needed to register
   * @param firstName user's first name
   * @param lastName user's last name
   * @param username user's username
   * @param password user's password
   * @param userType user's type
   */

  public LoginRequest(String firstName, String lastName, String username, String password, UserType userType)
  {
    this.password=password;
    this.lastName = lastName;
    this.firstName = firstName;
    this.userType = userType;
    this.username = username;
    isRegister = true;
  }

  /**
   * Gets the first name
   * @return the user's firstName
   */

  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Gets the lastName
   * @return the user's last name
   */

  public String getLastName()
  {
    return lastName;
  }

  /**
   * Gets the username
   * @return the user's username
   */

  public String getUsername()
  {
    return username;
  }

  /**
   * Gets the password
   * @return the user's password
   */

  public String getPassword()
  {
    return password;
  }

  /**
   * Gets the userType
   * @return the user's type
   */

  public UserType getUserType()
  {
    return userType;
  }

  /**
   * gets the isRegister
   * @return the boolean expression weather the user was already registered
   */

  public boolean getIsRegister()
  {
    return isRegister;
  }
}
