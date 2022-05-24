package client.model;

import client.networking.Client;
import javafx.collections.ObservableList;
import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import transferobjects.Request;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO javadocs

public class CartModelImpl implements CartModel{
    private Client client;
    private final PropertyChangeSupport support;

    private static List<CartItem> itemsInShoppingCart;

    public CartModelImpl(Client client){
        support = new PropertyChangeSupport(this);

        this.client = client;
        client.addListener(Client.CART_LIST_RECEIVED, this::cartListReceived);
    }

    private void cartListReceived(PropertyChangeEvent evt) {
        Object cartItems = evt.getNewValue();
        itemsInShoppingCart = (List<CartItem>) cartItems;
        support.firePropertyChange(CartModel.CART_LIST_RECEIVED, null, cartItems);
    }

    @Override public void addToCart(MenuItemWithQuantity menuItem) {
        Request request = new Request(Request.ADD_ITEM_TO_CART);
        CartItem cartItem = new CartItem(menuItem.getName(), menuItem.getIngredients(), menuItem.getPrice(), menuItem.getImgPath(),
                UserModelImp.getUsername(), 1, new ArrayList<>());
        request.setObject(cartItem);


        client.sendRequest(request);
    }

    @Override public void editCartItem(CartItem cartItem) {
        Request request = new Request(Request.EDIT_CART_ITEM);
        request.setObject(cartItem);

        client.sendRequest(request);
    }

    @Override public void deleteCartItem(CartItem cartItem) {
        Request request = new Request(Request.DELETE_CART_ITEM);
        request.setObject(cartItem);

        client.sendRequest(request);
    }

    @Override public void requestCartList() {
        String username = UserModelImp.getUsername();

        Request request = new Request(Request.CART_LIST_REQUEST);
        request.setObject(username);

        client.sendRequest(request);
    }

    @Override public void placeOrder()
    {
        String username = UserModelImp.getUsername();

        Request request = new Request(Request.PLACE_ORDER);
        request.setObject(username);

        client.sendRequest(request);
    }

    @Override public void addListener(String event, PropertyChangeListener listener) {
        support.addPropertyChangeListener(event, listener);
    }

    @Override public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Checks is item sent through argument is in the customer's cart
     * @param itemName name of the item to check
     * @return true if item is in the cart, else false
     */
    public static boolean isItemInShoppingCart(String itemName){

        boolean isIn = false;

        if (itemsInShoppingCart != null){
            for (CartItem item:itemsInShoppingCart
            )
            {
                if(item.getName().equals(itemName)){
                    isIn = true;
                }
            }
        }

        return isIn;
    }
}


