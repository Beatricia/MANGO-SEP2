package server.databaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

class MenuDatabaseConn
{
  public void addItem(String name, ArrayList<String> ingredients, double price, String imgPath) throws
      SQLException
  {

    /*TODO Greg all of our tables are named with lowercase letters so you will have to change it from MenuItem to menuItem*/

    try (Connection connection = DatabaseConnImp.getConnection()) {
      String sql = "INSERT INTO MenuItem (name, price, imgPath) " +
          "VALUES (?, ?, ?)";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, name);
      statement.setDouble(2, price);
      statement.setString(3, imgPath);

      statement.executeUpdate();

      // insert tables????
    }
  }
}
