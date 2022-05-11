package client.view.Admin.acceptEmployee;

import client.model.AdminModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import shared.Log;
import shared.UserType;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class AcceptEmployeeViewModel implements PropertyChangeListener
{
  private AdminModel adminModel;
  private ObservableList<User> users;
  private static String PENDING_USERS_REQUEST = "PendingUsersRequest";

  public AcceptEmployeeViewModel(AdminModel adminModel)
  {
    this.adminModel = adminModel;
    users = FXCollections.observableList(new ArrayList<>());
    adminModel.addListener(this);
  }

  public void handleUser(User user, boolean accept)
  {
    if (accept)     // accepts employee
    {
      adminModel.acceptEmployee(user);
      Log.log("AcceptEmployeeViewModel an accepted employee user is sent to the adminModel");
    }
    else            //declines employee
    {
      adminModel.declineEmployee(user);
      Log.log("AcceptEmployeeViewModel an declined employee user is sent to the adminModel");
    }
    users.remove(user);
  }

  public void refresh()
  {
   /* users.clear();

      ArrayList<User> pendingUsers = adminModel.requestPendingEmployees();

      for (User pendingUser: pendingUsers
           )
      {
        User user = new User(pendingUser.getUsername(), UserType.EMPLOYEE,
            pendingUser.getFirstName(), pendingUser.getLastName());

        users.add(user);
      }*/

    adminModel.requestPendingEmployees();

  }

  public ObservableList<User> getEmployeeList()
  {
    return users;
  }

  //man idk
  public void updateEmployeeList(PropertyChangeEvent e){
    if (e.getPropertyName().equals(PENDING_USERS_REQUEST)){
      users.clear();
      users.addAll((ArrayList<User>) e.getNewValue());
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Log.log("AcceptEmployeeViewModel the employee list is updating");
    updateEmployeeList(evt);
  }
}
