package client.model;

import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import util.PropertyChangeSubject;

//TODO javadocs

public interface CartModel extends PropertyChangeSubject
{
  String CART_LIST_RECEIVED = "CartListReceived";

  void addToCart(MenuItemWithQuantity menuItem);
  void editCartItem(CartItem cartItem);
  void deleteCartItem(CartItem cartItem);
  void requestCartList();
  void placeOrder();
  /**
   * Updates the cart and checks if item sent through argument is in the customer's cart
   * @param itemName name of the item to check
   * @return true if item is in the cart, else false
   */
  boolean isItemInShoppingCart(String itemName);
}
