package server.model;

import java.sql.SQLException;

/**
 * Interface responsible for canceling all orders and emptying all customers'
 * carts once the canteen is closed
 * @author Mango
 */
public interface ClosingRoutineModel
{

  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread empties all customers' carts, marks all orders as
   * collected, and removes them from the MyOrder view in the customer
   * @throws SQLException
   */
  void setClosingTimer() throws SQLException;
}
