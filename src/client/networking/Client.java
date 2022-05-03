package client.networking;

import transferobjects.LoginRequest;
import transferobjects.MenuItem;
import util.PropertyChangeSubject;

// An interface that holds two variables that will be called in the socketClient for firing events
// It will handle the login and the register action
// It takes a loginRequest object as a parameter for both register and login
public interface Client extends PropertyChangeSubject
{
  String ERROR_RECEIVED = "ErrorReceived";
  String LOGGED_IN_RECEIVED = "LogInReceived";

  /**
   * The method is used to sent to the Server the LoginRequest object
   *    when a person loggs in it is called
   * @param request the LoginRequest to be sent
   */
  void login(LoginRequest request);
  void register(LoginRequest request);
  void addItem(MenuItem menuItem);
}
