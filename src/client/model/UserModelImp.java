package client.model;

import client.networking.Client;
import shared.Log;
import shared.UserType;
import transferobjects.LoginRequest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A class that implements the UserModel interface. Used to connect the
 * ViewModel objects with the networking.
 * @author Uafa
 */

public class UserModelImp implements UserModel
{
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

  private void sendLogin(PropertyChangeEvent event)
  {
    support.firePropertyChange(LOGGED_IN_RECEIVED, null, event.getNewValue());

    Log.log("UserModelImp fires a LOGGED_IN_RECEIVED event");
  }

  private void sendError(PropertyChangeEvent event){
    support.firePropertyChange(ERROR_RECEIVED, null, event.getNewValue());
    Log.log("UserModelImp fires a ERROR_RECEIVED event");
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
