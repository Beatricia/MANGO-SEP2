package client.model;

import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import util.PropertyChangeSubject;

public interface CartModel extends PropertyChangeSubject
{
  String CART_LIST_RECEIVED = "CartListReceived";

  void addToCart(MenuItemWithQuantity menuItem);
  void editCartItem(CartItem cartItem);
  void deleteCartItem(CartItem cartItem);

  void requestCartList();
}
