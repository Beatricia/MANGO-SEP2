package server.model;

import transferobjects.CartItem;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CartModel
{
  void addItemToCart(CartItem cartItem) throws SQLException;
  void editCartItem(CartItem cartItem);
  void deleteCartItem(CartItem cartItem);
  ArrayList<CartItem> getCartList(String username) throws SQLException;
}
