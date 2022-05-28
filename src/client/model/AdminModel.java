package client.model;

import transferobjects.User;
import util.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * An interface which is responsible for handling the connection between the
 * ViewModels and the Networking
 * @author Mango
 */
public interface AdminModel extends PropertyChangeSubject
{

  String  PENDING_EMPLOYEES_RECEIVED = "PendingEmployeesReceived";
  String ACCEPTED_EMPLOYEES_RECEIVED= "AcceptedEmployeesReceived";
  String OPENING_HOURS_RECEIVED = "OpeningHoursReceived";
  String PURCHASE_HISTORY_RECEIVED = "PurchaseHistoryReceived";
  String STATISTICS_RECEIVED = "StatisticsReceived";

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

  /**
   * This method is responsible for requesting the hours which have been set as
   * working hours for the canteen
   */
  void requestOpeningHours();

  /**
   * This method is responsible for removing an employee from the system
   * @param username the username of the employee to be removed
   */
  void removeEmployee(String username);

  /**
   * This method is responsible for requesting a list of all accepted employees
   */
  void requestAcceptedEmployees();

  /**
   * This method is responsible for setting the hours in which the canteen is
   * working
   * @param from from when is it open
   * @param to til when is it open
   */
  void setOpeningHours(LocalTime from, LocalTime to);

  /**
   * Request purchase history data.
   */
  void requestPurchaseHistory();

  /**
   * Request statistics of the canteen.
   */
  void requestStatistics();
}
