package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.OrderItem;

import javax.lang.model.util.ElementScanner6;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class responsible for connecting the networking part of the Server with Database connection and sending Order Items into the Database.
 */
public class OrderModelImpl implements OrderModel
{
  private DatabaseConn databaseConn;
  private long waitTime = 0;
  private Thread timeThread;

  /**
   * Constructor which is assigning database connection
   * @param databaseConn is an instance of the database connection which is connection the server with the database
   */

  public OrderModelImpl(DatabaseConn databaseConn){
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
    return databaseConn.getAllUncollectedOrders();
  }

  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread marks all orders as collected and removes them from
   * the MyOrder view in the customer
   * @throws SQLException
   */
  @Override public void setClosingTimer() throws SQLException
  {
    ArrayList<LocalTime> time = databaseConn.getOpeningHours();
   // LocalTime opening = time.get(0);
    LocalTime closing = time.get(1);


    //have to get All uncollected orders and mark them as collected
    Runnable runnable = () -> {
      try
      {
       if(closing.isBefore(LocalTime.now()))
       {
         waitTime = (1000*60*60*24) - (toMilliSeconds(LocalTime.now()) - toMilliSeconds(closing));
          Thread.sleep(waitTime);
        }
        else
       {
         waitTime = toMilliSeconds(closing) - toMilliSeconds(LocalTime.now());
         Thread.sleep(waitTime);
       }
          ArrayList<ArrayList<OrderItem>> orders = databaseConn.getAllUncollectedOrders();

        try
        {
          for (ArrayList<OrderItem> order : orders)
          {
            //because all the orderItems in the list have the same code
            databaseConn.collectOrder(order.get(0).getCode());
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      catch (InterruptedException | SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        setClosingTimer();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    };

    timeThread = new Thread(runnable);
    timeThread.start();

  }

  private long toMilliSeconds(LocalTime time)
  {
    long hoursToMin = (time.getHour()*60);
    long minToSec = ((hoursToMin + time.getMinute()) *60);

    return (minToSec * 1000);
  }


}
