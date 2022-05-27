package client.model;

import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import util.PropertyChangeSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface responsible to connect all the shopping cart's functionality to
 * the Client
 * @author Mango
 * @version 1
 */

public interface CartModel extends PropertyChangeSubject
{
  String CART_LIST_RECEIVED = "CartListReceived";
  String IS_ITEM_IN_CART = "IsItemInCart";

  /**
   *The method takes a MenuItemWithQuantity object and send it to the Client
   * so it can be added to the system
   * @param menuItem the item to be sent
   */
  void addToCart(MenuItemWithQuantity menuItem);

  /**
   * The method takes a CartItem object and send it to the Client so it can be
   * edited in the system
   * @param cartItem the item to be edited
   */
  void editCartItem(CartItem cartItem);

  /**
   * The method takes a CartItem object and send it to the Client so it can be
   * removed from the system
   * @param cartItem the item to be deleted
   */
  void deleteCartItem(CartItem cartItem);

  /**
   * The method sends a request to the Client to retrieve a list with all
   * items in the cart
   */
  void requestCartList();

  /**
   * The method signalizes to the Client that the shopping cart should be
   * converted into an order
   */
  void placeOrder();
  /**
   * Gets all the names of cart items, puts them into an ArrayList
   * and fires a propertyChange with this ArrayList
   *
   * @param itemsInCart list of items in the cart
   */
  void isItemInShoppingCart(List<CartItem> itemsInCart);
}
