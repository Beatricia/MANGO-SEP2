package server.model;

import transferobjects.LoginRequest;
import transferobjects.User;
import util.LogInException;

import java.sql.SQLException;

public interface UserModel
{
  User login(LoginRequest request) throws SQLException, LogInException;
  User register(LoginRequest request) throws SQLException;
}
