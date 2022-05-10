package client.view.Admin.acceptEmployee;

import client.model.AdminModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.UserType;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class AcceptEmployeeViewModel
{
  private AdminModel adminModel;
  private ObservableList<User> users;

  public AcceptEmployeeViewModel(AdminModel adminModel)
  {
    this.adminModel = adminModel;
    users = FXCollections.observableList(new ArrayList<>());
  }

  public void handleUser(User user, boolean accept)
  {
    if (accept)     // accepts employee
    {
      adminModel.acceptEmployee(user);
    }
    else            //declines employee
    {
      adminModel.declineEmployee(user);
    }
    users.remove(user);
  }

  public void refresh()
  {
    users.clear();

      ArrayList<User> pendingUsers = adminModel.requestPendingEmployees();

      for (User pendingUser: pendingUsers
           )
      {
        User user = new User(pendingUser.getUsername(), UserType.EMPLOYEE,
            pendingUser.getFirstName(), pendingUser.getLastName());

        users.add(user);
      }

  }

  public ObservableList<User> getEmployeeList()
  {
    return users;
  }
}
