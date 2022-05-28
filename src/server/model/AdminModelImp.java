package server.model;

import server.databaseConn.DatabaseConn;
import shared.Log;
import transferobjects.OrderItem;
import transferobjects.Statistics;
import transferobjects.User;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class responsible for connecting the networking part of the Server with Database connection.
 * @author Mango
 * @version 1
 */

public class AdminModelImp implements AdminModel
{
  private DatabaseConn databaseConn;

  /**
   * Constructor which is assigning database connection
   * @param databaseConn is an instance of the database connection which is connection the server with the database
   */
  public AdminModelImp(DatabaseConn databaseConn){
    this.databaseConn = databaseConn;
  }

  /**
   * Receiving a list of all employee-users waiting to be accepted
   * @return ArrayList of Users received from DatabaseConn
   */

  @Override public ArrayList<User> requestPendingEmployees() throws SQLException
  {
    return databaseConn.getAllPendingEmployees();
  }

  /**
   * Sends user object that should be accepted as an employee
   * @param user the User object that should be accepted
   */

  @Override public void acceptEmployee(User user) throws SQLException
  {
    databaseConn.handlePendingEmployee(user.getUsername(),true);
  }

  /**
   * Sends user object that should delete from users list
   * @param user the User object that should be declined
   */

  @Override public void declineEmployee(User user) throws SQLException
  {
    databaseConn.handlePendingEmployee(user.getUsername(),false);
  }


  /**
   * The method is used to send a username to the DatabaseConn so the employee
   * with that username can be removed from the system
   * @param username employee that has to be deleted
   */
  @Override public void removeEmployee(String username) throws SQLException {
    databaseConn.removeEmployee(username);
  }

  /**
   * The method connects to the DatabaseConn to get a list of all employee
   * accounts that have been accepted
   * @return list of accepted employees
   */
  @Override public ArrayList<User> requestAcceptedEmployees() throws SQLException {
    return databaseConn.getAcceptedEmployees();
  }

  /**
   * The method connects to DatabaseConn to set the opening hours of the canteen
   * @param openingHours the hours to be set as working
   */
  @Override public void setOpeningHours(ArrayList<LocalTime> openingHours) throws SQLException {
    Log.log("AdminModelImp: Sets opening hours");
    databaseConn.setOpeningHours(openingHours.get(0), openingHours.get(1));
  }

  /**
   * The method connects to DatabaseConn to get an ArrayList containing the
   * opening and closing time of the canteen
   * @return an ArrayList with the opening and closing time of the canteen
   */
  @Override public ArrayList<LocalTime> requestOpeningHours() throws SQLException {
    Log.log("AdminModelImp: request opening hours");
    return databaseConn.getOpeningHours();
  }

  @Override public ArrayList<ArrayList<OrderItem>> requestPurchaseHistory()
      throws SQLException
  {
    return databaseConn.requestPurchaseHistory();
  }

  @Override public Statistics requestStatistics() throws SQLException
  {
    return databaseConn.requestStatistic();
  }
}
