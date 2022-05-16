package client.view.employee.AddQuantity;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;
import util.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddQuantityViewModel
{

  private ObservableList<MenuItemWithQuantity> menuItemWithQuantities;
  private MenuModel menuModel;

  public AddQuantityViewModel(MenuModel menuModel)
  {
    this.menuModel = menuModel;
    menuItemWithQuantities = FXCollections.observableArrayList();

    menuModel.addListener(MenuModel.DAILY_MENU_RECEIVED, this:: menuReceived);
  }

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

  public ObservableList<MenuItemWithQuantity> dailyMenuItemsList()
  {
    return menuItemWithQuantities;
  }

  public void requestDailyMenuItems()
  {

    menuModel.requestDailyMenu();

    //JUST FOR TESTING
    /*for (int i = 0; i < 11; i++)
    {
      transferobjects.MenuItem menuItem = new MenuItem("abc", new ArrayList<>(Arrays.asList("cucumber", "banana", "hamburger")),
          3.4, "Resources/MenuItemImages/abc.png");
      MenuItemWithQuantity menuItemWithQuantity = new MenuItemWithQuantity(
          menuItem, LocalDate.now(), 3);
      menuItemWithQuantities.add(menuItemWithQuantity);

    }*/
  }

  public void addQuantityToItems(ArrayList<MenuItemWithQuantity> menuItemWithQuantities)
  {
    menuModel.addQuantity(menuItemWithQuantities);
  }
}
