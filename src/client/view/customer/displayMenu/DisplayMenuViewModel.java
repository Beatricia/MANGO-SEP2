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

  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  public DisplayMenuViewModel(MenuModel menuModel, CartModel cartModel){
    this.menuModel = menuModel;
    this.cartModel = cartModel;
    menuItemWithQuantities = FXCollections.observableArrayList();

    menuModel.addListener(MenuModel.DAILY_MENU_RECEIVED, this::menuReceived);
    menuModel.addListener(MenuModel.OPENING_HOURS_RECEIVED, this::openingHoursReceived);
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
   * Listener for MenuModel. If change called OPENING_HOURS_RECEIVED is received opening hours are updated and new Thread is started.
   * The thread checks if the current time is within the opening hours (canteen is open) or outside (canteen is closed).
   * Whenever there is a change in the canteen's opening state a propertyChange is fired.
   * @param propertyChangeEvent Update of the opening hours
   */
  public void openingHoursReceived(PropertyChangeEvent propertyChangeEvent)
  {
    //Uafi needs to fire it in ArrayList<LocalTime>
    ArrayList<LocalTime> openingHours = (ArrayList<LocalTime>) propertyChangeEvent.getNewValue();

    LocalTime open = openingHours.get(0);

    LocalTime close = openingHours.get(1);

    System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

    Thread openingHoursThread = new Thread(()->{
      while (true){
        if (LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).equals(open.toString())){
          canteenIsClosed = false;
          propertyChangeSupport.firePropertyChange(MenuModel.OPENING_HOURS_RECEIVED, - 1,  canteenIsClosed);
        }
        else if (LocalTime.now().isAfter(open) && LocalTime.now().isBefore(close)){
          if (canteenIsClosed){
            canteenIsClosed = false;
            propertyChangeSupport.firePropertyChange(MenuModel.OPENING_HOURS_RECEIVED, - 1,  canteenIsClosed);
          }
        }
        else if (LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).equals(close.toString())){
          canteenIsClosed = true;
          propertyChangeSupport.firePropertyChange(MenuModel.OPENING_HOURS_RECEIVED, - 1,  canteenIsClosed);
        }
      }
    });

    openingHoursThread.start();
  }

  /**
   * Calls isItemInShoppingCart in cartModel
   * @param itemName name of the item to check
   * @return true if item is in the cart, else false
   */
  public boolean isItemInShoppingCart(String itemName){
    Log.log("DisplayMenuViewModel: Calls isItemInShoppingCart in cartModel");
    return cartModel.isItemInShoppingCart(itemName);
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
