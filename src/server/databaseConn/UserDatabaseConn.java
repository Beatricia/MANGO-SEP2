package server.databaseConn;

import shared.UserType;
import transferobjects.User;
import util.LogInException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

class UserDatabaseConn
{
  public User register(String firstName, String lastName, String username,
      String password, UserType userType) throws SQLException, LogInException
  {
    try (Connection connection = DatabaseConnImp.getConnection())
    {
      String str = "INSERT INTO " + userType.toString().toLowerCase(Locale.ROOT)
          + "(firstName, lastName,username, password) VALUES(?,?,?,?);";
      PreparedStatement statement = connection.prepareStatement(str);

      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setString(3, username);
      statement.setString(4, password);
      statement.executeUpdate();
      if(userType.equals(UserType.EMPLOYEE))
      {
        throw new LogInException("Employee account needs to be verified");
      }
      else
      {
        return new User(username, userType, firstName, lastName);
      }
    }
    catch (Throwable e)
    {
      if (e.getMessage().contains("Username not unique"))
        throw new LogInException("Username not unique");
      else
        throw e;
    }
  }

  public User login(String username, String password)
      throws LogInException, SQLException
  {

    try (Connection connection = DatabaseConnImp.getConnection())
    {
      for (UserType userType : UserType.values())
      {

        String table = userType.toString().toLowerCase(Locale.ROOT);
        String str = "SELECT firstname, lastname, password";


        if(userType.equals(UserType.EMPLOYEE))
        {
          str +=", accepted ";
        }
        str += "FROM " + table + " Where username = ?";


        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1, username);
        System.out.println(str);
        ResultSet set = statement.executeQuery();

        try
        {
          if (set.next())
          {
            String firstName = set.getString("firstName");
            String lastName = set.getString("lastName");
            String passwordFromDB = set.getString("password");

            if(userType.equals(UserType.EMPLOYEE))
            {
              boolean accepted = set.getBoolean("accepted");

              if(!accepted)
              {
                throw new LogInException("Employee account needs to be verified");
              }
            }

            if (!passwordFromDB.equals(password))
            {
              throw new LogInException("Password does not match");
            }

            return new User(username, userType, firstName, lastName);
          }
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    catch (Throwable e)
    {
      if (e.getMessage().contains("Username not unique"))
        throw new LogInException("Username not unique");
      else
        throw e;
    }
    throw new LogInException("User does not exist");
  }
}
