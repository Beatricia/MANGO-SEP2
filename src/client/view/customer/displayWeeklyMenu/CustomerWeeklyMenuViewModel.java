package client.view.customer.displayWeeklyMenu;

import client.model.MenuModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;
import util.DateHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This view model is responsible for display ready data for the controller.
 *
 * @author Greg
 * @version 1
 */
public class CustomerWeeklyMenuViewModel
{
  private final StringProperty mondayDate;
  private final StringProperty tuesdayDate;
  private final StringProperty wednesdayDate;
  private final StringProperty thursdayDate;
  private final StringProperty fridayDate;
  
  private final StringProperty[] dates;
  
  private final ObservableList<MenuItemWithQuantity> mondayList;
  private final ObservableList<MenuItemWithQuantity> tuesdayList;
  private final ObservableList<MenuItemWithQuantity> wednesdayList;
  private final ObservableList<MenuItemWithQuantity> thursdayList;
  private final ObservableList<MenuItemWithQuantity> fridayList;


  private final MenuModel menuModel;

  /**
   * Initializes the CustomerWeeklyMenuViewModel class, creating all the StringProperties
   * and observable lists. Also setting up the correct dates for each of the date StringProperty
   * @param menuModel MenuModel to get data from.
   */
  public CustomerWeeklyMenuViewModel(MenuModel menuModel){
    this.menuModel = menuModel;
    this.menuModel.addListener(MenuModel.WEEKLY_MENU_RECEIVED, this::weeklyMenuReceived);
    
    mondayDate = new SimpleStringProperty();
    tuesdayDate = new SimpleStringProperty();
    wednesdayDate = new SimpleStringProperty();
    thursdayDate = new SimpleStringProperty();
    fridayDate = new SimpleStringProperty();

    dates = new StringProperty[] {mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate};

    mondayList = FXCollections.observableArrayList();
    tuesdayList = FXCollections.observableArrayList();
    wednesdayList = FXCollections.observableArrayList();
    thursdayList = FXCollections.observableArrayList();
    fridayList = FXCollections.observableArrayList();
    
    setUpDates();
  }

  /**
   * Weekly menu item received event handler for the menu model
   * @param propertyChangeEvent event data
   */
  private void weeklyMenuReceived(PropertyChangeEvent propertyChangeEvent) {
    ArrayList<MenuItemWithQuantity> menuItemWithQuantities =
        (ArrayList<MenuItemWithQuantity>) propertyChangeEvent.getNewValue();

    setUpDates();

    System.out.println("Size: " + menuItemWithQuantities.size());

    mondayList.clear();
    tuesdayList.clear();
    wednesdayList.clear();
    thursdayList.clear();
    fridayList.clear();

    for (MenuItemWithQuantity item : menuItemWithQuantities){
      LocalDate localDate = item.getDate();
      DayOfWeek dayOfWeek = localDate.getDayOfWeek();

      switch (dayOfWeek){
        case MONDAY:
          mondayList.add(item);
          break;
        case TUESDAY:
          tuesdayList.add(item);
          break;
        case WEDNESDAY:
          wednesdayList.add(item);
          break;
        case THURSDAY:
          thursdayList.add(item);
          break;
        case FRIDAY:
          fridayList.add(item);
          break;

      }
    }
  }

  /**
   * Setting up the correct dates in the date string properties
   */
  private void setUpDates(){
    LocalDate dateVar = DateHelper.getCurrentAvailableMonday();

    for (int i = 0; i < 5; i++) {
      StringProperty dayLabel = dates[i];
      String dateText = dateVar.toString();
      dayLabel.set(dateText);

      dateVar = dateVar.plusDays(1);
    }
  }

  /**
   * Requesting all the required data from the server
   */
  public void refresh(){
    menuModel.requestWeeklyMenu();
  }

  
  public StringProperty mondayDateProperty() {
    return mondayDate;
  }
  public StringProperty tuesdayDateProperty() {
    return tuesdayDate;
  }
  public StringProperty wednesdayDateProperty() {
    return wednesdayDate;
  }
  public StringProperty thursdayDateProperty() {
    return thursdayDate;
  }
  public StringProperty fridayDateProperty() {
    return fridayDate;
  }

  public ObservableList<MenuItemWithQuantity> getMondayList() {
    return mondayList;
  }
  public ObservableList<MenuItemWithQuantity> getTuesdayList() {
    return tuesdayList;
  }
  public ObservableList<MenuItemWithQuantity> getWednesdayList() {
    return wednesdayList;
  }
  public ObservableList<MenuItemWithQuantity> getThursdayList() {
    return thursdayList;
  }
  public ObservableList<MenuItemWithQuantity> getFridayList() {
    return fridayList;
  }


  // ------------- Test class --------------
  static class TestModel implements MenuModel{
    private PropertyChangeListener listener = null;
    private int count = 1;

    @Override public void requestWeeklyMenu() {
      System.out.println("RefreshWeeklyMenu");
      ArrayList<MenuItemWithQuantity> menuItems = new ArrayList<>();

      LocalDate date = DateHelper.getCurrentAvailableMonday();

      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < i + count; j++) {
          MenuItem menuItem = new MenuItem("item", new ArrayList<>(), 3, "");
          MenuItemWithQuantity menuItemWithQuantity =
              new MenuItemWithQuantity(menuItem, date, 3);

          menuItems.add(menuItemWithQuantity);
        }
        date = date.plusDays(1);
      }
      count++;

      PropertyChangeEvent event =
          new PropertyChangeEvent(this, MenuModel.WEEKLY_MENU_RECEIVED, null, menuItems);

      listener.propertyChange(event);
    }

    @Override public void addListener(String event, PropertyChangeListener listener) {
      System.out.println("Listener added");
      this.listener = listener;
    }




    @Override public void addListener(PropertyChangeListener listener) {}
    @Override public void addItem(String name, ArrayList<String> ingredients, double price,
        String imgPath) {}
    @Override public void requestMenuItems() {}
    @Override public void addItemsToDailyMenu(LocalDate date, ArrayList<MenuItem> menuItems) {}
    @Override public void requestDailyMenu() {}
    @Override public void addQuantity(ArrayList<MenuItemWithQuantity> listOfItemsWithQuantity) {}
    @Override public void deleteMenuItemFromWeeklyMenu(
        ArrayList<MenuItemWithQuantity> listOfMenuItemsToDelete) {}
  }

}

