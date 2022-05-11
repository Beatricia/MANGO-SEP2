package server.databaseConn;

import shared.Log;
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
      Log.log("UserDatabaseConn the user is added(registred) to the database");
      if(userType.equals(UserType.EMPLOYEE))
      {
        Log.log("UserDatabaseConn the employee user needs to be verified");
        throw new LogInException("Employee account needs to be verified");
      }
      else
      {
        Log.log("UserDatabaseConn a new User is received from the database");
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
        Log.log("UserDatabaseConn a new login(employee, customer, administrator) is received from the database ");

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
                Log.log("UserDatabaseConn employee account needs to be verified");
                throw new LogInException("Employee account needs to be verified");
              }
            }

            if (!passwordFromDB.equals(password))
            {
              Log.log("UserDatabaseConn user inserted a password that does not match");
              throw new LogInException("Password does not match");
            }

            Log.log("UserDatabaseConn a new user is created with database's information");
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
