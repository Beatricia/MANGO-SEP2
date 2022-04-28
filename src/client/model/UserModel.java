package client.model;

import shared.UserType;
import util.PropertyChangeSubject;

/**
 * An interface which is responsible for handling the connection between the
 * ViewModels and the Networking
 */
public interface UserModel extends PropertyChangeSubject
{
  String ERROR_RECEIVED = "ErrorReceived";
  String LOGGED_IN_RECEIVED = "LogInReceived";

  /**
   * The method is used to send to the Client String objects, that are required
   * for a log in
   * @param username the username that the user has provided
   * @param password the password that the user has provided
   */
  void login(String username, String password);

  /**
   * The method is used to send to the Client String objects and an Usertype,
   * which are all needed for a register action
   * @param firstName the first name that the user has provided
   * @param lastName the last name that the user has provided
   * @param username the username that the user has provided
   * @param password the password that the user has provided
   * @param userType the userType that the user has selected
   */
  void register(String firstName, String lastName, String username, String password, UserType userType);
}
