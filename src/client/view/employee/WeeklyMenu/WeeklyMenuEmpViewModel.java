package client.view.employee.WeeklyMenu;

import client.model.MenuModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;
import util.DateHelper;

import java.beans.PropertyChangeEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

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

  public WeeklyMenuEmpViewModel(MenuModel menuModel)
  {
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

  private void weeklyMenuReceived(PropertyChangeEvent event)
  {
    ArrayList<MenuItemWithQuantity> menuItemWithQuantities = (ArrayList<MenuItemWithQuantity>) event.getNewValue();

    setUpDates();

    mondayList.clear();
    tuesdayList.clear();
    wednesdayList.clear();
    thursdayList.clear();
    fridayList.clear();

    for (int i = 0; i < menuItemWithQuantities.size(); i++)
    {
      LocalDate localDate = menuItemWithQuantities.get(i).getDate();

      DayOfWeek dayOfWeek = localDate.getDayOfWeek();

      switch (dayOfWeek)
      {
        case MONDAY:
          mondayList.add(menuItemWithQuantities.get(i));
          break;
        case TUESDAY:
          tuesdayList.add(menuItemWithQuantities.get(i));
          break;
        case WEDNESDAY:
          wednesdayList.add(menuItemWithQuantities.get(i));
          break;
        case THURSDAY:
          thursdayList.add(menuItemWithQuantities.get(i));
          break;
        case FRIDAY:
          fridayList.add(menuItemWithQuantities.get(i));
          break;
      }
    }
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

  public void refresh()
  {
    menuModel.requestWeeklyMenu();
  }


}
