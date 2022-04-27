package server.databaseConn;

import shared.UserType;
import transferobjects.User;
import util.LogInException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class DatabaseConnImp implements DatabaseConn
{

  private String password;


  private String getPass()
  {
    File file = new File("Resources/DataBase_password.txt");
    try
    {
      Scanner scanner = new Scanner(file);
      password = scanner.nextLine();
      System.out.println(password);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }

    return password;
  }

  private Connection getConnection() throws SQLException
  {
      return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=caneat","postgres",getPass());

  }

  @Override public User login(String username, String password) throws LogInException,SQLException
  {

    try(Connection connection = getConnection())
    {
      for (UserType userType: UserType.values())
      {
        String str = "SELECT firstName, lastName, password FROM " + userType.toString().toLowerCase(
            Locale.ROOT) + " Where username = ?";

        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1,username);
        ResultSet set = statement.executeQuery();

        if(set.next())
        {
          String firstName = set.getString("firstName");
          String lastName = set.getString("lastName");
          String passwordFromDB = set.getString("password");

          if(!passwordFromDB.equals(password))
          {
            throw new LogInException("Password does not match");
          }

          return new User(username,userType,firstName,lastName);
        }
      }
    }
    throw new LogInException("User does not exist");
  }

  @Override public User register(String firstName, String lastName,
      String username, String password, UserType userType) throws SQLException
  {
    try(Connection connection= getConnection())
    {
      String str = "INSERT INTO " + userType.toString().toLowerCase(Locale.ROOT) + "(firstName, lastName,username, password) VALUES(?,?,?,?);";
        PreparedStatement statement = connection.prepareStatement(str);

        statement.setString(1,firstName);
        statement.setString(2,lastName);
        statement.setString(3,username);
        statement.setString(4,password);
        statement.executeUpdate();
        return new User(username,userType,firstName,lastName);

    }
  }
}
