package client.model;

import client.networking.Client;
import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import transferobjects.Request;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

public class CartModelImpl implements CartModel{
    private Client client;

    private final PropertyChangeSupport support;

    public CartModelImpl(Client client){
        this.client = client;
        support = new PropertyChangeSupport(this);
    }

    @Override public void addToCart(MenuItemWithQuantity menuItem) {
        Request request = new Request(Request.ADD_ITEM_TO_CART);
        request.setObject(menuItem);

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

    @Override public void addListener(String event, PropertyChangeListener listener) {
        support.addPropertyChangeListener(event, listener);
    }

    @Override public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
