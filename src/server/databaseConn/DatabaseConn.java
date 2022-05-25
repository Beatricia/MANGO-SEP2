package server.databaseConn;

import shared.UserType;
import transferobjects.*;
import transferobjects.MenuItem;
import util.LogInException;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

//TODO javadocs

/**
 * Abstract Model for accessing the Database.
 */
public interface DatabaseConn
{
  //region USER

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

  //endregion

  //region ADMIN
  void handlePendingEmployee(String userName, boolean accept) throws SQLException;

  ArrayList<User> getAllPendingEmployees() throws SQLException;

  void removeEmployee(String username) throws SQLException;

  ArrayList<User> getAcceptedEmployees() throws SQLException;
  //endregion

  //region MENU

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

  /**
   * Add a list of MenuItemWithQuantity to their assigned days.
   * @param menuItems menu items to be added to the daily menu
   * @throws SQLException on unexpected exception
   */
  void addDailyMenu(ArrayList<MenuItemWithQuantity> menuItems) throws SQLException;

  /**
   * Gets a list of all the menu items registered in the system.
   * @return the list containing all the menu items.
   * @throws SQLException if any error happens during setting up the database connection
   */
  ArrayList<MenuItem> getListOfMenuItems() throws SQLException;

  /**
   * Get a list of MenuItemWithQuantity for a specific date
   * @param date the date to get the menu items from
   * @return a list containing the MenuItemWithQuantity objects on the specific date
   * @throws SQLException if any error happens during setting up the database connection
   */
  ArrayList<MenuItemWithQuantity> gatDailyMenuItemList(LocalDate date) throws SQLException;

  /**
   * Add quantity to a menu item which is enrolled in the daily menu
   * @param date date on which the menu item is enrolled in.
   * @param name name of the menu item
   * @param quantity quantity
   * @throws SQLException if any error happens during setting up the database connection
   */
  void addQuantity(LocalDate date, String name, int quantity) throws SQLException;

  void deleteMenuItemFromDailyMenu(LocalDate date, String name)
      throws SQLException;

  void removeMenuItem(ArrayList<MenuItem> menuItems) throws SQLException;

  //endregion

  //region CART

  /**
   * The method calls teh private method getCartIdFromUsername to get the cart
   * id, and inserts the item that has the name given as a parameter into
   * the cartItem table with the cart id value.
   * @param cartItemName the name of the item to be added to the cartItem table
   * @param username  the username of the Client used to get the cart id
   * @throws SQLException on unexpected exception
   */
  void addItemToCart(String cartItemName, String username) throws SQLException;

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
  void editCartItem(CartItem cartItem) throws SQLException;

  /**
   * The method deletes the specified cart item from the cartItem table
   * (The table has a TRIGGER which automatically deletes all the unselected
   * items for this item from the cartUnselectedIngredient table)
   * @param cartItem  the cart item that should be deleted
   * @throws SQLException on unexpected exception
   */
  void removeCartItem(CartItem cartItem) throws SQLException;

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
  ArrayList<CartItem> getCartList(String username) throws SQLException;

  //endregion

  //region ORDER

  /**
   * Method which is called when an order is placed.
   * First is gets  all the required data from the cart of customer who placed the order,
   * then it creates an order and order items with the unselected ingredients.
   * All of these data are then inserted into the right tables.
   * Lastly, the method deletes the whole cart.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  void placeOrder(String username) throws SQLException;

  /**
   * Cancels the whole order of the customer.
   * This means all the data about the order are deleted.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  void cancelOrder(String username) throws SQLException;

  /**
   * Returns all data of the customer's order.
   * That is name of the ordered items, its ingredients, prices, image paths, quantities, unselected ingredients, username of the customer and code of the order
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  ArrayList<OrderItem> getUncollectedOrder(String username) throws SQLException;

  /**
   * Gets all the uncollected orders from all the customers and returns a list of each customer's
   * list of order items.
   * @return a list of each customer's list of order items.
   * @throws SQLException when an unexpected exception happens
   */
  ArrayList<ArrayList<OrderItem>> getAllUncollectedOrders() throws SQLException;

  /**
   * Mark an uncollected order as collected by the employee.
   * @param orderCode the order's code to mark it collected.
   * @throws SQLException when the order was cancelled before this task.
   */
  void collectOrder(int orderCode) throws SQLException;

  //endregion

  //region SET OPENING HOURS

  void setOpeningHours(LocalTime from, LocalTime to) throws SQLException;

  ArrayList<LocalTime> getOpeningHours() throws SQLException;


  //endregion
}
