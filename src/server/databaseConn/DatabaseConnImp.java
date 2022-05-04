package server.databaseConn;

import shared.UserType;
import transferobjects.MenuItem;
import transferobjects.User;
import util.LogInException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * A concrete implementation of the {@link DatabaseConn}.
 *
 * @author Mango
 * @version 1
 */
public class DatabaseConnImp implements DatabaseConn
{

  /**
   * Password to the SQL Server.
   */
  private static String password;

  /**
   * Returns the password if it is loaded, else it loads the password from the file.
   *
   * @return The password to access the database.
   */
  private static String getPass() {
    if (password != null)
      return password;

    File file = new File("Resources/DataBase_password.txt");
    try {
      Scanner scanner = new Scanner(file);
      password = scanner.nextLine();
      System.out.println(password);
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return password;
  }

  /**
   * Get database connection for the sql server.
   *
   * @return A connection object to access the database.
   * @throws SQLException When an unexpected exception happens.
   */
  static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=caneat", "postgres", getPass());

  }

  private UserDatabaseConn userDatabaseConn;
  private MenuDatabaseConn menuDatabaseConn;

  public DatabaseConnImp() {
    userDatabaseConn = new UserDatabaseConn();
  }

  /**
   * Log in with the specified username and the password.
   *
   * @param username username
   * @param password password
   * @return A user object representing the logged-in user if the log-in was successful
   * @throws SQLException   When an unexpected sql exception happens
   * @throws LogInException When the user has not provided the correct data
   */
  @Override public User login(String username, String password)
      throws LogInException, SQLException {
    return userDatabaseConn.login(username, password);
  }

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
  @Override public User register(String firstName, String lastName, String username,
      String password, UserType userType) throws SQLException, LogInException {
    return userDatabaseConn.register(firstName, lastName, username, password, userType);
  }


  @Override public void addItem(MenuItem menuItem) {
    menuDatabaseConn.addItem(menuItem);
  }
}
