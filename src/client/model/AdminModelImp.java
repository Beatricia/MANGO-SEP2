package client.model;

import client.networking.Client;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import shared.Log;
import transferobjects.Request;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class AdminModelImp implements AdminModel, PropertyChangeListener
{

  private Client client;
  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


  public AdminModelImp(Client client){
    this.client = client;
    client.addListener(Client.PENDING_EMPLOYEES_RECEIVED, this::updatePendingEmployees);
  }

  @Override public void requestPendingEmployees()
  {

    Request pendingEmployeesRequest = new Request(Request.PENDING_USER_REQUEST);

    Log.log("AdminModelImpl sends the PENDING_USER_REQUEST to the Client");
    client.sendRequest(pendingEmployeesRequest);


  }

  public void updatePendingEmployees(PropertyChangeEvent e){

    Log.log("AdminModelImpl fires a PENDING_USER_REQUEST property");

    propertyChangeSupport.firePropertyChange(Request.PENDING_USER_REQUEST, -1, e.getNewValue());

  }

  @Override public void acceptEmployee(User user)
  {
    Request request = new Request(Request.EMPLOYEE_IS_ACCEPTED);
    request.setObject(user);

    Log.log("AdminModelImpl sends an EMPLOYEE_IS_ACCEPTED object to the Client");
    client.sendRequest(request);
  }

  @Override public void declineEmployee(User user)
  {
    Request request = new Request(Request.EMPLOYEE_IS_DECLINED);
    request.setObject(user);

    Log.log("AdminModelImpl sends an EMPLOYEE_IS_DECLINED object to the Client");
    client.sendRequest(request);
  }

  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(event,listener);
    Log.log(listener + "has been added as a listener to " + this + " for " + event);
  }

  @Override public void addListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
    Log.log(listener + "has been added as a listener to " + this);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    updatePendingEmployees(evt);
  }
}
