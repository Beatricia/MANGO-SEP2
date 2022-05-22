package server.databaseConn;

import shared.Log;
import shared.UserType;
import transferobjects.*;
import util.LogInException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

//TODO javadocs

/**
 * A concrete implementation of the {@link DatabaseConn}.
 *
 * @author Mango
 * @version 1
 */
public class DatabaseConnImp implements DatabaseConn
{

  private final UserDatabaseConn userDatabaseConn;
  private final MenuDatabaseConn menuDatabaseConn;
  private final AdminDatabaseConnection adminDataBaseConnection;
  private final CartDatabaseConn cartDatabaseConn;
  private final OrderDatabaseConn orderDatabaseConn;

  public DatabaseConnImp()
  {
    userDatabaseConn = new UserDatabaseConn();
    menuDatabaseConn = new MenuDatabaseConn();
    adminDataBaseConnection = new AdminDatabaseConnection();
    cartDatabaseConn = new CartDatabaseConn();
    orderDatabaseConn = new OrderDatabaseConn();
  }

  /**
   * Password to the SQL Server.
   */
  private static String password;

  /**
   * Returns the password if it is loaded, else it loads the password from the file.
   *
   * @return The password to access the database.
   */
  private static String getPass()
  {
    if (password != null)
      return password;

    File file = new File("Resources/DataBase_password.txt");
    try
    {
      Scanner scanner = new Scanner(file);
      password = scanner.nextLine();
      System.out.println(password);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }

    Log.log("DatabaseConnImp: System just hacked your Password for DataGrip");
    return password;
  }

  /**
   * Get database connection for the sql server.
   *
   * @return A connection object to access the database.
   * @throws SQLException When an unexpected exception happens.
   */
  static Connection getConnection() throws SQLException
  {
    Log.log("DatabaseConnImp: Connection with Database established");

    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=caneat",
        "postgres", getPass());

  }

  /**
   * Format sql exception message so that it makes sense for the end user
   *
   * @param e Exception which was thrown
   * @return the exception with a descriptive error message
   */
  private static SQLException formatExceptionMessage(SQLException e)
  {
    String message = e.getMessage();
    e.printStackTrace();

    // Check if the error message is about duplicate primary keys
    if (message.startsWith(
        "ERROR: duplicate key value violates unique constraint ")) {
      e = new SQLException("Item already exists");
    }

      // Remove that weird capital error text from the beginning of the message
    else if (message.startsWith("ERROR: ")) {
    e = new SQLException(message.substring("ERROR: ".length()));
  }

    return e;
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
      throws LogInException, SQLException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a Log-In request to the UserDatabaseConn");
      return userDatabaseConn.login(username, password);
    }
    catch (SQLException e)
    {
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
  @Override public User register(String firstName, String lastName,
      String username, String password, UserType userType)
      throws SQLException, LogInException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a Register request to the UserDatabaseConn");
      return userDatabaseConn.register(firstName, lastName, username, password,
          userType);
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e);
    }
  }

  /**
   * Adds a Menu item to the database with the ingredients
   *
   * @param name        menu item name
   * @param ingredients ingredients
   * @param price       price of the menu item
   * @param imgPath     img path for the menu
   * @throws SQLException When the menu item name already exists.
   */
  @Override public void addItem(String name, ArrayList<String> ingredients,
      double price, String imgPath) throws SQLException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a addItem request to the MenuDatabaseConn");
      menuDatabaseConn.addItem(name, ingredients, price, imgPath);
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e);
    }
  }

  @Override public void addDailyMenu(
      ArrayList<MenuItemWithQuantity> menuItems) throws SQLException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a AddDailyMenu request to the MenuDatabaseConn");
      menuDatabaseConn.addDailyMenu(menuItems);
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e);
    }
  }

  @Override public ArrayList<MenuItem> getListOfMenuItems() throws SQLException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a getListMenuItems request to the MenuDatabaseConn");
      return menuDatabaseConn.getListOfMenuItems();
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e);
    }
  }

  @Override public ArrayList<MenuItemWithQuantity> gatDailyMenuItemList(LocalDate date)
      throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a getDailyMenuItem request to the MenuDatabaseConn");
    return menuDatabaseConn.getDailyMenuItemList(date);
  }

  @Override public void addQuantity(LocalDate date, String name, int quantity)
      throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a AddQuantity request to the MenuDatabaseConn");
    menuDatabaseConn.addQuantity(date, name, quantity);
  }

  @Override public void deleteMenuItemFromDailyMenu(LocalDate date, String name)
      throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a deleteMenuItemFromDailyMenu request to the MenuDatabaseConn");
    menuDatabaseConn.deleteMenuItemFromDailyMenu(date,name);
  }

  @Override
  public void addItemToCart(String cartItemName, String username) throws SQLException
  {

    try{
      Log.log("DatabaseConnImp: Sending an addItemToCart request to the CartDatabaseConn");
      cartDatabaseConn.addItemToCart(cartItemName,username);
    }
    catch (SQLException e){
      if (e.getMessage().startsWith("ERROR: new row for relation \"dailymenuitem\"")){
        e = new SQLException("The item is sold out :(");
      }
      throw formatExceptionMessage(e);
    }
  }

  @Override
  public void editCartItem(CartItem cartItem) throws SQLException
  {
    try {
      Log.log("DatabaseConnImp: Sending an editCartItem request to the CartDatabaseConn");
      cartDatabaseConn.editCartItem(cartItem);
    } catch (SQLException e){
      if (e.getMessage().startsWith("ERROR: new row for relation \"dailymenuitem\"")){
        e = new SQLException("The available quantity is not enough :(");
      }
      throw formatExceptionMessage(e);
    }
  }

  @Override
  public void removeCartItem(CartItem cartItem) throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a removeCartItem request to the CartDatabaseConn");
    cartDatabaseConn.removeCartItem(cartItem);
  }

  @Override
  public ArrayList<CartItem> getCartList(String username) throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a getCartList request to the MenuDatabaseConn");
    return cartDatabaseConn.getCartList(username);
  }

  @Override public ArrayList<OrderItem> placeOrder(String username) throws SQLException
  {
    return orderDatabaseConn.placeOrder(username);
  }

  @Override public void cancelOrder(String username) throws SQLException
  {
    orderDatabaseConn.cancelOrder(username);
  }

  @Override public ArrayList<OrderItem> getUncollectedOrder(String username)
      throws SQLException
  {
    return orderDatabaseConn.getUncollectedOrder(username);
  }

  @Override public void handlePendingEmployee(String username, boolean accept)
      throws SQLException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a handlePendingEmployee request to the AminDatabaseConn");
      adminDataBaseConnection.handlePendingEmployee(username, accept);
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e);
    }
  }

  @Override public ArrayList<User> getAllPendingEmployees() throws SQLException
  {
    try
    {
      Log.log("DatabaseConnImp: Sending a getAllPendingEmployees request to the AdminDatabaseConn");
      return adminDataBaseConnection.getAllPendingEmployees();
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e);
    }
  }
}
