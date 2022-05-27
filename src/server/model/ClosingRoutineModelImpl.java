package server.model;

import server.databaseConn.DatabaseConn;
import shared.Log;
import transferobjects.OrderItem;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The class is responsible for canceling all orders and emptying all customers'
 * carts once the canteen is closed
 *
 * @author Mango
 */
public class ClosingRoutineModelImpl implements ClosingRoutineModel
{
  private Thread timeThread;
  private long waitTime = 0;
  private final DatabaseConn databaseConn;
  private ArrayList<LocalTime> time;

  /**
   * Constructor for the class
   *
   * @param databaseConn the database connection class that the model should
   *                     be connected with
   */
  public ClosingRoutineModelImpl(DatabaseConn databaseConn) {
    this.databaseConn = databaseConn;
  }

  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread empties all customers' carts, marks all orders as
   * collected, and removes them from the MyOrder view in the customer
   */
  @Override public void setClosingTimer() {
    if (timeThread!=null) {
      timeThread.interrupt();
    }

    try {
      time = databaseConn.getOpeningHours();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    timeThread = new Thread(this::timer);

    Log.log("ClosingRoutineModelImp: Thread starts");
    timeThread.start();

  }

  /**
   * Private method that empties all shopping carts once the canteen is closed
   */
  private void emptyAllCarts() {
    try {
      Log.log("ClosingRoutineModelImp: Empty all carts");
      databaseConn.emptyAllCarts();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Private method that cancels all uncollected orders once the canteen is
   * closed
   */
  private void cancelAllOrders() {
    try {
      databaseConn.cancelOrder(null);
      /*ArrayList<ArrayList<OrderItem>> orders = databaseConn.getAllUncollectedOrders();
      Log.log("ClosingRoutineModelImp: All orders cancelled ");
      for (ArrayList<OrderItem> order : orders) {
        //because all the orderItems in the list come from the same user
        try{
          databaseConn.cancelOrder(order.get(0).getUsername());
        } catch (SQLException ignored) {
        }
      }*/
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * After waiting for the next closing time, it empties all the carts and cancels all the
   * uncollected orders automatically. If this method's thread was not interrupted during the
   * waiting process, it will call the setClosingTimer method again, to automatically set itself
   * for the next closing time
   */
  private void timer() {
    try {
      LocalTime closing = time.get(1);
      LocalTime now = LocalTime.now();
      if (closing.isBefore(now)) {
        waitTime = (1000 * 60 * 60 * 24) - (toMilliSeconds(now) - toMilliSeconds(closing));
      }
      else {
        waitTime = toMilliSeconds(closing) - toMilliSeconds(now);
      }
      Log.log("ClosingRoutineModelImp: Thread waiting: " + waitTime);
      Thread.sleep(waitTime);

      emptyAllCarts();
      cancelAllOrders();

    }
    catch (InterruptedException e) {
      e.printStackTrace();
      return;
    }

    setClosingTimer();
  }

  /**
   * A method that takes a LocalTime object and returns the time in milliseconds
   *
   * @param time the time that should be returned in milliseconds
   * @return the time in milliseconds
   */
  private long toMilliSeconds(LocalTime time) {
    long hoursToMin = (time.getHour() * 60);
    long minToSec = ((hoursToMin + time.getMinute()) * 60);

    return (minToSec * 1000);
  }
}
