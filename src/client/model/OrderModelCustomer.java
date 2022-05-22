package client.model;

import util.PropertyChangeSubject;

//TODO javadocs

public interface OrderModelCustomer extends PropertyChangeSubject
{
  String ORDER_RECEIVED = "OrderReceived";

  void requestUncollectedOrder();

  void cancelOrder();
}
