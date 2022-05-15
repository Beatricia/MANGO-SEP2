package client.view.employee.DailyMenu;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.MenuItem;

import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * View Model class responsible for connecting the add menu items to daily menu
 * view and the MenuModel.
 * @author Uafa
 * @version 1
 */
public class DailyMenuViewModel
{
  private MenuModel model;
  private ObservableList<MenuItem> items = FXCollections.observableList(new ArrayList<>());

  /**
   * Constructor for the class
   * @param menuModel The model that items have to be send to
   */
  public DailyMenuViewModel(MenuModel menuModel)
  {
    model = menuModel;

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
   * Takes an ObservableList that contains MenuItem objects and a LocalDate
   * object and calls the addItemsToDailyMenu method in the MenuModel.
   * @param selectedItems the MenuItem objects selected by the user
   * @param date the date specified by the user
   */
  public void addToDailyMenu(ObservableList<MenuItem> selectedItems, LocalDate date)
  {
    ArrayList<MenuItem> menuItems = new ArrayList<>(selectedItems);
    Platform.runLater(
        () -> {
          model.addItemsToDailyMenu(date, menuItems);
        }
    );
    Log.log("DailyMenuViewModel calls the addItemsToDailyMenu on the MenuModel");
  }

  /**
   * Returns an Observable list containing Menu Items from the database
   * @return Menu Items from the database in an Observable List
   */
  public ObservableList<MenuItem> getMenuItems()
  {
    return items;
  }

  public void requestList()
  {
    model.requestMenuItems();
  }
}
