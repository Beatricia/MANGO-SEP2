package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.CartItem;
import transferobjects.OrderItem;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class responsible for connecting the networking part of the Server with
 * Database connection and managing operations with the shopping cart
 * @author Mango
 * @version 1
 */
public class CartModelImpl implements CartModel
{
  private DatabaseConn databaseConn;
  private Thread timeThread;
  private long waitTime = 0;

  /**
   * Constructor for the class
   * @param databaseConn the database connection class the model should connect
   *                     to
   */
  public CartModelImpl(DatabaseConn databaseConn)
  {
    this.databaseConn = databaseConn;
  }

  /**
   * Adds a selected item to the customer's shopping cart
   * @param cartItem the item to be added
   * @throws SQLException
   */
  @Override public void addItemToCart(CartItem cartItem) throws SQLException
  {
    String name = cartItem.getName();
    String username = cartItem.getUsername();

    databaseConn.addItemToCart(name, username);
  }

  /**
   * Edits details about either quantity or ingredients for a specific CartItem
   * object
   * @param cartItem the item to be edited
   * @throws SQLException
   */
  @Override public void editCartItem(CartItem cartItem) throws SQLException
  {
    databaseConn.editCartItem(cartItem);
  }

  /**
   * Removes an item from the customer's shopping cart
   * @param cartItem the item to be removed
   * @throws SQLException
   */
  @Override public void removeCartItem(CartItem cartItem) throws SQLException
  {
    databaseConn.removeCartItem(cartItem);
  }

  /**
   * The method returns all the items in the cart
   * @param username the username of the customer whose cart should be returned
   * @return an ArrayList of all items in the cart
   * @throws SQLException
   */
  @Override public ArrayList<CartItem> getCartList(String username)
      throws SQLException
  {
    return databaseConn.getCartList(username);
  }

  /**
   * The method used to convert the customer's cart into an order
   * @param username the username of the customer whose order should be made
   * @throws SQLException
   */
  @Override public void placeOrder(String username) throws SQLException
  {
    databaseConn.placeOrder(username);
  }

  }

