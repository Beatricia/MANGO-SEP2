package server.model;

import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;

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
   * Adding the list of daily menu items to the database
   * @param dailyMenuItem the list with menu items which should be added to the system
   */

  void addDailyMenuItem(ArrayList<MenuItemWithQuantity> dailyMenuItem) throws SQLException;

  /**
   * Asking for the daily menu
   * @return a list with the menu item with quantity for the current date
   */

  ArrayList<MenuItemWithQuantity> requestDailyMenu() throws SQLException;

  /**
   * Adding the quantity to items from daily menu
   * @param listOfMenuItemsWithQuantity the list of menu items with new quantity
   */

  void addQuantity(ArrayList<MenuItemWithQuantity> listOfMenuItemsWithQuantity) throws SQLException;

  /**
   * Getting the weekly menu from the database
   * @return the array list with Monday-Friday's daily menu (a weekly menu overall)
   */

  ArrayList<MenuItemWithQuantity> requestWeeklyMenu() throws SQLException;

  /**
   * Deleting the items from menu for specific date
   * @param listOfItemsToDelete the list of the items that will be deleted
   */
  void deleteMenuItemFromWeeklyMenu(ArrayList<MenuItemWithQuantity> listOfItemsToDelete) throws SQLException;

  /**
   * The method is used to delete menu items from database
   * @param menuItems the list of menu items to delete
   */
  void removeMenuItem(ArrayList<MenuItem> menuItems);
}
