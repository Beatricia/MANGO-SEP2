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

  @Override public ArrayList<User> requestPendingEmployees() throws SQLException
  {
    return databaseConn.getAllPendingEmployees();
  }

  @Override public void acceptEmployee(User user) throws SQLException
  {
    databaseConn.handlePendingEmployee(user.getUsername(),user.getIsAccepted());
  }

  @Override public void declineEmployee(User user) throws SQLException
  {
    databaseConn.handlePendingEmployee(user.getUsername(),user.getIsAccepted());
  }
}
