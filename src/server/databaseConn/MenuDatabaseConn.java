package server.databaseConn;

import shared.Log;
import transferobjects.MenuItem;
import util.LogInException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

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
      Log.log("MenuDatabaseConn adds MenuItem to database");
    }
  }

  public void addDailyMenu(LocalDate date, ArrayList<MenuItem> menuItems) throws
      SQLException
  {
    try (Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "INSERT INTO dailyMenuItem (date, name) VALUES ";



      for (int i = 0; i < menuItems.size(); i++)
      {
        sql +=  String.format("('%s', '%s'),", date.toString(), menuItems.get(i).getName());
      }
      sql = sql.substring(0,sql.length()-1);

      Log.log("--------" + sql);
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.execute();
      Log.log("MenuDatabaseConn adds DailyMenuItem to database");

    }

  }

  public ArrayList<MenuItem> getListOfMenuItems() throws SQLException
  {
    ArrayList<MenuItem> menuItems = new ArrayList<>();

    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "SELECT * FROM menuItem";

      PreparedStatement statement = connection.prepareStatement(sql);

      ResultSet resultSet = statement.executeQuery();


      while (resultSet.next())
      {
        String name = resultSet.getString("name");
        double price  = resultSet.getDouble("price");

        menuItems.add(new MenuItem(name, null, price));
      }
    }

    Log.log("MenuDatabaseConn returns ListOfMenuItems to database");
    return menuItems;
  }
}
