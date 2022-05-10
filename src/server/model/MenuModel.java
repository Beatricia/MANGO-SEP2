package server.model;

import transferobjects.DailyMenuItem;
import transferobjects.MenuItem;
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

  ArrayList<MenuItem> getListOfMenuItems();

  void addDailyMenuItem(DailyMenuItem dailyMenuItem) throws SQLException;

}
