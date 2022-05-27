package client.view.customer.displayMenu;

import client.model.CartModel;
import client.model.MenuModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import util.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//TODO javadocs

/**
 * A class that connects the DisplayMenuController with the MenuModel.
 *
 *
 * @author Greg, Simon
 */
public class DisplayMenuViewModel implements PropertyChangeSubject
{
  private ObservableList<MenuItemWithQuantity> menuItemWithQuantities;
  private MenuModel menuModel;
  private CartModel cartModel;
  private boolean canteenIsClosed = true;
  private LocalTime openingHour;
  private LocalTime closingHour;
  ArrayList<LocalTime> openingHours = new ArrayList<>();

  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public DisplayMenuViewModel(MenuModel menuModel, CartModel cartModel){
    this.menuModel = menuModel;
    this.cartModel = cartModel;
    menuItemWithQuantities = FXCollections.observableArrayList();

    menuModel.addListener(MenuModel.DAILY_MENU_RECEIVED, this::menuReceived);
    menuModel.addListener(MenuModel.OPENING_HOURS_RECEIVED, this::openingHoursReceived);

    cartModel.addListener(CartModel.IS_ITEM_IN_CART, this::isItemInShoppingCartReceived);
  }

  private void menuReceived(PropertyChangeEvent propertyChangeEvent) {
    List<MenuItemWithQuantity> menuItems = (List<MenuItemWithQuantity>) propertyChangeEvent.getNewValue();

    Platform.runLater(() -> {
      menuItemWithQuantities.clear();

      for (MenuItemWithQuantity m : menuItems){
        if(m.getQuantity() > 0)
          menuItemWithQuantities.add(m);
      }
    });
  }

  public void requestDailyMenuItems(){
    menuModel.requestDailyMenu();
  }

  public void requestOpeningHours() { menuModel.requestOpeningHours(); }

  public ObservableList<MenuItemWithQuantity> menuItemWithQuantitiesList(){
    return menuItemWithQuantities;
  }

  /**
   * Calles the method addToCart in cartModel
   * @param menuItem instance of MenuItemWithQuantity which is added to cart
   */
  public void addMenuItemToCart(MenuItemWithQuantity menuItem){
    Log.log("DisplayMenuViewModel: Add item to a cart");
    cartModel.addToCart(menuItem);
  }

  /**
   * Listener for MenuModel. If change called OPENING_HOURS_RECEIVED is received opening hours are updated.
   * @param propertyChangeEvent Update of the opening hours
   */
  public void openingHoursReceived(PropertyChangeEvent propertyChangeEvent)
  {
    openingHours = (ArrayList<LocalTime>) propertyChangeEvent.getNewValue();

    openingHour = openingHours.get(0);
    closingHour = openingHours.get(1);

  }

  /**
   * Calls requestOpeningHours method.
   * Checks if the time now is within or outside of the opening hours.
   * If within, changes the canteenIsClosed to false, else to true, and fires propertyChange
   */
  public void isCanteenClosed(){
    requestOpeningHours();

    if (openingHour != null && closingHour != null)
    {
      if (LocalTime.now().isAfter(openingHour) && LocalTime.now().isBefore(closingHour)){
        canteenIsClosed = false;
        propertyChangeSupport.firePropertyChange(MenuModel.OPENING_HOURS_RECEIVED, null,  canteenIsClosed);
      }
      else {
        canteenIsClosed = true;
        propertyChangeSupport.firePropertyChange(MenuModel.OPENING_HOURS_RECEIVED, null,  canteenIsClosed);
      }
    }

  }

  /**
   * Calls isItemInShoppingCart in cartModel
   */
  public void itemsInShoppingCartRequest(){
    Log.log("DisplayMenuViewModel: Calls isItemInShoppingCart in cartModel");
    cartModel.requestCartList();
  }

  public void isItemInShoppingCartReceived(PropertyChangeEvent propertyChangeEvent){
    propertyChangeSupport.firePropertyChange(CartModel.IS_ITEM_IN_CART, null, propertyChangeEvent);
  }

  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(event, listener);
  }

  @Override public void addListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

}
