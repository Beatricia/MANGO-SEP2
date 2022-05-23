package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.OrderItem;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class responsible for connecting the networking part of the Server with Database connection and sending Order Items into the Database.
 */
public class OrderModelCustomerImpl implements OrderModelCustomer
{
  private DatabaseConn databaseConn;

  /**
   * Constructor which is assigning database connection
   * @param databaseConn is an instance of the database connection which is connection the server with the database
   */

  public OrderModelCustomerImpl(DatabaseConn databaseConn){
    this.databaseConn = databaseConn;
  }

  /**
   * The method is used to delete order from database
   * @param username the username of the customer whose order should be removed
   */

  @Override public void cancelOrder(String username) throws SQLException {
    databaseConn.cancelOrder(username);
  }

  /**
   * Request the database for the specific customer's order
   * @param username the username of customer whose order is got from server
   * @return a list with OrderItems
   */
  @Override public ArrayList<OrderItem> getUncollectedOrder(String username) throws SQLException {
    return databaseConn.getUncollectedOrder(username);
  }

  /**
   * The method is used to delete order from database
   * @param orderCode the code of the order which status should be changed as collected
   */
  @Override public void collectOrder(int orderCode) throws SQLException
  {
    databaseConn.collectOrder(orderCode);
  }

  /**
   * Request the database for the all uncollected orders
   * @return the list of list with orderItems
   */

  @Override public ArrayList<ArrayList<OrderItem>> requestAllUncollectedOrder()
      throws SQLException
  {
    return databaseConn.getAllUncollectedOrder();
  }
}
