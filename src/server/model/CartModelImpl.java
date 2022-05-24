package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.CartItem;
import transferobjects.OrderItem;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
//TODO javadocs
public class CartModelImpl implements CartModel
{
  private DatabaseConn databaseConn;
  private Thread timeThread;
  private long waitTime = 0;

  public CartModelImpl(DatabaseConn databaseConn)
  {
    this.databaseConn = databaseConn;
  }

  @Override public void addItemToCart(CartItem cartItem) throws SQLException
  {
    String name = cartItem.getName();
    String username = cartItem.getUsername();

    databaseConn.addItemToCart(name, username);
  }

  @Override public void editCartItem(CartItem cartItem) throws SQLException
  {
    databaseConn.editCartItem(cartItem);
  }

  @Override public void removeCartItem(CartItem cartItem) throws SQLException
  {
    databaseConn.removeCartItem(cartItem);
  }

  @Override public ArrayList<CartItem> getCartList(String username)
      throws SQLException
  {
    return databaseConn.getCartList(username);
  }

  @Override public void placeOrder(String username) throws SQLException
  {
    databaseConn.placeOrder(username);
  }

  /**
   * Starts a Thread which is asleep during the opening hours. Once the canteen
   * is closed the thread empties the customers' shopping cart.
   * @throws SQLException
   */
  @Override public void setClosingTimer() throws SQLException
  {

    if (timeThread!= null)
    {
      timeThread.interrupt();
    }

    ArrayList<LocalTime> time = databaseConn.getOpeningHours();
    // LocalTime opening = time.get(0);
    LocalTime closing = time.get(1);


    //have to get All uncollected orders and mark them as collected
    Runnable runnable = () -> {
      try
      {
        if (closing.isBefore(LocalTime.now()))
        {
          waitTime = (1000 * 60 * 60 * 24) - (toMilliSeconds(LocalTime.now())
              - toMilliSeconds(closing));
          Thread.sleep(waitTime);
        }
        else
        {
          waitTime = toMilliSeconds(closing) - toMilliSeconds(LocalTime.now());
          Thread.sleep(waitTime);
        }
        ArrayList<ArrayList<OrderItem>> orders = databaseConn.getAllUncollectedOrders();

          //todo

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

