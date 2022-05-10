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


  /**
   * Adds a Menu item to the database with the ingredients
   * @param name menu item name
   * @param ingredients ingredients
   * @param price price of the menu item
   * @param imgPath img path for the menu
   * @throws SQLException When the menu item name already exists.
   */
  public void addItem(String name, ArrayList<String> ingredients, double price, String imgPath) throws
      SQLException
  {

    try (Connection connection = DatabaseConnImp.getConnection()) {

      String ingredientsArray = "'{" + String.join(",", ingredients) + "}'";
      String sql = "SELECT addMenuItem(?, ?, ?, " + ingredientsArray + "::varchar[]);";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, name);
      statement.setDouble(2, price);
      statement.setString(3, imgPath);

      statement.executeQuery();
    }
  }
}
