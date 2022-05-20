package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.CartItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class CartModelImpl implements CartModel
{
  private DatabaseConn databaseConn;

  public CartModelImpl(DatabaseConn databaseConn){
    this.databaseConn = databaseConn;
  }

  @Override public void addItemToCart(CartItem cartItem) throws SQLException {
    String name = cartItem.getName();
    String username = cartItem.getUsername();

    databaseConn.addItemToCart(name, username);
  }

  @Override public void editCartItem(CartItem cartItem) throws SQLException {
    databaseConn.editCartItem(cartItem);
  }

  @Override public void removeCartItem(CartItem cartItem) throws SQLException {
    databaseConn.removeCartItem(cartItem);
  }

  @Override public ArrayList<CartItem> getCartList(String username) throws SQLException {
    return databaseConn.getCartList(username);
  }

  @Override public void placeOrder(String username) throws SQLException
  {
    databaseConn.placeOrder(username);
  }
}
