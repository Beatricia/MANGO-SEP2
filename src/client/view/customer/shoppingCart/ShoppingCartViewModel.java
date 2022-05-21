package client.view.customer.shoppingCart;

import client.model.CartModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.CartItem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that connects the DisplayMenuController with the MenuModel.
 * @author Beatricia
 */
public class ShoppingCartViewModel {
    private ObservableList<CartItem> cartItems;
    private CartModel cartModel;
    private IntegerProperty quantity;

    /**
     * Constructor for the class, initializes the ObservableList cartItems, and adds the class to be a Listener
     * to the CartModel
     * @param cartModel the model that is subject for the class
     */
    public ShoppingCartViewModel(CartModel cartModel) {
        this.cartModel=cartModel;
        cartItems = FXCollections.observableArrayList();

        cartModel.addListener(CartModel.CART_LIST_RECEIVED, this::cartReceived);
    }

    /**
     * Adding all the new cartItemsList to the cartItems everytime a new property is fired
     * @param propertyChangeEvent the event that is fired
     */
    private void cartReceived(PropertyChangeEvent propertyChangeEvent){
        List<CartItem> cartItemList = (List<CartItem>) propertyChangeEvent.getNewValue();

        Platform.runLater( () -> {
            cartItems.clear();
            cartItems.addAll(cartItemList);
        });
    }

    /**
     * Getting the ObservableList of type CartItem
     * @return
     */
    public ObservableList<CartItem> getAllCartItems(){
        return cartItems;
    }

    /**
     * Calling the placeOrder on the cartModel
     */
    public void placeOrder(){
        cartModel.placeOrder();
    }

    /**
     * Calling the requestCartList on the cartModel
     */
    public void refresh(){
        cartModel.requestCartList();
    }

    /**
     * Calling the editCartItem on the cartModel with a specific cartItem
     * @param cartItem the cartItem to be edited
     */
    public void editCartItem(CartItem cartItem) {
        cartModel.editCartItem(cartItem);
    }

    /**
     * Calling the deleteCartItem on the cartModel with a specific cartItem
     * @param cartItem the cartItem to be deleted
     */
    public void deleteCartItem(CartItem cartItem) {
        cartModel.deleteCartItem(cartItem);
    }
}
