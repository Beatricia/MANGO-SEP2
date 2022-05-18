package client.view.admin.displayMenu;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItemWithQuantity;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * A class that connects the DisplayMenuController with the MenuModel.
 *
 *
 * @author Greg
 */
public class DisplayMenuViewModel
{
  private ObservableList<MenuItemWithQuantity> menuItemWithQuantities;
  private MenuModel menuModel;

  public DisplayMenuViewModel(MenuModel menuModel){
    this.menuModel = menuModel;
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
}
