package transferobjects;

import javafx.beans.property.Property;
import shared.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class DailyMenuItem implements Serializable
{
  private LocalDate date;
  private ArrayList<MenuItem> menuItems;

  public DailyMenuItem(LocalDate date,ArrayList<MenuItem> menuItems)
  {
    this.date = date;
    this.menuItems = menuItems;

    Log.log("DailyMenuItem transferobject created");
  }

  public LocalDate getDate()
  {
    return date;
  }

  public ArrayList<MenuItem> getMenuItems()
  {
    return menuItems;
  }
}
