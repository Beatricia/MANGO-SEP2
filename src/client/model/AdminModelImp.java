package client.model;

import client.networking.Client;
import shared.Log;
import transferobjects.Request;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A class that connects the view model and the Client side of networking.
 * In this class the information about pending employees is passed through
 * as well as information about if admin did accept or decline one particular employee
 *
 * @author Mango
 */
public class AdminModelImp implements AdminModel
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
    client.addListener(Client.ACCEPTED_EMPLOYEES_RECEIVED, this::sendAcceptedEmployees);
    client.addListener(Client.OPENING_HOURS_RECEIVED, this::sendOpeningHours);
  }

  /**
   * The method fires an OPENING_HOURS_RECEIVED event to its listeners
   * @param event the event that occurred in the Client
   */
  private void sendOpeningHours(PropertyChangeEvent event)
  {
    Log.log("AdminModelImp fires an OPENING_HOURS_RECEIVED to the view model");
    propertyChangeSupport.firePropertyChange(OPENING_HOURS_RECEIVED, null, event.getNewValue());
  }

  /**
   * Method used for creating and sending a Request object to the database
   * (to receive list of pending employees)
   */
  @Override public void requestPendingEmployees()
  {

    Request pendingEmployeesRequest = new Request(Request.PENDING_USER_REQUEST);

    Log.log("AdminModelImpl sends the PENDING_USER_REQUEST to the Client");
    client.sendRequest(pendingEmployeesRequest);

  }

  /**
   * This method is responsible for creating and sending a new Request
   * object to the database (to accept an employee)
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
   * The method sends a OPENING_HOURS_REQUEST request object to the Client
   * to get the opening hours.
   */
  @Override
  public void requestOpeningHours()
  {
    Request request = new Request(Request.OPENING_HOURS_REQUEST);

    Log.log("AdminModelImpl sends an OPENING_HOURS_REQUEST object to the Client");
    client.sendRequest(request);
  }

  /**
   * The method sends a REMOVE_EMPLOYEE_REQUEST request to the client to remove
   * a specified employee
   * @param username the username of the employee to be removed
   */
  @Override
  public void removeEmployee(String username)
  {
    Request request = new Request(Request.REMOVE_EMPLOYEE_REQUEST);
    request.setObject(username);

    Log.log("AdminModelImpl sends an REMOVE_EMPLOYEE_REQUEST object to the Client");
    client.sendRequest(request);
  }

  /**
   * The method sends a ALL_ACCEPTED_EMPLOYEES_REQUEST request object to the
   * Client to get all the accepted employees
   */
  @Override
  public void requestAcceptedEmployees()
  {
    Request request = new Request(Request.ALL_ACCEPTED_EMPLOYEES_REQUEST);

    Log.log("AdminModelImpl sends an ALL_ACCEPTED_EMPLOYEES_REQUEST object to the Client");
    client.sendRequest(request);
  }

  /**
   * The method creates an ArrayList to hold the two LocalTime values and
   * send it to the client in a SET_OPENING_HOURS_REQUEST request object
   * @param from from when is it open
   * @param to til when is it open
   */
  @Override
  public void setOpeningHours(LocalTime from, LocalTime to)
  {
    ArrayList<LocalTime> openingHours = new ArrayList<>();
    openingHours.add(from);
    openingHours.add(to);

    Request request = new Request(Request.SET_OPENING_HOURS_REQUEST);
    request.setObject(openingHours);

    Log.log("AdminModelImpl sends an SET_OPENING_HOURS_REQUEST object to the Client");
    client.sendRequest(request);
  }

  /**
   * This method is responsible for sending a PropertyChange event to the ViewModel
   *
   * @param e event caught from the client
   */
  private void updatePendingEmployees(PropertyChangeEvent e)
  {

    Log.log("AdminModelImpl fires a PENDING_USER_REQUEST property");

    propertyChangeSupport.firePropertyChange(PENDING_EMPLOYEES_RECEIVED, -1,
        e.getNewValue());

  }

  /**
   * The method fires an ACCEPTED_EMPLOYEES_RECEIVED event
   * @param event the event received
   */
  private void sendAcceptedEmployees(PropertyChangeEvent event)
  {
    Log.log("AdminModelImpl fires an ACCEPTED_EMPLOYEES_RECEIVED");
    propertyChangeSupport.firePropertyChange(ACCEPTED_EMPLOYEES_RECEIVED, null, event.getNewValue());
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
}
