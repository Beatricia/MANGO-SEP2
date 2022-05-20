package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.OrderItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModelCustomerImpl implements OrderModelCustomer
{
  private DatabaseConn databaseConn;

  public OrderModelCustomerImpl(DatabaseConn databaseConn){
    this.databaseConn = databaseConn;
  }


  @Override public void cancelOrder(String username) throws SQLException {
    databaseConn.cancelOrder(username);
  }

  @Override public ArrayList<OrderItem> getUncollectedOrder(String username) throws SQLException {
    return databaseConn.getUncollectedOrder(username);
  }
}
