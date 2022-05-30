package client.view.employee.WeeklyMenu;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
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
 * View Model for Weekly Menu view. Connects the WeeklyMenuEmpController with the model.
 * All data property can be bound.
 *
 * @author Agata
 * @version 1
 */
public class WeeklyMenuEmpViewModel
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
   * Constructs all the StringProperties, adds a listener to the MenuModel
   * @param menuModel Menu model
   */
  public WeeklyMenuEmpViewModel(MenuModel menuModel)
  {
    this.menuModel = menuModel;
    //this.menuModel = new TestModel();
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
   * Set the right dates for the date StringProperties
   */
  private void setUpDates()
  {
    LocalDate dateVar = DateHelper.getCurrentAvailableMonday();

    for (int i = 0; i <5 ; i++)
    {
      StringProperty dayLabel = dates[i];
      String dateText = dateVar.toString();
      dayLabel.set(dateText);

      dateVar = dateVar.plusDays(1);
    }
  }

  /**
   * Event listener for receiving a weekly menu event from the server
   * @param propertyChangeEvent event data
   */
  private void weeklyMenuReceived(PropertyChangeEvent propertyChangeEvent) {
    ArrayList<MenuItemWithQuantity> menuItemWithQuantities =
        (ArrayList<MenuItemWithQuantity>) propertyChangeEvent.getNewValue();



    System.out.println("Size: " + menuItemWithQuantities.size());

    Platform.runLater(() -> {
      setUpDates();
      mondayList.clear();
      tuesdayList.clear();
      wednesdayList.clear();
      thursdayList.clear();
      fridayList.clear();
    });


    Platform.runLater(() -> {
      for (MenuItemWithQuantity item : menuItemWithQuantities){
        LocalDate localDate = item.getDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        ObservableList<MenuItemWithQuantity> listToPut = null;

        switch (dayOfWeek){
          case MONDAY:
            listToPut = mondayList;
            break;
          case TUESDAY:
            listToPut = tuesdayList;
            break;
          case WEDNESDAY:
            listToPut = wednesdayList;
            break;
          case THURSDAY:
            listToPut = thursdayList;
            break;
          case FRIDAY:
            listToPut = fridayList;
            break;

        }

        if(listToPut != null)
          listToPut.add(item);
      }
    });

  }

  public ObservableList<MenuItemWithQuantity> getMondayList()
  {
    return mondayList;
  }

  public ObservableList<MenuItemWithQuantity> getTuesdayList()
  {
    return tuesdayList;
  }

  public ObservableList<MenuItemWithQuantity> getWednesdayList()
  {
    return wednesdayList;
  }

  public ObservableList<MenuItemWithQuantity> getThursdayList()
  {
    return thursdayList;
  }

  public ObservableList<MenuItemWithQuantity> getFridayList()
  {
    return fridayList;
  }

  public StringProperty mondayDateProperty()
  {
    return mondayDate;
  }

  public StringProperty tuesdayDateProperty()
  {
    return tuesdayDate;
  }

  public StringProperty wednesdayDateProperty()
  {
    return wednesdayDate;
  }

  public StringProperty thursdayDateProperty()
  {
    return thursdayDate;
  }

  public StringProperty fridayDateProperty()
  {
    return fridayDate;
  }

  /**
   * Request weekly menu data from the server.
   */
  public void refresh()
  {
    menuModel.requestWeeklyMenu();
  }

  /**
   * Delete several items from the system, by forwarding the delete request to the menu model.
   * @param listToDelete items to be deleted
   */
  public void deleteItems(ArrayList<MenuItemWithQuantity> listToDelete)
  {
    menuModel.deleteMenuItemFromWeeklyMenu(listToDelete);
  }

}
