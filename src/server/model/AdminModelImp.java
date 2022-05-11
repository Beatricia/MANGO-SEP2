package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.User;

import java.sql.SQLException;
import java.util.ArrayList;

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
   * Sends user object which should be accepted as an employee to databaseconn
   * @param user with true isAccepted parameter
   */

  @Override public void acceptEmployee(User user) throws SQLException
  {
    databaseConn.handlePendingEmployee(user.getUsername(),user.getIsAccepted());
  }

  /**
   * Sends user object which should delete from users list in
   * @param user with false isAccepted parameter
   */

  @Override public void declineEmployee(User user) throws SQLException
  {
    databaseConn.handlePendingEmployee(user.getUsername(),user.getIsAccepted());
  }
}
