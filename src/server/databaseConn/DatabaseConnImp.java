package server.databaseConn;

import shared.Log;
import shared.UserType;
import transferobjects.*;
import util.LogInException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

  private final UserDatabaseConn userDatabaseConn;
  private final MenuDatabaseConn menuDatabaseConn;
  private final AdminDatabaseConnection adminDataBaseConnection;
  private final CartDatabaseConn cartDatabaseConn;
  private final OrderDatabaseConn orderDatabaseConn;

  /**
   * Contractor for a class
   * initialize all needed subclasses
   */

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

  /**
   * Add a list of MenuItemWithQuantity to their assigned days.
   * @param menuItems menu items to be added to the daily menu
   * @throws SQLException on unexpected exception
   */
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

  /**
   * Gets a list of all the menu items registered in the system.
   * @return the list containing all the menu items.
   * @throws SQLException if any error happens during setting up the database connection
   */
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

  /**
   * Get a list of MenuItemWithQuantity for a specific date
   * @param date the date to get the menu items from
   * @return a list containing the MenuItemWithQuantity objects on the specific date
   * @throws SQLException if any error happens during setting up the database connection
   */
  @Override public ArrayList<MenuItemWithQuantity> gatDailyMenuItemList(LocalDate date)
      throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a getDailyMenuItem request to the MenuDatabaseConn");
    return menuDatabaseConn.getDailyMenuItemList(date);
  }

  /**
   * Add quantity to a menu item which is enrolled in the daily menu
   * @param date date on which the menu item is enrolled in.
   * @param name name of the menu item
   * @param quantity quantity
   * @throws SQLException if any error happens during setting up the database connection
   */
  @Override public void addQuantity(LocalDate date, String name, int quantity)
      throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a AddQuantity request to the MenuDatabaseConn");
    menuDatabaseConn.addQuantity(date, name, quantity);
  }

  /**
   * The method is used to delete menu item from daily menu by given date and name
   * @param date the daily menu's day
   * @param name menu item's name
   * @throws SQLException
   */
  @Override public void deleteMenuItemFromDailyMenu(LocalDate date, String name)
      throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a deleteMenuItemFromDailyMenu request to the MenuDatabaseConn");
    menuDatabaseConn.deleteMenuItemFromDailyMenu(date,name);
  }

  /**
   * The method calls teh private method getCartIdFromUsername to get the cart
   * id, and inserts the item that has the name given as a parameter into
   * the cartItem table with the cart id value.
   * @param cartItemName the name of the item to be added to the cartItem table
   * @param username  the username of the Client used to get the cart id
   * @throws SQLException on unexpected exception
   */
  @Override
  public void addItemToCart(String cartItemName, String username) throws SQLException
  {

    try{
      Log.log("DatabaseConnImp: Sending an addItemToCart request to the CartDatabaseConn");
      cartDatabaseConn.addItemToCart(cartItemName,username);
    }
    catch (SQLException e){
      if (e.getMessage().startsWith("ERROR: new row for relation \"dailymenuitem\"")){
        throw new SQLException("The item is sold out :(");
      }
      throw formatExceptionMessage(e);
    }
  }

  /**
   * Called whenever a new quantity value is set for the item in cart or/and
   * whenever an ingredient is unselected from the view. The method calls the
   * private getCartIdFromUsername method to get te cart id using the
   * provided in the cart item username, first edits the quantity, then checks
   * if there are any unselected ingredients, if yes calls te private method
   * updateUnselectedIngredientsTable.
   *
   * @param cartItem the cart item that has to be edited
   * @throws SQLException on unexpected exception
   */
  @Override
  public void editCartItem(CartItem cartItem) throws SQLException
  {
    try {
      Log.log("DatabaseConnImp: Sending an editCartItem request to the CartDatabaseConn");
      cartDatabaseConn.editCartItem(cartItem);
    } catch (SQLException e){
      if (e.getMessage().startsWith("ERROR: new row for relation \"dailymenuitem\"")){
        throw  new SQLException("The available quantity is not enough :(");
      }
      throw formatExceptionMessage(e);
    }
  }

  /**
   * The method deletes the specified cart item from the cartItem table
   * (The table has a TRIGGER which automatically deletes all the unselected
   * items for this item from the cartUnselectedIngredient table)
   * @param cartItem  the cart item that should be deleted
   * @throws SQLException on unexpected exception
   */
  @Override
  public void removeCartItem(CartItem cartItem) throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a removeCartItem request to the CartDatabaseConn");
    cartDatabaseConn.removeCartItem(cartItem);
  }

  /**
   * The method first calls the private method getCartIdFromUsername to get
   * the cart id with the username given as a parameter. Secondly, the itemName
   * and quantity are selected from the cartItem. Next the price and image path
   * are extracted from the menuItemTable. Lastly, the getAllIngredients
   * private method is called to obtain all the ingredients of the menu item,
   * and with all that information a new CartItem object is created and added
   * an ArrayList, which is returned (when creating the CartItem object the
   * unselected ingredients are represented by an empty ArrayList)
   * @param username user's username to get the cart list
   * @return an Arraylist with all the CartItem object in the shopping cart
   * @throws SQLException on unexpected exception
   */
  @Override
  public ArrayList<CartItem> getCartList(String username) throws SQLException
  {
    Log.log("DatabaseConnImp: Sending a getCartList request to the MenuDatabaseConn");
    return cartDatabaseConn.getCartList(username);
  }

  /**
   * Method which is called when an order is placed.
   * First is gets  all the required data from the cart of customer who placed the order,
   * then it creates an order and order items with the unselected ingredients.
   * All of these data are then inserted into the right tables.
   * Lastly, the method deletes the whole cart.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  @Override public void placeOrder(String username) throws SQLException
  {
    orderDatabaseConn.placeOrder(username);
  }

  /**
   * Cancels the whole order of the customer.
   * This means all the data about the order are deleted.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  @Override public void cancelOrder(String username) throws SQLException
  {
    orderDatabaseConn.cancelOrder(username);
  }

  /**
   * Returns all data of the customer's order.
   * That is name of the ordered items, its ingredients, prices, image paths, quantities, unselected ingredients, username of the customer and code of the order
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  @Override public ArrayList<OrderItem> getUncollectedOrder(String username)
      throws SQLException
  {
    return orderDatabaseConn.getUncollectedOrder(username);
  }

  /**
   * Gets all the uncollected orders from all the customers and returns a list of each customer's
   * list of order items.
   * @return a list of each customer's list of order items.
   * @throws SQLException when an unexpected exception happens
   */
  @Override public ArrayList<ArrayList<OrderItem>> getAllUncollectedOrders() throws SQLException {
    return orderDatabaseConn.getAllUncollectedOrders();
  }

  /**
   * Mark an uncollected order as collected by the employee.
   * @param orderCode the order's code to mark it collected.
   * @throws SQLException when the order was cancelled before this task.
   */
  @Override public void collectOrder(int orderCode) throws SQLException {
    orderDatabaseConn.collectOrder(orderCode);
  }

  /**
   * Calling the setOpeningHours method in adminDataBaseConnection
   * @param from LocalTime object which stores the opening time of the canteen
   * @param to LocalTime object which stores the closing time of the canteen
   * @throws SQLException when the table does not exist
   */
  @Override public void setOpeningHours(LocalTime from, LocalTime to)
      throws SQLException
  {
    Log.log("DatabaseConnImp: calls setOpeningHours in aDmInDataBaseCoNnEcTiOn");
    adminDataBaseConnection.setOpeningHours(from, to);
  }

  /**
   * Calling the getOpeningHours method in adminDataBaseConnection
   * @return an arrayList of LocalTime objects representing opening and closing time of the canteen
   * @throws SQLException when the table does not exist
   */
  @Override public ArrayList<LocalTime> getOpeningHours() throws SQLException
  {
    Log.log("DatabaseConnImp: calls getOpeningHours in adminDatabaseConnection");
    return adminDataBaseConnection.getOpeningHours();
  }

  /**
   * Calling the emptyAllCarts method in cartDataBaseConnection
   * @throws SQLException
   */
  @Override public void emptyAllCarts() throws SQLException
  {
    cartDatabaseConn.emptyAllCarts();
  }

  /**
   * The method is used to delete menu item from the system
   * @param menuItems menu item to delete
   * @throws SQLException
   */
  @Override
  public void removeMenuItem(ArrayList<MenuItem> menuItems) throws SQLException
  {
    Log.log("DatabaseConnImp: Sending the removed menu item(s) to the MenuDatabaseConn");
     menuDatabaseConn.removeMenuItem(menuItems);
  }

  /**
   * Accept the employee with specific username
   * @param username employee's username
   * @param accept employee's status
   * @throws SQLException
   */
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

  /**
   * Gets a list of all pending employees from the database
   * @return the list containing user objects
   * @throws SQLException
   */
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

  /**
   * Sends the username of employee which should be deleted from the system
   * @param username employee's username
   * @throws SQLException
   */
  @Override
  public void removeEmployee(String username) throws SQLException {
    try
    {
      Log.log("DatabaseConnImp: Sending a removeEmployee request to the AdminDatabaseConn");
      adminDataBaseConnection.removeEmployee(username);
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e); //look again
    }
  }
  /**
   * Gets a list of all employees from the database
   * @return the list containing all employees
   * @throws SQLException
   */


  @Override
  public ArrayList<User> getAcceptedEmployees() throws SQLException {
    try
    {
      Log.log("DatabaseConnImp: Sending a getAcceptedEmployee request to the AdminDatabaseConn");
      return adminDataBaseConnection.getAllAcceptedEmployees();
    }
    catch (SQLException e)
    {
      throw formatExceptionMessage(e); // look again
    }
  }
}
