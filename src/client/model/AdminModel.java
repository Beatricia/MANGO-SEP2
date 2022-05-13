package client.model;

import transferobjects.User;
import util.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * An interface which is responsible for handling the connection between the
 * ViewModels and the Networking
 * @author Simon
 */
public interface AdminModel extends PropertyChangeSubject
{

  /**
   * Method used for creating and sending a Request object to the database (to receive list of pending employees)
   */
  void requestPendingEmployees();

  /**
   * This method is responsible for creating and sending a new Request object to the database (to accept an employee)
   *
   * @param user one particular User object to be accepted
   */
  void acceptEmployee(User user);

  /**
   * This method is responsible for creating and sending a new Request object to the database (to decline an employee)
   *
   * @param user one particular User object to be declined
   */
  void declineEmployee(User user);
}
