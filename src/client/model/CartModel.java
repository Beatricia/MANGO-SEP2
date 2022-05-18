package client.model;

import transferobjects.CartItem;
import util.PropertyChangeSubject;

public interface CartModel extends PropertyChangeSubject
{
  String CART_LIST_RECEIVED = "CartListReceived";

  void addToCart(CartItem cartItem);
  void editCartItem(CartItem cartItem);
  void deleteCartItem(CartItem cartItem);

  void requestCartList();
}
