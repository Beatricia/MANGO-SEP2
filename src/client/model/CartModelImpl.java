package client.model;

import client.networking.Client;
import transferobjects.CartItem;

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

    @Override public void addToCart(CartItem cartItem) {

    }

    @Override public void editCartItem(CartItem cartItem) {

    }

    @Override public void deleteCartItem(CartItem cartItem) {

    }

    @Override public void requestCartList() {
        
    }

    @Override public void addListener(String event, PropertyChangeListener listener) {
        support.addPropertyChangeListener(event, listener);
    }

    @Override public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
