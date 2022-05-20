package client.model;

import client.networking.Client;

import java.beans.PropertyChangeListener;

public class OrderModelCustomerImp implements OrderModelCustomer
{
private Client client;

public OrderModelCustomerImp(Client client)
{
  this.client = client;
}

  @Override public void requestUncollectedOrder() {

  }

  @Override public void cancelOrder() {

  }

  @Override public void addListener(String event, PropertyChangeListener listener) {

  }

  @Override public void addListener(PropertyChangeListener listener) {

  }
}
