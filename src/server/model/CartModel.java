package server.model;

import transferobjects.CartItem;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 *  Interface responsible for connecting the networking part of the Server with Database connection.
 */
public interface CartModel
{
  /**
   * Adds a selected item to the customer's shopping cart
   * @param cartItem the item to be added
   * @throws SQLException
   */
  void addItemToCart(CartItem cartItem) throws SQLException;

  /**
   * Edits details about either quantity or ingredients for a specific CartItem
   * object
   * @param cartItem the item to be edited
   * @throws SQLException
   */
  void editCartItem(CartItem cartItem) throws SQLException;

  /**
   * Removes an item from the customer's shopping cart
   * @param cartItem the item to be removed
   * @throws SQLException
   */
  void removeCartItem(CartItem cartItem) throws SQLException;

  /**
   * The method returns all the items in the cart
   * @param username the username of the customer whose cart should be returned
   * @return an ArrayList of all items in the cart
   * @throws SQLException
   */
  ArrayList<CartItem> getCartList(String username) throws SQLException;

  /**
   * The method used to convert the customer's cart into an order
   * @param username the username of the customer whose order should be made
   * @throws SQLException
   */
  void placeOrder(String username) throws SQLException;


  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread empties the customers' shopping cart.
   * @throws SQLException
   */
  void setClosingTimer() throws SQLException;
}
