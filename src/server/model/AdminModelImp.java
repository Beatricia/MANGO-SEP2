package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.User;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class responsible for connecting the networking part of the Server with Database connection.
 * @author Simon
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
   * @return ArrayList of Users received from Databaseconn
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

  @Override public void removeEmployee(String username)
  {
    databaseConn.removeEmployee(username);
  }

  @Override public ArrayList<User> requestAcceptedEmployees()
  {
    return databaseConn.getAcceptedEmployees();
  }

  @Override public void setOpeningHours(ArrayList<LocalTime> openingHours)
  {
    databaseConn.setOpeningHours(openingHours.get(0), openingHours.get(1));
  }

  @Override public ArrayList<LocalTime> requestOpeningHours()
  {
    return databaseConn.getOpeningHours();
  }
}
