package server.model;

import transferobjects.OrderItem;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface responsible for connecting the networking part of the Server with Database connection.
 */
public interface OrderModel
{
  /**
   * The method used to delete order connected to the given name
   * @param username
   * @throws SQLException
   */
  void cancelOrder(String username) throws SQLException;

  /**
   * Retrieves the specific customer's order from Databaseconn
   * @return uncollected order item
   * @throws SQLException
   */

  ArrayList<OrderItem> getUncollectedOrder(String object) throws SQLException;

  /**
   * The method used to mark the order, connected to the given name, as collected
   * @param orderCode
   * @throws SQLException
   */
  void collectOrder(int orderCode) throws SQLException;

  /**
   * Retrieves the list of all uncollected orders from Databaseconn
   * @return the list of list with orderItems
   * @throws SQLException
   */
  ArrayList<ArrayList<OrderItem>> requestAllUncollectedOrder() throws SQLException;

  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread marks all orders as collected and removes them from
   * the MyOrder view in the customer
   * @throws SQLException
   */
  void setClosingTimer() throws SQLException;
}
