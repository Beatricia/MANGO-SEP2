package server.databaseConn;

import shared.UserType;
import transferobjects.MenuItem;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Abstract Model for accessing the Database.
 */
public interface DatabaseConn
{
  /**
   * Log in with the specified username and the password.
   *
   * @param username username
   * @param password password
   * @return A user object representing the logged-in user if the log-in was successful
   * @throws SQLException   When an unexpected sql exception happens
   * @throws LogInException When the user has not provided the correct data
   */
  User login(String username, String password)
      throws SQLException, LogInException;

  /**
   * Register a user with the specified username, password, first name and last name.
   *
   * @param firstName first name
   * @param lastName  last name
   * @param username  username
   * @param password  password
   * @param userType  type of the user
   * @return A user object representing the registered user if the register was successful
   * @throws SQLException When the user has not provided the correct data
   */
  User register(String firstName, String lastName, String username,
      String password, UserType userType) throws SQLException, LogInException;

  /**
   * Adds a Menu item to the database with the ingredients
   * @param name menu item name
   * @param ingredients ingredients
   * @param price price of the menu item
   * @param imgPath img path for the menu
   * @throws SQLException When the menu item name already exists.
   */
  void addItem(String name, ArrayList<String> ingredients, double price, String imgPath)
      throws SQLException;
}
