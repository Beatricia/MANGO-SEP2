package client.model;

import client.networking.Client;
import shared.Log;
import transferobjects.Request;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A class that connects the view model and the Client side of networking.
 * In this class the information about pending employees is passed through
 * as well as information about if admin did accept or decline one particular employee
 *
 * @author Simon
 */
public class AdminModelImp implements AdminModel, PropertyChangeListener
{

  private Client client;
  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
      this);

  /**
   * Constructor used for initializing and adding listeners
   *
   * @param client instance to pass information to server through
   */
  public AdminModelImp(Client client)
  {
    this.client = client;
    client.addListener(Client.PENDING_EMPLOYEES_RECEIVED,
        this::updatePendingEmployees);
  }

  /**
   * Method used for creating and sending a Request object to the database (to receive list of pending employees)
   */
  @Override public void requestPendingEmployees()
  {

    Request pendingEmployeesRequest = new Request(Request.PENDING_USER_REQUEST);

    Log.log("AdminModelImpl sends the PENDING_USER_REQUEST to the Client");
    client.sendRequest(pendingEmployeesRequest);

  }

  /**
   * This method is responsible for creating and sending a new Request object to the database (to accept an employee)
   *
   * @param user one particular User object to be accepted
   */
  @Override public void acceptEmployee(User user)
  {
    Request request = new Request(Request.EMPLOYEE_IS_ACCEPTED);
    request.setObject(user);

    Log.log(
        "AdminModelImpl sends an EMPLOYEE_IS_ACCEPTED object to the Client");
    client.sendRequest(request);
  }

  /**
   * This method is responsible for creating and sending a new Request object to the database (to decline an employee)
   *
   * @param user one particular User object to be declined
   */
  @Override public void declineEmployee(User user)
  {
    Request request = new Request(Request.EMPLOYEE_IS_DECLINED);
    request.setObject(user);

    Log.log(
        "AdminModelImpl sends an EMPLOYEE_IS_DECLINED object to the Client");
    client.sendRequest(request);
  }

  /**
   * This method is responsible for sending a PropertyChange event to the ViewModel
   *
   * @param e event caught from the client
   */
  @Override public void updatePendingEmployees(PropertyChangeEvent e)
  {

    Log.log("AdminModelImpl fires a PENDING_USER_REQUEST property");

    propertyChangeSupport.firePropertyChange(Request.PENDING_USER_REQUEST, -1,
        e.getNewValue());

  }

  /**
   * Adds a ChangeListener which will be notified whenever the value of the ObservableValue changes. If the same listener is added more than once, then it will be notified more than once. That is, no check is made to ensure uniqueness.
   * Note that the same actual ChangeListener instance may be safely registered for different ObservableValues.
   * The ObservableValue stores a strong reference to the listener which will prevent the listener from being garbage collected and may result in a memory leak. It is recommended to either unregister a listener by calling removeListener after use or to use an instance of WeakChangeListener avoid this situation.
   * @param event The event to listen to
   * @param listener The listener to register
   */
  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(event, listener);
    Log.log(
        listener + "has been added as a listener to " + this + " for " + event);
  }

  /**
   * Adds a ChangeListener which will be notified whenever the value of the ObservableValue changes. If the same listener is added more than once, then it will be notified more than once. That is, no check is made to ensure uniqueness.
   * Note that the same actual ChangeListener instance may be safely registered for different ObservableValues.
   * The ObservableValue stores a strong reference to the listener which will prevent the listener from being garbage collected and may result in a memory leak. It is recommended to either unregister a listener by calling removeListener after use or to use an instance of WeakChangeListener avoid this situation.
   * @param listener The listener to register
   */
  @Override public void addListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
    Log.log(listener + "has been added as a listener to " + this);
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
    updatePendingEmployees(evt);
  }
}
