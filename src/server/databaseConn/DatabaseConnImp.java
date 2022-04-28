package server.databaseConn;

import shared.UserType;
import transferobjects.User;
import util.LogInException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * A concrete implementation of the {@link DatabaseConn}.
 * @author Mango
 * @version 1
 */
public class DatabaseConnImp implements DatabaseConn
{

  /**
   * Password to the SQL Server.
   */
  private String password;

  /**
   * Returns the password if it is loaded, else it loads the password from the file.
   * @return The password to access the database.
   */
  private String getPass() {
    if (password != null)
      return password;

    File file = new File("Resources/DataBase_password.txt");
    try {
      Scanner scanner = new Scanner(file);
      password = scanner.nextLine();
      System.out.println(password);
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return password;
  }

  /**
   * Get database connection for the sql server.
   * @return A connection object to access the database.
   * @throws SQLException When an unexpected exception happens.
   */
  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=caneat", "postgres", getPass());

  }

  /**
   * Log in with the specified username and the password.
   * @param username username
   * @param password password
   * @return A user object representing the logged-in user if the log-in was successful
   * @throws SQLException When an unexpected sql exception happens
   * @throws LogInException When the user has not provided the correct data
   */
  @Override public User login(String username, String password)
      throws LogInException, SQLException {

    try (Connection connection = getConnection()) {
      for (UserType userType : UserType.values()) {
        String table = userType.toString().toLowerCase(Locale.ROOT);
        String str = "SELECT firstname, lastname, password FROM " + table + " Where username = ?";

        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1, username);
        ResultSet set = statement.executeQuery();

        if (set.next()) {
          String firstName = set.getString("firstName");
          String lastName = set.getString("lastName");
          String passwordFromDB = set.getString("password");

          if (!passwordFromDB.equals(password)) {
            throw new LogInException("Password does not match");
          }

          return new User(username, userType, firstName, lastName);
        }
      }
    }
    throw new LogInException("User does not exist");
  }

  /**
   * Register a user with the specified username, password, first name and last name.
   * @param firstName first name
   * @param lastName last name
   * @param username username
   * @param password password
   * @param userType type of the user
   * @return A user object representing the registered user if the register was successful
   * @throws SQLException When the user has not provided the correct data
   */
  @Override public User register(String firstName, String lastName, String username,
      String password, UserType userType) throws SQLException {
    try (Connection connection = getConnection()) {
      String str = "INSERT INTO " + userType.toString().toLowerCase(Locale.ROOT)
          + "(firstName, lastName,username, password) VALUES(?,?,?,?);";
      PreparedStatement statement = connection.prepareStatement(str);

      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setString(3, username);
      statement.setString(4, password);
      statement.executeUpdate();
      return new User(username, userType, firstName, lastName);
    }
  }
}
