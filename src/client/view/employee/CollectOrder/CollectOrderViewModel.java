package client.view.employee.CollectOrder;

import client.model.OrderModelCustomer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.OrderItem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * A view model class responsible for connecting the CollectOrderViewModel and
 * the OrderModel
 */
public class CollectOrderViewModel
{
  private OrderModelCustomer orderModel;
  private ObservableList<ArrayList<OrderItem>> uncollectedOrders;

  /**
   * A constructor for the class
   * @param orderModelCustomer the model that the view should be connected to
   */
  public CollectOrderViewModel(OrderModelCustomer orderModelCustomer)
  {
    Log.log("CollectOrderViewModel: Oder marked as collected");

    this.orderModel = orderModelCustomer;
    uncollectedOrders = FXCollections.observableArrayList();

    orderModel.addListener(OrderModelCustomer.ALL_UNCOLLECTED_ORDERS_RECEIVED, this:: ordersReceived);
  }

  /**
   * The method listens to the OrderModel and whenever it fires a
   * ALL_UNCOLLECTED_ORDERS_RECEIVED property, this method fills in the private
   * ObservableList<ArrayList<OrderItem>> attribute with the received data.
   * @param event the event that has been sent by the OrderModel
   */
  private void ordersReceived(PropertyChangeEvent event)
  {
    Log.log("CollectOrderViewModel: All uncollected orders received");

    ArrayList<ArrayList<OrderItem>> orders = (ArrayList<ArrayList<OrderItem>>) event.getNewValue();

    Platform.runLater( () -> {

      uncollectedOrders.clear();
      uncollectedOrders.addAll(orders);
    });
  }

  /**
   * The method returns all uncollected orders
   * @return uncollected orders
   */
  public ObservableList<ArrayList<OrderItem>> getAllOrders()
  {
   return uncollectedOrders;
  }

  /**
   * The method request all uncollected orders from the OrderModel
   */
  public void requestAllUncollectedOrders()
  {
    orderModel.requestAllUncollectedOrders();
  }

  /**
   * The method takes the code of an order and send it to the OrderModel so the
   * order can be canceled.
   * @param orderCode the order's code
   */
  public void cancelOrder(int orderCode)
  {
    orderModel.collectOrder(orderCode);
  }
}
