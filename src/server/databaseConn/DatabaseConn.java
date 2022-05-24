package server.databaseConn;

import shared.UserType;
import transferobjects.*;
import util.LogInException;

import java.sql.SQLException;
import java.time.LocalDate;
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

  void addDailyMenu(ArrayList<MenuItemWithQuantity> menuItems) throws SQLException;

  ArrayList<MenuItem> getListOfMenuItems() throws SQLException;

  ArrayList<MenuItemWithQuantity> gatDailyMenuItemList(LocalDate date) throws SQLException;

  void addQuantity(LocalDate date, String name, int quantity) throws SQLException;

  void deleteMenuItemFromDailyMenu(LocalDate date, String name)
      throws SQLException;

  //endregion

  //region CART

  void addItemToCart(String cartItemName, String username) throws SQLException;

  void editCartItem(CartItem cartItem) throws SQLException;

  void removeCartItem(CartItem cartItem) throws SQLException;

  ArrayList<CartItem> getCartList(String username) throws SQLException;

  //endregion

  //region ORDER

  void placeOrder(String username) throws SQLException;

  void cancelOrder(String username) throws SQLException;

  ArrayList<OrderItem> getUncollectedOrder(String username) throws SQLException;
  void removeMenuItem(ArrayList<MenuItem> menuItems);
  void collectOrder(int orderCode);
  ArrayList<ArrayList<OrderItem>> getAllUncollectedOrder();

  ArrayList<ArrayList<OrderItem>> getAllUncollectedOrders() throws SQLException;

  void collectOrder(int orderCode) throws SQLException;

  //endregion

  void removeMenuItem(ArrayList<MenuItem> menuItems) throws SQLException;
}
