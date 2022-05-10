package client.model;

import transferobjects.MenuItem;
import transferobjects.Request;
import util.PropertyChangeSubject;

import java.time.LocalDate;
import java.util.ArrayList;

public interface MenuModel extends PropertyChangeSubject
{
  String ERROR_RECEIVED = "ErrorReceived";
  String Menu_Items_Received = "MenuItemsReceived";

  void addItem(String name, ArrayList<String> ingredients, double price,
      String imgPath);

  void requestMenuItems();

  void addItemsToDailyMenu(LocalDate date,ArrayList<MenuItem> menuItems);
}
