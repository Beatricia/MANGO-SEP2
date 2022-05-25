package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.OrderItem;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The class is responsible for canceling all orders and emptying all customers'
 * carts once the canteen is closed
 * @author Mango
 */
public class ClosingRoutineModelImpl implements ClosingRoutineModel
{
  private Thread timeThread;
  private long waitTime = 0;
  private DatabaseConn databaseConn;

  /**
   * Constructor for the class
   * @param databaseConn the database connection class that the model should
   *                     be connected with
   */
  public ClosingRoutineModelImpl(DatabaseConn databaseConn)
  {
    this.databaseConn = databaseConn;
  }


  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread empties all customers' carts, marks all orders as
   * collected, and removes them from the MyOrder view in the customer
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

        //empty all carts
        try
        {
          databaseConn.emptyAllCarts();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }

        //cancel all orders
        ArrayList<ArrayList<OrderItem>> orders = databaseConn.getAllUncollectedOrders();

        try
        {
          for (ArrayList<OrderItem> order : orders)
          {
            //because all the orderItems in the list come from the same user
            databaseConn.cancelOrder(order.get(0).getUsername());
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

  /**
   * A method that takes a LocalTime object and returns the time in milliseconds
   * @param time the time that should be returned in milliseconds
   * @return the time in milliseconds
   */
  private long toMilliSeconds(LocalTime time)
  {
    long hoursToMin = (time.getHour()*60);
    long minToSec = ((hoursToMin + time.getMinute()) *60);

    return (minToSec * 1000);
  }
}
