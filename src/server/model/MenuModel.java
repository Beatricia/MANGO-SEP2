package server.model;

import transferobjects.DailyMenuItemList;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;
import transferobjects.Request;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface responsible for connecting the networking part of the Server with Database connection.
 * @author Simon
 * @version 1
 */
public interface MenuModel
{

  /**
   * Passes the menu item onto the Database connection.
   * @param menuItem which is unwrapped and passed onto the class Databaseconn
   */
  void addItem(MenuItem menuItem) throws SQLException;

  /**
   * Retrieves the list of all MenuItems from Databaseconn
   * @return list off all Menu items
   * @throws SQLException
   */

  ArrayList<MenuItem> getListOfMenuItems() throws SQLException;

  /**
   * Passes the daily menu item onto the Database connection.
   * @param dailyMenuItemList which is unwrapped and passed onto the class Databaseconn
   */
  void addDailyMenuItem(DailyMenuItemList dailyMenuItemList) throws SQLException;

  ArrayList<MenuItemWithQuantity> requestDailyMenu() throws SQLException;

  void addQuantity(ArrayList<MenuItemWithQuantity> listOfMenuItemsWithQuantity) throws SQLException;
}
