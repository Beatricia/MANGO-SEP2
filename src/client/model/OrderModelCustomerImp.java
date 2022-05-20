package client.model;

import client.networking.Client;
import shared.Log;
import transferobjects.Request;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class OrderModelCustomerImp implements OrderModelCustomer
{
  private Client client;
  private PropertyChangeSupport support;

  public OrderModelCustomerImp(Client client){
    this.client = client;
    support = new PropertyChangeSupport(this);

    client.addListener(Client.ORDER_RECEIVED, this::orderReceived);
  }

  private void orderReceived(PropertyChangeEvent evt) {
    Log.log("OrderModelCustomerImpl order received from client");
    support.firePropertyChange(ORDER_RECEIVED, null, evt.getNewValue());
  }

  @Override public void requestUncollectedOrder() {
    String username = UserModelImp.getUsername();
    Request request = new Request(Request.CUSTOMER_UNCOLLECTED_ORDER_REQUEST);
    request.setObject(username);

    client.sendRequest(request);
  }

  @Override public void cancelOrder() {
    if(1 == 1) throw new RuntimeException("Not implemented. Reason: no request object designed");

    String username = UserModelImp.getUsername();
    Request request = new Request("");
    request.setObject(username);

    client.sendRequest(request);
  }

  @Override public void addListener(String event, PropertyChangeListener listener) {
    support.addPropertyChangeListener(event, listener);
  }

  @Override public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
