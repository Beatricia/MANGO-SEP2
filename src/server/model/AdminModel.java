package server.model;

import transferobjects.OrderItem;
import transferobjects.Statistics;
import transferobjects.User;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Interface responsible for connecting the networking part of the Server with Database connection.
 * @author Mango
 * @version 1
 */

public interface AdminModel
{
  /**
   * Retrieves the list of pending employees from DatabaseConn
   * @return list off User objects
   * @throws SQLException
   */
  ArrayList<User> requestPendingEmployees() throws SQLException;
  /**
   * The method is used to send User object that should be accepted as an employee to DatabaseConn
   * @param user the User object to send to DatabaseConn
   * @throws SQLException
   */
  void acceptEmployee(User user) throws SQLException;
  /**
   * The method is used to send User object that should be deleted from employee list to DatabaseConn
   * @param user the User object to send to DatabaseConn
   * @throws SQLException
   */
  void declineEmployee(User user) throws SQLException;


  /**
   * The method is used to send a username to the DatabaseConn so the employee
   * with that username can be removed from the system
   * @param username employee that has to be deleted
   */
  void removeEmployee(String username) throws SQLException;

  /**
   * The method connects to the DatabaseConn to get a list of all employee
   * accounts that have been accepted
   * @return list of accepted employees
   */
  ArrayList<User> requestAcceptedEmployees() throws SQLException;

  /**
   * The method connects to DatabaseConn to set the opening hours of the canteen
   * @param openingHours the hours to be set as working
   */
  void setOpeningHours(ArrayList<LocalTime> openingHours) throws SQLException;

  /**
   * The method connects to DatabaseConn to get an ArrayList containing the
   * opening and closing time of the canteen
   * @return an ArrayList with the opening and closing time of the canteen
   */
  ArrayList<LocalTime> requestOpeningHours() throws SQLException;

  ArrayList<ArrayList<OrderItem>> requestPurchaseHistory() throws SQLException;

  Statistics requestStatistics() throws SQLException;
}
