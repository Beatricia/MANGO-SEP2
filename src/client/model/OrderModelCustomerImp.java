package client.model;

import client.networking.Client;
import shared.Log;
import transferobjects.Request;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * An interface which is responsible for handling the connection between the
 * ViewModels and the Networking
 * In this class the information about order is passed through
 */

public class OrderModelCustomerImp implements OrderModelCustomer
{
  private Client client;
  private PropertyChangeSupport support;

  /**
   * Constructor used for initializing and adding listeners
   *
   * @param client instance to pass information to server through
   */

  public OrderModelCustomerImp(Client client){
    this.client = client;
    support = new PropertyChangeSupport(this);

    client.addListener(Client.ORDER_RECEIVED, this::orderReceived);
    client.addListener(Client.ALL_UNCOLLECTED_ORDERS_RECEIVED, this::allUncollectedOrders);
  }

  /**
   * Fires an event with the list of all uncollected orders to the ViewModel
   * @param event the event fired by the client
   */

  private void allUncollectedOrders(PropertyChangeEvent event)
  {
    Log.log("OrderModelCustomerImp: All uncollected orders received");
    support.firePropertyChange(ALL_UNCOLLECTED_ORDERS_RECEIVED, null,event.getNewValue());
  }

  /**
   * Fires an event with the specific customer's order to the ViewModel
   * @param evt the event fired by the client
   */

  private void orderReceived(PropertyChangeEvent evt) {
    Log.log("OrderModelCustomerImpl order received from client");
    support.firePropertyChange(ORDER_RECEIVED, null, evt.getNewValue());
  }

  /**
   * The method creates request object with username
   * and sends this object to the client
   */

  @Override public void requestUncollectedOrder() {
    String username = UserModelImp.getUsername();
    Request request = new Request(Request.CUSTOMER_UNCOLLECTED_ORDER_REQUEST);
    request.setObject(username);

    client.sendRequest(request);
  }

  /**
   * The method creates request object with username
   * and sends this object to the client
   */

  @Override public void cancelOrder() {
    String username = UserModelImp.getUsername();
    Request request = new Request(Request.CANCEL_ORDER);
    request.setObject(username);

    client.sendRequest(request);
  }

  /**
   * The method creates request object with order's code
   * and sends this object to the client
   */

  @Override public void collectOrder(int orderCode)
  {
    Request request = new Request(Request.COLLECT_ORDER_REQUEST);
    request.setObject(orderCode);

    client.sendRequest(request);
  }

  /**
   * Creating a request for the list of all uncollected orders, which is sent to the client
   */

  @Override public void requestAllUncollectedOrders()
  {
    Request request = new Request(Request.ALL_UNCOLLECTED_ORDERS_REQUEST);
    client.sendRequest(request);
  }

  @Override public void addListener(String event, PropertyChangeListener listener) {
    support.addPropertyChangeListener(event, listener);
  }

  @Override public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
