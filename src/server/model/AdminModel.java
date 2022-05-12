package server.model;

import transferobjects.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface responsible for connecting the networking part of the Server with Database connection.
 * @author Agata
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
}
