package client.view.admin.AcceptEmployee;

import client.model.AdminModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * A class that connects the view controller with the model
 * In this class the information about pending employees is passed through
 * as well as information about if admin did accept or decline one particular employee
 *
 * @author Simon
 */
public class AcceptEmployeeViewModel implements PropertyChangeListener
{
  private AdminModel adminModel;
  private ObservableList<User> users;


  /**
   * Constructor used for initializing and adding listeners
   *
   * @param adminModel To send information about requests from the GUI to
   */
  public AcceptEmployeeViewModel(AdminModel adminModel)
  {
    this.adminModel = adminModel;
    users = FXCollections.observableList(new ArrayList<>());
    adminModel.addListener(AdminModel.PENDING_EMPLOYEES_RECEIVED,this);
  }

  /**
   * This method is responsible for handling different scenarios that could happen with employee
   * depending on whether the admin accepts or declines the employee
   *
   * @param user User to be handled (accept or decline)
   * @param accept information about whether employee was accepted or declined
   */
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

  /**
   * This method is responsible for sending a request to update the <b>users</b> list
   */
  public void refresh()
  {
    adminModel.requestPendingEmployees();
  }

  /**
   * Method that returns the <b>users</b> list
   * @return list of User type objects
   */
  public ObservableList<User> getEmployeeList()
  {
    return users;
  }

  /**
   * This method is responsible for updating the current <b>users</b> list
   * first clears the whole list and then inserts the one received from the caught event
   *
   * @param e event caught from the admin
   */
  public void updateEmployeeList(PropertyChangeEvent e){
    if (e.getPropertyName().equals(AdminModel.PENDING_EMPLOYEES_RECEIVED)){
      users.clear();
      users.addAll((ArrayList<User>)e.getNewValue());
    }
  }

  /**
   * A "PropertyChange" event gets delivered whenever a bean changes a "bound" or "constrained" property. A PropertyChangeEvent object is sent as an argument to the PropertyChangeListener and VetoableChangeListener methods.
   * Normally PropertyChangeEvents are accompanied by the name and the old and new value of the changed property. If the new value is a primitive type (such as int or boolean) it must be wrapped as the corresponding java.lang.* Object type (such as Integer or Boolean).
   *
   * Null values may be provided for the old and the new values if their true values are not known.
   *
   * An event source may send a null object as the name to indicate that an arbitrary set of if its properties have changed. In this case the old and new values should also be null.
   * @param evt Event to be caught
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Log.log("AcceptEmployeeViewModel the employee list is updating");
    updateEmployeeList(evt);
  }
}
