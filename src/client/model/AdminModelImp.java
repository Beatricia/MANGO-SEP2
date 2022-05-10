package client.model;

import client.networking.Client;
import transferobjects.Request;
import transferobjects.User;

import java.util.ArrayList;

public class AdminModelImp implements AdminModel
{

  private Client client;
  private static String PENDING_USERS_REQUEST = "PendingUsersRequest";


  public AdminModelImp(Client client){
    this.client = client;
  }

  @Override public ArrayList<User> requestPendingEmployees()
  {
    Request pendingEmployeesRequest = new Request(PENDING_USERS_REQUEST);

    client.sendRequest(pendingEmployeesRequest);

    return null;
  }

  @Override public void acceptEmployee(User user)
  {
    client.acceptEmployee(user);
  }

  @Override public void declineEmployee(User user)
  {
    client.declineEmployee(user);
  }
}
