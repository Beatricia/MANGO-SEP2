package client.view.MenuEmpl.DailyMenu;

import client.model.MenuModel;
import javafx.collections.ObservableList;
import transferobjects.MenuItem;

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
  private ArrayList<MenuItem> menuItems;

  /**
   * Constructor for the class
   * @param menuModel The model that items have to be send to
   */
  public DailyMenuViewModel(MenuModel menuModel)
  {
    model = menuModel;
    menuItems = new ArrayList<>();
  }

  /**
   * Takes an ObservableList that contains MenuItem objects and a LocalDate
   * object and calls the addItemsToDailyMenu method in the MenuModel.
   * @param selectedItems the MenuItem objects selected by the user
   * @param date the date specified by the user
   */
  public void addToDailyMenu(ObservableList<MenuItem> selectedItems, LocalDate date)
  {
    selectedItems.addAll(menuItems);

    model.addItemsToDailyMenu(date, menuItems);
  }
}
