package server.databaseConn;

import shared.UserType;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;

/**
 * Abstract Model for accessing the Database.
 */
public interface DatabaseConn
{
  /**
   * Log in with the specified username and the password.
   * @param username username
   * @param password password
   * @return A user object representing the logged-in user if the log-in was successful
   * @throws SQLException When an unexpected sql exception happens
   * @throws LogInException When the user has not provided the correct data
   */
  User login(String username, String password) throws SQLException,
      LogInException;

  /**
   * Register a user with the specified username, password, first name and last name.
   * @param firstName first name
   * @param lastName last name
   * @param username username
   * @param password password
   * @param userType type of the user
   * @return A user object representing the registered user if the register was successful
   * @throws SQLException When the user has not provided the correct data
   */
  User register(String firstName, String lastName, String username, String password, UserType userType) throws
      SQLException;
}
