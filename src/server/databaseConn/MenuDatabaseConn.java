package server.databaseConn;

import shared.Log;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

//TODO javadocs

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

    Log.log("MenuDatabaseConn adds MenuItem to database");

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

  /**
   * Add a list of MenuItemWithQuantity to their assigned days.
   * @param menuItems menu items to be added to the daily menu
   * @throws SQLException on unexpected exception
   */
  public void addDailyMenu(ArrayList<MenuItemWithQuantity> menuItems) throws
      SQLException
  {
    Log.log("MenuDatabaseConn adds DailyMenuItem to database");
    try (Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "INSERT INTO dailyMenuItem (date, name, quantity) VALUES ";

      for (MenuItemWithQuantity menuItem : menuItems) {
        sql += String.format("('%s', '%s', '%s'),", menuItem.getDate(), menuItem.getName(),
            menuItem.getQuantity());
      }
      sql = sql.substring(0,sql.length()-1);

      PreparedStatement statement = connection.prepareStatement(sql);
      statement.execute();
      Log.log("MenuDatabaseConn adds DailyMenuItem to database");

    }

  }

  /**
   * Gets a list of all the menu items registered in the system.
   * @return the list containing all the menu items.
   * @throws SQLException if any error happens during setting up the database connection
   */
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
        String path = resultSet.getString("imgPath");

        menuItems.add(new MenuItem(name, null, price,path));
      }
    }

    Log.log("MenuDatabaseConn returns ListOfMenuItems to database");
    return menuItems;
  }

  /**
   * Get a list of MenuItemWithQuantity for a specific date
   * @param date the date to get the menu items from
   * @return a list containing the MenuItemWithQuantity objects on the specific date
   * @throws SQLException if any error happens during setting up the database connection
   */
  public ArrayList<MenuItemWithQuantity> getDailyMenuItemList(LocalDate date)
      throws SQLException
  {
    ArrayList<MenuItemWithQuantity> dailyItems = new ArrayList<>();


    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql1 = "SELECT dailyMenuItem.name, dailyMenuItem.quantity, menuItem.price, menuItem.imgPath FROM dailyMenuItem "
          + "INNER JOIN menuItem ON dailyMenuItem.name = menuItem.name WHERE date = '" + date + "'";


      PreparedStatement statement1 = connection.prepareStatement(sql1);

      ResultSet resultSet1 = statement1.executeQuery();

      while (resultSet1.next())
      {
        String name = resultSet1.getString("name");

        int quantity = resultSet1.getInt("quantity");

        double price = resultSet1.getDouble("price");

        String path = resultSet1.getString("imgPath");

        String sql2 = "SELECT name FROM ingredient WHERE id in (SELECT ingredientId FROM menuItemIngredient WHERE itemName = '" + name + "' ) ";

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ArrayList<String> ingredients = new ArrayList<>();

        while (resultSet2.next())
        {
          String ingredientName = resultSet2.getString("name");
          ingredients.add(ingredientName);
        }

          MenuItem menuItem = new MenuItem(name,ingredients,price,path);

          MenuItemWithQuantity dailyMenuItem = new MenuItemWithQuantity(menuItem,date,quantity);
          dailyItems.add(dailyMenuItem);
      }
    }

    Log.log("MenuDatabaseConn returns DailyMenuItemList to database");

    return dailyItems;
  }

  /**
   * Add quantity to a menu item which is enrolled in the daily menu
   * @param date date on which the menu item is enrolled in.
   * @param name name of the menu item
   * @param quantity quantity
   * @throws SQLException if any error happens during setting up the database connection
   */
  public void addQuantity(LocalDate date, String name, int quantity)
      throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "UPDATE dailyMenuItem set quantity = " + quantity + " WHERE name = '"
          + name + "' AND date = '" + date + "'";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.execute();

      Log.log("MenuDatabaseConn adds quantity to database");
    }
  }

  public void deleteMenuItemFromDailyMenu(LocalDate date, String name)
      throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "DELETE FROM dailyMenuItem WHERE date = '" + date + "' AND name = '" + name + "'";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.execute();

      Log.log("MenuDatabaseConn delete menuItem from dailyMenuItem");
    }

  }

    public void removeMenuItem(ArrayList<MenuItem> menuItems)
            throws SQLException {
    try(Connection connection = DatabaseConnImp.getConnection()){

      String menuItemsArray = "{";
      for (int i = 0; i < menuItems.size(); i++) {
        if(i==0){
          menuItemsArray += menuItems.get(i).getName();
        }
        else {
          menuItemsArray += "," + menuItems.get(i).getName();
        }
      }
      menuItemsArray+="}";

      String sql = "SELECT deleteMenuItem('" + menuItemsArray + "'::varchar[]);";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.executeQuery();



      Log.log("MenuDataBaseConn delete menu items" + " from database");
    }
    catch (SQLException e){
      String errorMessage = "Item cannot be deleted because it was previously used";

      throw new SQLException(errorMessage);
    }
    }
}
