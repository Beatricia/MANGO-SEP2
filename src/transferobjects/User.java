package transferobjects;

import shared.UserType;

import java.io.Serializable;

/**
 * The class representing a user object  with username, userType, firstName, lastName.
 * @author Agata
 * @version 1
 */

public class User implements Serializable
{
  private String username;
  private UserType userType;
  private String firstName;
  private String lastName;

  /**
   * Construct the User object
   * @param username user's username
   * @param userType user' type
   * @param firstName user's first name
   * @param lastName user's last name
   */

  public User(String username,UserType userType,String firstName,String lastName)
  {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userType = userType;
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
   * Gets the firstName
   * @return the user's firstName
   */

  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Gets the lastName
   * @return the user's lastName
   */

  public String getLastName()
  {
    return lastName;
  }


  /**
   * Gets the userType
   * @return the user's type
   */

  public UserType getUserType()
  {
    return userType;
  }
}
