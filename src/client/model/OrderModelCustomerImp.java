package client.model;

import client.networking.Client;

public class OrderModelCustomerImp implements OrderModelCustomer
{
private Client client;

public OrderModelCustomerImp(Client client)
{
  this.client = client;
}
}
