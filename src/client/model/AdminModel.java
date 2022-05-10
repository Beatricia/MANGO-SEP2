package client.model;

import transferobjects.User;

import java.util.ArrayList;

public interface AdminModel
{

  ArrayList<User> requestPendingEmployees();
  void acceptEmployee(User user);
  void declineEmployee(User user);
}
