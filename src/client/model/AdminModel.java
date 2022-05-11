package client.model;

import transferobjects.User;
import util.PropertyChangeSubject;

import java.util.ArrayList;

public interface AdminModel extends PropertyChangeSubject
{

  void requestPendingEmployees();
  void acceptEmployee(User user);
  void declineEmployee(User user);
}
