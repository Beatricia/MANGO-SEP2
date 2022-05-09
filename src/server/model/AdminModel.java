package server.model;

import transferobjects.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AdminModel
{
  ArrayList<User> requestPendingEmployees() throws SQLException;
  void acceptEmployee(User user) throws SQLException;
  void declineEmployee(User user) throws SQLException;
}
