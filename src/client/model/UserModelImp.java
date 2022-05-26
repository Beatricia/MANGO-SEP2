package client.model;

import client.networking.Client;
import shared.Log;
import shared.UserType;
import transferobjects.LoginRequest;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;



/**
 * A class that implements the UserModel interface. Used to connect the
 * ViewModel objects with the networking.
 * @author Mango
 * @version 1
 */

public class UserModelImp implements UserModel
{
  private static User loggedInUser = null;


  private PropertyChangeSupport support;
  private Client client;

  /**
   * Constructor for the UserModelImp class. Instantiates a
   * PropertyChangeSupport object and adds itself as a listener to the client
   * (listens for a 'LOGGED_IN_RECEIVED' and handles it with a private method.
   * @param client takes a Client object
   */
  public UserModelImp(Client client)
  {
    this.client = client;
    support = new PropertyChangeSupport(this);

    client.addListener(LOGGED_IN_RECEIVED, this:: sendLogin);
    client.addListener(ERROR_RECEIVED, this::sendError);
  }

  /**
   * A method used to get the username of the user
   * @return the username of the user
   */
  public static String getUsername() {
    return loggedInUser.getUsername();
  }

  /**
   * The method fires a LOGGED_IN_RECEIVED event with a User object
   * @param event the event received
   */
  private void sendLogin(PropertyChangeEvent event)
  {
    User user = (User) event.getNewValue();
    loggedInUser = user;

    Log.log("UserModelImp fires a LOGGED_IN_RECEIVED event");
    support.firePropertyChange(LOGGED_IN_RECEIVED, null, user);
  }

  /**
   * The method fires an ERROR_RECEIVED event with the body of the error message
   * @param event the event received
   */
  private void sendError(PropertyChangeEvent event){
    Log.log("UserModelImp fires a ERROR_RECEIVED event");
    support.firePropertyChange(ERROR_RECEIVED, null, event.getNewValue());
  }

  /**
   * The method is used to send a LoginRequest object to the client
   * @param username the username that the user has provided
   * @param password the password that the user has provided
   */
  @Override public void login(String username, String password)
  {
    client.login(new LoginRequest(username,password));

    Log.log("UserModelImpl sends a new LoginRequest object to the Client (login method)");
  }

  /**
   * The method is used to send a LoginRequest to the client.
   * @param firstName the first name that the user has provided
   * @param lastName the last name that the user has provided
   * @param username the username that the user has provided
   * @param password the password that the user has provided
   * @param userType the userType that the user has selected
   */
  @Override public void register(String firstName, String lastName,
      String username, String password, UserType userType)
  {
    client.register(new LoginRequest(firstName,lastName,username,password,userType));

    Log.log("UserModelImpl sends a new LoginRequest object to the Client (register method)");
  }

  /**
   * Using the PropertyChangeSubject object adds a listener for a specific event
   * @param event  event to be listened
   * @param listener listener to be added
   */
  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(event,listener);
    Log.log(listener + "has been added as a listener to " + this + " for " + event);
  }

  /**
   * Using the PropertyChangeSubject object adds a listener for all types of
   * events
   * @param listener listener to be added
   */

  @Override public void addListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
    Log.log(listener + "has been added as a listener to " + this);
  }
}
