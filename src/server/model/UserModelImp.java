package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.LoginRequest;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;

public class UserModelImp implements UserModel
{
  private DatabaseConn databaseConn;

  public UserModelImp(DatabaseConn databaseConn)
  {
    this.databaseConn = databaseConn;
  }


  @Override public User login(LoginRequest request)
      throws SQLException, LogInException
  {
    return databaseConn.login(request.getUsername(), request.getPassword());
  }

  @Override public User register(LoginRequest request) throws SQLException
  {
    return databaseConn.register(request.getFirstName(), request.getLastName(),
        request.getUsername(), request.getPassword(), request.getUserType());
  }
}
