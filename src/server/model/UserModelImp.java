package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.LoginRequest;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;

/**
 * Class responsible for connecting the ServerHandler with DatabaseConn.
 * @author Mango
 * @version 1
 */
public class UserModelImp implements UserModel
{
  private DatabaseConn databaseConn;

  /**
   * Constructor where DatabaseConn is initialized.
   * @param databaseConn instance of DatabaseConn.
   */
  public UserModelImp(DatabaseConn databaseConn)
  {
    this.databaseConn = databaseConn;
  }


  /**
   * Sends a log-in request to the DatabaseConn.
   * @param request custom transfer object with information about registered user.
   */
  @Override public User login(LoginRequest request)
      throws SQLException, LogInException
  {
    return databaseConn.login(request.getUsername(), request.getPassword());
  }

  /**
   * Sends a register request to the DatabaseConn.
   * @param request custom transfer object with information about non-registered user.
   */
  @Override public User register(LoginRequest request) throws SQLException, LogInException
  {
    return databaseConn.register(request.getFirstName(), request.getLastName(),
        request.getUsername(), request.getPassword(), request.getUserType());
  }
}
