package client.view.customer.displayMenu;

import client.model.CartModel;
import client.model.MenuModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;

import java.beans.PropertyChangeEvent;
import java.util.List;

//TODO javadocs

/**
 * A class that connects the DisplayMenuController with the MenuModel.
 *
 *
 * @author Greg, Simon
 */
public class DisplayMenuViewModel
{
  private ObservableList<MenuItemWithQuantity> menuItemWithQuantities;
  private MenuModel menuModel;
  private CartModel cartModel;

  public DisplayMenuViewModel(MenuModel menuModel, CartModel cartModel){
    this.menuModel = menuModel;
    this.cartModel = cartModel;
    menuItemWithQuantities = FXCollections.observableArrayList();

    menuModel.addListener(MenuModel.DAILY_MENU_RECEIVED, this::menuReceived);
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
}
