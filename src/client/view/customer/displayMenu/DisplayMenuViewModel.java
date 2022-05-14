package client.view.customer.displayMenu;

import client.model.MenuModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItemWithQuantity;

public class DisplayMenuViewModel
{
  private ObservableList<MenuItemWithQuantity> menuItemWithQuantities;
  private MenuModel menuModel;

  public DisplayMenuViewModel(MenuModel menuModel){
    this.menuModel = menuModel;
    menuItemWithQuantities = FXCollections.observableArrayList();
  }

  public void requestDailyMenuItems(){
    menuModel.requestDailyMenu();
  }

  public ObservableList<MenuItemWithQuantity> menuItemWithQuantitiesList(){
    return menuItemWithQuantities;
  }
}
