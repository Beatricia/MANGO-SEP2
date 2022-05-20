package client.view.customer.myOrder;

import client.model.OrderModelCustomer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.OrderItem;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that connects the MyOrderController with the OrderModelCustomer.
 * @author Agata
 */

public class MyOrderViewModel
{
  private ObservableList<OrderItem> orderItems;
  private OrderModelCustomer orderModel;
  private StringProperty orderCode;
  private StringProperty price;

  /**
   * Constructor for the class, initializes the ObservableList orderItems, and adds the class to be a Listener
   * to the OrderModelCustomer
   * @param orderModel the model that is subject for the class
   */

  public MyOrderViewModel(OrderModelCustomer orderModel)
  {
    //orderModel = new TestModel();
    this.orderModel = orderModel;

    orderCode = new SimpleStringProperty();
    price = new SimpleStringProperty();

    orderItems = FXCollections.observableArrayList();

    orderModel.addListener(OrderModelCustomer.ORDER_RECEIVED, this::orderReceived);
  }

  /**
   * Calling the requestUncolectedOrder on the orderMenu
   */
  public void refresh()
  {
    Log.log("MyOrderViewModel: requestUncollectedOrder is created");
    orderModel.requestUncollectedOrder();
  }

  /**
   * The method is used to return total price of order items
   * @return
   */

  public StringProperty getTotalPrice()
  {

    return price;
  }

  /**
   * The method is used to return orderCode
   * @return
   */

  public StringProperty getOrderCode()
  {
    return orderCode;
  }

  /**
   * Adding all the new orderItemsList to the orderItems everytime a new property is fired
   * @param event the event that is fired
   */

  private void orderReceived(PropertyChangeEvent event)
  {
    Log.log("MyOrderViewModel: List with orderItems is received");
    List<OrderItem> orderItemsList = (List<OrderItem>) event.getNewValue();

    Platform.runLater(() ->{
      orderItems.clear();
      orderItems.addAll(orderItemsList);
      if (orderItemsList.size() > 0)
      {
        orderCode.set(String.format("%06d",orderItemsList.get(orderItemsList.size()-1).getCode()));
      }

      double priceDouble = 0;
      for (int i = 0; i < orderItemsList.size(); i++)
      {
        priceDouble += orderItemsList.get(i).getPrice() * orderItemsList.get(i).getQuantity();
      }
      price.set(priceDouble + " dkk");
    });
  }

  /**
   * Getting the ObservableList of type orderItem
   * @return
   */
  public ObservableList<OrderItem> getAllOrderItems()
  {
    Log.log("MyOrderViewModel: return allOrderItems");
    return orderItems;
  }

  /**
   * Calling the cancel method on the orderModel
   */

  public void cancelOrder()
  {
    Log.log("MyOrderViewModel: cancelOrder method is called");

    orderModel.cancelOrder();
  }

  public static class TestModel implements OrderModelCustomer
  {
    private PropertyChangeListener listener;

    @Override public void requestUncollectedOrder()
    {
      ArrayList<String> ingredient = new ArrayList<>();
      ingredient.add("banana");
      ingredient.add("onion");
      ingredient.add("tomato");
      OrderItem item1 = new OrderItem("Agata", ingredient, 13, null, null, 3,
          null, 13);
      OrderItem item2 = new OrderItem("Bartel", ingredient, 15, null, null, 2,
          null, 13);
      OrderItem item3 = new OrderItem("Maciek", ingredient, 17, null, null, 3,
          null, 13);

      ArrayList<OrderItem> orderItems = new ArrayList<>(){{
        add(item1);
        add(item2);
        add(item3);
      }};

      PropertyChangeEvent evt = new PropertyChangeEvent(this, OrderModelCustomer.ORDER_RECEIVED, null, orderItems);
      listener.propertyChange(evt);
    }



    @Override public void cancelOrder()
    {
      System.out.println("cancelling order");
    }

    @Override public void addListener(String event,
        PropertyChangeListener listener)
    {
      this.listener = listener;
    }

    @Override public void addListener(PropertyChangeListener listener)
    {
      this.listener = listener;
    }
  }
}
