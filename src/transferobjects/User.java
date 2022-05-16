package transferobjects;

import shared.Log;
import shared.UserType;

import java.io.Serializable;
import java.util.Objects;

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

    Log.log("User transferobject created");
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


  public String toString(){
    return String.format("%s %s (%s)", firstName, lastName, username);
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return Objects.equals(username, user.username) && userType == user.userType
        && Objects.equals(firstName, user.firstName) && Objects.equals(lastName,
        user.lastName);
  }

}
