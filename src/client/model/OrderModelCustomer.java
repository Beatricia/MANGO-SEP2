package client.model;

import transferobjects.OrderItem;
import util.PropertyChangeSubject;

import java.util.ArrayList;

/**
 * An interface which is responsible for handling the connection between the
 * ViewModels and the Networking
 */

public interface OrderModelCustomer extends PropertyChangeSubject
{
  String ORDER_RECEIVED = "OrderReceived";
  String ALL_UNCOLLECTED_ORDERS_RECEIVED = "AllUncollectedOrdersReceived";

  /**
   * The method is used to send a request for a specific customer's uncollected order to the Client
   */
  void requestUncollectedOrder();

  /**
   * The method is used to delete the customer's order
   */
  void cancelOrder();

  /**
   * The method is used to mark the order as collected
   * @param orderCode order code, the status of which should be changed to collected
   */
  void collectOrder(int orderCode);

  /**
   * The method is used to send a request for list of all uncollected orders to the Client
   */

  void requestAllUncollectedOrders();
}
