package client.view.customer.shoppingCart;

import client.model.CartModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.CartItem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartViewModel {
    private ObservableList<CartItem> cartItems;
    private CartModel cartModel;

    public ShoppingCartViewModel(CartModel cartModel) {
        this.cartModel=cartModel;
        cartItems = FXCollections.observableArrayList();

        cartModel.addListener(CartModel.CART_LIST_RECEIVED, this::cartReceived);
    }

    private void cartReceived(PropertyChangeEvent propertyChangeEvent){
        List<CartItem> cartItemList = (List<CartItem>) propertyChangeEvent.getNewValue();

        Platform.runLater( () -> {
            cartItems.clear();
            cartItems.addAll(cartItemList);
        });
    }

    public ObservableList<CartItem> getAllCartItems(){
        return cartItems;
    }

    public void placeOrder(){
        cartModel.placeOrder();
    }

    public void refresh(){
        cartModel.requestCartList();
    }


    public void editCartItem(CartItem cartItem) {
        cartModel.editCartItem(cartItem);
    }

    public void deleteCartItem(CartItem cartItem) {
        cartModel.deleteCartItem(cartItem);
    }
}
