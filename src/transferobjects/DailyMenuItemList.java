package transferobjects;

import javafx.beans.property.Property;
import shared.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class representing a DailyMenuItem object with date and list of menuItems .
 * @author Agata
 * @version 1
 */

public class DailyMenuItemList implements Serializable
{
  private LocalDate date;
  private ArrayList<MenuItemWithQuantity> menuItems;

  /**
   * Construct the DailyMenuItem object
   * needed to transfer scheduled daily menu
   * @param date date of menu
   * @param menuItems list of menu's items
   */

  public DailyMenuItemList(LocalDate date,ArrayList<MenuItemWithQuantity> menuItems)
  {
    this.date = date;
    this.menuItems = menuItems;

    Log.log("DailyMenuItem transferobject created");
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
  public ArrayList<MenuItemWithQuantity> getMenuItems()
  {
    return menuItems;
  }
}
