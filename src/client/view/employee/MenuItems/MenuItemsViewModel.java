package client.view.employee.MenuItems;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * View Model class responsible for connecting the MenuItemsController with MenuModel.
 * @author Agata
 * @version 1
 */

public class MenuItemsViewModel
{
  private MenuModel model;
  private ObservableList<MenuItem> items = FXCollections.observableList(new ArrayList<>());

  /**
   * Constructor for the class
   * @param menuModel The model that items have to be send to
   */

  public MenuItemsViewModel(MenuModel menuModel)
  {
    this.model = menuModel;

    model.addListener(MenuModel.MENU_ITEMS_RECEIVED, this::fillInMenuItems);
  }

  private void fillInMenuItems(PropertyChangeEvent event)
  {
    Platform.runLater(
        () -> {
          items.clear();
          ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) event.getNewValue();

          items.addAll(menuItems);
        }
    );
  }

  /**
   * Takes an ObservableList that contains MenuItem objects
   * and calls the removeMenuItem method in the MenuModel.
   * @param selectedItems the MenuItem objects selected by the user
   */
  public void removeMenuItem(ObservableList selectedItems)
  {
    ArrayList itemsToDelete = new ArrayList();
    itemsToDelete.addAll(selectedItems);
    model.removeMenuItem(itemsToDelete);
  }

  /**
   * The method used to call the request for Menu Items from database
   */
  public void requestList()
  {
    model.requestMenuItems();
  }

  /**
   * Returns an Observable list containing Menu Items from the database
   * @return Menu Items from the database in an Observable List
   */
  public ObservableList getMenuItems()
  {
    return items;
  }
}
