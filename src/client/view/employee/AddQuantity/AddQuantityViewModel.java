package client.view.employee.AddQuantity;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItemWithQuantity;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that connects the AddQuantityController with the MenuModel class.
 *
 * @author Uafa
 */
public class AddQuantityViewModel
{

  private ObservableList<MenuItemWithQuantity> menuItemWithQuantities;
  private MenuModel menuModel;

  /**
   * Constructor for the class
   * @param menuModel the model class that must be connected to view
   */
  public AddQuantityViewModel(MenuModel menuModel)
  {
    this.menuModel = menuModel;
    menuItemWithQuantities = FXCollections.observableArrayList();

    menuModel.addListener(MenuModel.DAILY_MENU_RECEIVED, this:: menuReceived);
  }

  /**
   * Whenever a Daily Menu is received this method adds all the items to an
   * ObservableList<MenuItemWithQuantity> list
   * @param event the PropertyChangeEvent that occurred
   */
  private void menuReceived(PropertyChangeEvent event)
  {
    List<MenuItemWithQuantity> menuItems = (List<MenuItemWithQuantity>) event.getNewValue();
    Platform.runLater(
        () -> {
          menuItemWithQuantities.clear();
          menuItemWithQuantities.addAll(menuItems);
        }
    );

  }

  /**
   * Returns the ObservableList<MenuItemWithQuantity> that is filled in from the
   * MenuModel
   * @return an ObservableList containing MenuItemWithQuantities
   */
  public ObservableList<MenuItemWithQuantity> dailyMenuItemsList()
  {
    return menuItemWithQuantities;
  }

  /**
   * Requests the daily menu from the MenuModel
   */
  public void requestDailyMenuItems()
  {
    menuModel.requestDailyMenu();
  }

  /**
   * Sends to the MenuModel an ArrayList with multiple MenuItemWIthQuantity
   * objects.
   * @param menuItemWithQuantities an ArrayList containing MenuItemWithQuantity
   *                               objects that the user has specified quantity
   *                               for.
   */
  public void addQuantityToItems(ArrayList<MenuItemWithQuantity> menuItemWithQuantities)
  {
    menuModel.addQuantity(menuItemWithQuantities);
  }
}
