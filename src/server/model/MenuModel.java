package server.model;

import transferobjects.MenuItem;

/**
 * Interface responsible for connecting the networking part of the Server with Database connection.
 * @author Simon
 * @version 1
 */
public interface MenuModel
{

  /**
   * Passes the menu item onto the Database connection.
   * @param menuItem which is passed onto the class Databaseconn
   */
  void addItem(MenuItem menuItem);
}
