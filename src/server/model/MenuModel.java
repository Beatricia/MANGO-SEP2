package server.model;

import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;

import java.sql.SQLException;
import java.util.ArrayList;
//TODO javadocs
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

  void addDailyMenuItem(ArrayList<MenuItemWithQuantity> dailyMenuItem) throws SQLException;

  ArrayList<MenuItemWithQuantity> requestDailyMenu() throws SQLException;

  void addQuantity(ArrayList<MenuItemWithQuantity> listOfMenuItemsWithQuantity) throws SQLException;

  ArrayList<MenuItemWithQuantity> requestWeeklyMenu() throws SQLException;

  void deleteMenuItemFromWeeklyMenu(ArrayList<MenuItemWithQuantity> listOfItemsToDelete) throws SQLException;
}
