package transferobjects;

import javafx.beans.property.Property;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class representing a DailyMenuItem object with date and list of menuItems .
 * @author Agata
 * @version 1
 */

public class DailyMenuItem implements Serializable
{
  private LocalDate date;
  private ArrayList<MenuItem> menuItems;

  /**
   * Construct the DailyMenuItem object
   * needed to transfer scheduled daily menu
   * @param date date of menu
   * @param menuItems list of menu's items
   */

  public DailyMenuItem(LocalDate date,ArrayList<MenuItem> menuItems)
  {
    this.date = date;
    this.menuItems = menuItems;
  }

  /**
   * Gets the menu's date
   * @return date
   */
  public LocalDate getDate()
  {
    return date;
  }

  /**
   * Gets list of menu's items
   * @return ArrayList<MenuItem>
   */
  public ArrayList<MenuItem> getMenuItems()
  {
    return menuItems;
  }
}
