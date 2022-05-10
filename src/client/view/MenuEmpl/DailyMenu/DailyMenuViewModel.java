package client.view.MenuEmpl.DailyMenu;

import client.model.MenuModel;
import javafx.collections.FXCollections;
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
  private ObservableList<MenuItem> items = FXCollections.observableList(new ArrayList<>());

  /**
   * Constructor for the class
   * @param menuModel The model that items have to be send to
   */
  public DailyMenuViewModel(MenuModel menuModel)
  {
    model = menuModel;



    //just for testing
    ArrayList<String> ingr = new ArrayList<>();
    ingr.add("chicken");
    ingr.add("tomato");

    MenuItem menu1 = new MenuItem("lasagna", ingr, 12.5);
    MenuItem menu2 = new MenuItem("salat", ingr, 12.5);
    MenuItem menu3 = new MenuItem("chik", ingr, 12.5);

      items.addAll(menu1,menu2, menu3);
    //
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
    model.addItemsToDailyMenu(date, menuItems);
  }

  /**
   * Returns an Observable list containing Menu Items from the database
   * @return Menu Items from the database in an Observable List
   */
  public ObservableList<MenuItem> getMenuItems()
  {
    return items;
  }
}
