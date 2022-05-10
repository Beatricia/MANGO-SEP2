package server.databaseConn;

import shared.UserType;
import transferobjects.LoginRequest;
import transferobjects.MenuItem;
import transferobjects.User;
import util.LogInException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

  /**
   * Format sql exception message so that it makes sense for the end user
   * @param e Exception which was thrown
   * @return the exception with a descriptive error message
   */
  private static SQLException formatExceptionMessage(SQLException e) {
    String message = e.getMessage();

    // Check if the error message is about duplicate primary keys
    if(message.startsWith("ERROR: duplicate key value violates unique constraint "))
      e = new SQLException("Item already exists");

    // Remove that weird capital error text from the beginning of the message
    else if(message.startsWith("ERROR: "))
      e = new SQLException(message.substring("ERROR: ".length()));

    return e;
  }

  private final UserDatabaseConn userDatabaseConn;
  private final MenuDatabaseConn menuDatabaseConn;
  private final AdminDataBaseConnection adminDataBaseConnection;

  public DatabaseConnImp() {
    userDatabaseConn = new UserDatabaseConn();
    menuDatabaseConn = new MenuDatabaseConn();
    adminDataBaseConnection = new AdminDataBaseConnection();
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
  @Override public User login(String username, String password) throws LogInException, SQLException {
    try{
      return userDatabaseConn.login(username, password);
    } catch (SQLException e){
      throw formatExceptionMessage(e);
    }
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
    try{
      return userDatabaseConn.register(firstName, lastName, username, password, userType);
    } catch (SQLException e){
      throw formatExceptionMessage(e);
    }
  }

  /**
   * Adds a Menu item to the database with the ingredients
   * @param name menu item name
   * @param ingredients ingredients
   * @param price price of the menu item
   * @param imgPath img path for the menu
   * @throws SQLException When the menu item name already exists.
   */
  @Override public void addItem(String name, ArrayList<String> ingredients, double price, String imgPath)
      throws SQLException {
    try{
      menuDatabaseConn.addItem(name, ingredients, price, imgPath);
    } catch (SQLException e){
      throw formatExceptionMessage(e);
    }
  }



  @Override public void addDailyMenu(LocalDate date,
      ArrayList<MenuItem> menuItems) throws SQLException
  {

  }

  @Override
  public ArrayList<MenuItem> getListOfMenuItems() {
    return null;
  }

  @Override
  public void handlePendingEmployee(String username, boolean accept) throws SQLException {
    try{
      adminDataBaseConnection.handlePendingEmployee(username,accept);
    } catch (SQLException e){
      System.out.println(e.getMessage());
    }
  }

  @Override
  public ArrayList<User> getAllPendingEmployees() {
    try {
      return adminDataBaseConnection.getAllPendingEmployees();
    } catch (SQLException e){
      System.out.println(e.getMessage());
    }
    return null; // should it return null here?
  }


}
