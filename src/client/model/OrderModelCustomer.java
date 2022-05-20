package client.model;

import util.PropertyChangeSubject;

public interface OrderModelCustomer extends PropertyChangeSubject
{
  String ORDER_RECEIVED = "OrderReceived";

  void requestUncollectedOrder();

  void cancelOrder();
}
