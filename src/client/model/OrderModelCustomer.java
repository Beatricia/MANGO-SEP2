package client.model;

import util.PropertyChangeSubject;

public interface OrderModelCustomer extends PropertyChangeSubject
{
  String ORDER_RECEIVED = "";

  void requestUncollectedOrder();

  void cancelOrder();
}
