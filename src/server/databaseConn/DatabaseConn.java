package server.databaseConn;

import shared.UserType;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;

public interface DatabaseConn
{
  User login(String username, String password) throws SQLException,
      LogInException;
  User register(String firstName, String lastName, String username, String password, UserType userType) throws
      SQLException;
}
