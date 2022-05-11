package client.model;

import client.networking.Client;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import transferobjects.Request;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class AdminModelImp implements AdminModel, PropertyChangeListener
{

  private Client client;
  private static String PENDING_USERS_REQUEST = "PendingUsersRequest";
  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


  public AdminModelImp(Client client){
    this.client = client;
  }

  @Override public void requestPendingEmployees()
  {
    Request pendingEmployeesRequest = new Request(PENDING_USERS_REQUEST);

    client.sendRequest(pendingEmployeesRequest);

    client.addListener(this);
  }

  public void updatePendingEmployees(PropertyChangeEvent e){
    propertyChangeSupport.firePropertyChange(PENDING_USERS_REQUEST, -1, e.getNewValue());

  }

  @Override public void acceptEmployee(User user)
  {
    client.acceptEmployee(user);
  }

  @Override public void declineEmployee(User user)
  {
    client.declineEmployee(user);
  }

  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(event,listener);
  }

  @Override public void addListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    updatePendingEmployees(evt);
  }
}
