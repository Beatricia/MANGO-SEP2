package client.model;

import client.networking.Client;
import shared.Log;
import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import transferobjects.Request;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that connects the view model and the Client side of networking.
 * In this class all the functionality connected to the shopping cart can be
 * found
 *
 * @author Mango
 */

public class CartModelImpl implements CartModel
{
  private Client client;
  private final PropertyChangeSupport support;
  private List<CartItem> itemsInShoppingCart;

  /**
   * Constructor for the class. Takes a Client object as a parameter and
   * assigns it to the client private variable in the class. Adds the class as
   * to be a Listener for the Client object.
   *
   * @param client the client that information has to be sent to
   */
  public CartModelImpl(Client client)
  {
    support = new PropertyChangeSupport(this);

    this.client = client;
    client.addListener(Client.CART_LIST_RECEIVED, this::cartListReceived);
  }

  /**
   * Private method that fires a CART_LIST_RECEIVED event with a list of
   * cart items
   *
   * @param evt the event that occurred
   */
  private void cartListReceived(PropertyChangeEvent evt)
  {
    Object cartItems = evt.getNewValue();
    itemsInShoppingCart = (List<CartItem>) cartItems;
    support.firePropertyChange(CartModel.CART_LIST_RECEIVED, null, cartItems);
  }

  /**
   * The method takes a MenuItemWithQuantity object, which it wraps in an
   * ADD_ITEM_TO_CART request and sends to the Client to add it to the system
   *
   * @param menuItem the item to be sent
   */
  @Override public void addToCart(MenuItemWithQuantity menuItem)
  {
    Request request = new Request(Request.ADD_ITEM_TO_CART);
    CartItem cartItem = new CartItem(menuItem.getName(),
        menuItem.getIngredients(), menuItem.getPrice(), menuItem.getImgPath(),
        UserModelImp.getUsername(), 1, new ArrayList<>());
    request.setObject(cartItem);

    client.sendRequest(request);
  }

  /**
   * The method takes a CartItem object, which it wraps in an
   * EDIT_CART_ITEM request and sends to the Client to edit it in the system
   *
   * @param cartItem the item to be sent
   */
  @Override public void editCartItem(CartItem cartItem)
  {
    Request request = new Request(Request.EDIT_CART_ITEM);
    request.setObject(cartItem);

    client.sendRequest(request);
  }

  /**
   * The method takes a CartItem object, which it wraps in an
   * DELETE_CART_ITEM request and sends to the Client to delete it in the system
   *
   * @param cartItem the item to be sent
   */
  @Override public void deleteCartItem(CartItem cartItem)
  {
    Request request = new Request(Request.DELETE_CART_ITEM);
    request.setObject(cartItem);

    client.sendRequest(request);
  }

  /**
   * The method sends a CART_LIST_REQUEST request to the Client to retrieve a
   * list with all items in the cart
   */
  @Override public void requestCartList()
  {
    String username = UserModelImp.getUsername();

    Request request = new Request(Request.CART_LIST_REQUEST);
    request.setObject(username);

    client.sendRequest(request);
  }

  /**
   * The method send to the Client a PLACE_ORDER request to signalize the
   * shopping cart should be converted into an order
   */
  @Override public void placeOrder()
  {
    String username = UserModelImp.getUsername();

    Request request = new Request(Request.PLACE_ORDER);
    request.setObject(username);

    client.sendRequest(request);
  }

  /**
   * Using the PropertyChangeSubject object adds a listener for specific types
   * of events
   *
   * @param event    the name of the specific event that should be listened for
   * @param listener listener to be added
   */
  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(event, listener);
  }

  /**
   * Updates the cart and checks if item sent through argument is in the customer's cart
   *
   * @param itemName name of the item to check
   * @return true if item is in the cart, else false
   */
  @Override public boolean isItemInShoppingCart(String itemName)
  {

    requestCartList();
    Log.log("CartModelImp: Checks if item is in cart.");
    boolean isIn = false;

    //somehow wait here

    if (itemsInShoppingCart != null)
    {
      for (CartItem item : itemsInShoppingCart)
      {
        if (item.getName().equals(itemName))
        {
          isIn = true;
        }
      }
    }

    return isIn;
  }

  /**
   * Using the PropertyChangeSubject object adds a listener for all types of
   * events
   *
   * @param listener listener to be added
   */
  @Override public void addListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

}


