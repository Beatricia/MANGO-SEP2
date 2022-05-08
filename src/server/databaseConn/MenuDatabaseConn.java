package server.databaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

class MenuDatabaseConn
{
  //Table names
  private final static String MENU_ITEM_TABLE = "menuItem";
  private final static String INGREDIENTS_INPUT_TABLE = "ingredientInput";


  public void addItem(String name, ArrayList<String> ingredients, double price, String imgPath) throws
      SQLException
  {

    try (Connection connection = DatabaseConnImp.getConnection()) {
      String sql = "INSERT INTO " + MENU_ITEM_TABLE + " (name, price, imgPath) " +
          "VALUES (?, ?, ?);";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, name);
      statement.setDouble(2, price);
      statement.setString(3, imgPath);

      statement.executeUpdate();


      //Insert the ingredients
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("INSERT INTO " + INGREDIENTS_INPUT_TABLE + " (itemName, ingredientName) VALUES ");

      for (String ingredient : ingredients){
        String ingredientString = String.format("('%s', '%s'),", name, ingredient);
        stringBuilder.append(ingredientString);
      }
      //Remove the last comma from the sql code
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);

      sql = stringBuilder.toString();
      statement = connection.prepareStatement(sql);

      statement.executeUpdate();
    }
  }
}
