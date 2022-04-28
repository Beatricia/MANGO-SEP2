package server.model;

import transferobjects.LoginRequest;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;

/**
 * Interface responsible for connecting the ServerHandler with DatabaseConn.
 * @author Mango
 * @version 1
 */
public interface UserModel
{
  /**
   * Sends a log-in request to the DatabaseConn.
   * @param request custom transfer object with information about registered user.
   */
  User login(LoginRequest request) throws SQLException, LogInException;

  /**
   * Sends a register request to the DatabaseConn.
   * @param request custom transfer object with information about non-registered user.
   */
  User register(LoginRequest request) throws SQLException, LogInException;
}
