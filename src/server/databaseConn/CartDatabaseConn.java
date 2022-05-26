package server.databaseConn;

import shared.Log;
import transferobjects.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDatabaseConn
{
  private final static String CART_TABLE = "cart";
  private final static String CART_ITEM_TABLE = "cartItem";
  private final static String CART_UNSELECTED_INGREDIENTS_TABLE = "cartitemunselectedingredients";

  /**
   * The method calls teh private method getCartIdFromUsername to get the cart
   * id, and inserts the item that has the name given as a parameter into
   * the cartItem table with the cart id value.
   * @param cartItemName the name of the item to be added to the cartItem table
   * @param username  the username of the Client used to get the cart id
   * @throws SQLException on unexpected exception
   */
  public void addItemToCart(String cartItemName, String username)
      throws SQLException
  {

    try (Connection connection = DatabaseConnImp.getConnection())
    {

      int cartId = getCartIdFromUsername(username);


      String insertSql = "INSERT INTO " + CART_ITEM_TABLE + " (itemName, cartId) VALUES (?,?) ;";
      PreparedStatement statement = connection.prepareStatement(insertSql);

      statement.setString(1, cartItemName);
      statement.setInt(2,cartId);
      statement.executeUpdate();
      Log.log("CartDatabaseConn adds item to Cart");
    }
  }

  /**
   * Called whenever a new quantity value is set for the item in cart or/and
   * whenever an ingredient is unselected from the view. The method calls the
   * private getCartIdFromUsername method to get te cart id using the
   * provided in the cart item username, first edits the quantity, then checks
   * if there are any unselected ingredients, if yes calls te private method
   * updateUnselectedIngredientsTable.
   *
   * @param cartItem the cart item that has to be edited
   * @throws SQLException on unexpected exception
   */
  public void editCartItem(CartItem cartItem) throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      //first update the quantity
      int cartId = getCartIdFromUsername(cartItem.getUsername());

      String sql = "UPDATE cartitem SET quantity = ? WHERE itemName ='" + cartItem.getName()+  "' AND cartId = '" + cartId + " ';";

      PreparedStatement statement = connection.prepareStatement(sql);

      int quantity = cartItem.getQuantity();
      statement.setInt(1, quantity);

      statement.executeUpdate();


      Log.log("CartDatabaseConn: Quantity changed");

      //update the unselected ingredients table
     if(cartItem.getUnselectedIngredients().size()>0)
      {
        updateUnselectedIngredientsTable(cartItem, cartId);
      }

    }

  }

  /**
   * The method deletes the specified cart item from the cartItem table
   * (The table has a TRIGGER which automatically deletes all the unselected
   * items for this item from the cartUnselectedIngredient table)
   * @param cartItem  the cart item that should be deleted
   * @throws SQLException on unexpected exception
   */
  public void removeCartItem(CartItem cartItem) throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "DELETE FROM cartItem WHERE itemName = ?";
      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1,cartItem.getName());

      statement.executeUpdate();
      Log.log("CartDatabaseConn removes item from cart");

    }
  }

  /**
   * The method first calls the private method getCartIdFromUsername to get
   * the cart id with the username given as a parameter. Secondly, the itemName
   * and quantity are selected from the cartItem. Next the price and image path
   * are extracted from the menuItemTable. Lastly, the getAllIngredients
   * private method is called to obtain all the ingredients of the menu item,
   * and with all that information a new CartItem object is created and added
   * an ArrayList, which is returned (when creating the CartItem object the
   * unselected ingredients are represented by an empty ArrayList)
   * @param username
   * @return an Arraylist with all the CartItem object in the shopping cart
   * @throws SQLException on unexpected exception
   */
  public ArrayList<CartItem> getCartList(String username) throws SQLException
  {
      ArrayList<CartItem> cartItems = new ArrayList<>();

      //get the Cart id using the Username given
      int cartId = getCartIdFromUsername(username);

      try(Connection connection = DatabaseConnImp.getConnection())
      {

        //get the name of the item and its quantity
        String sql1 = "SELECT itemName, quantity FROM cartItem WHERE cartId = ?";

        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setInt(1, cartId);

        ResultSet set1 = statement1.executeQuery();

        while (set1.next())
        {
          String itemName = set1.getString("itemname");
          int quantity = set1.getInt("quantity");


          //price imgPath
          String sql2 = "SELECT price, imgpath FROM menuItem WHERE name = ?";

          PreparedStatement statement2 = connection.prepareStatement(sql2);
          statement2.setString(1,itemName);

          ResultSet set2 = statement2.executeQuery();

          if (set2.next())
          {
            double price = set2.getDouble("price");
            String imgPath = set2.getString("imgpath");

            //get the ing
            ArrayList<String> ingredients = getAllIngredients(itemName);

            //Create the CartItem and add it to the ArrayList

            //get all unselected ingredients


            ArrayList<String> unselected = getUnselectedIngredients(itemName, cartId);

            CartItem cartItem = new CartItem(itemName,ingredients,price,imgPath
                ,username,quantity, unselected);

            cartItems.add(cartItem);
          }

        }
      }
    return cartItems;
  }

  /**
   * The method is used to get a list of all unselected ingredients of the item, which
   * has the name given as a parameter, and it's stored in cart with given id.
   * @param itemName the name of the item
   * @param cartId id of the cart where the item is located
   * @return an ArrayList that contains all ingredients the menu item has
   */
  private ArrayList<String> getUnselectedIngredients(String itemName, int cartId)
      throws SQLException
  {
    ArrayList<String> unselected = new ArrayList<>();

    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "SELECT name FROM ingredient WHERE id in (SELECT ingredientId FROM cartItemUnselectedIngredients WHERE itemName = ? AND cartId = ?)";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, itemName);
      statement.setInt(2, cartId);

      ResultSet resultSet = statement.executeQuery();


      while (resultSet.next())
      {
        String ing = resultSet.getString("name");

        unselected.add(ing);
      }
    }
    return unselected;
  }

  /**
   * The method uses a subquery to select all ingredients of the item, which
   * has the name given as a parameter.
   * @param itemName the name of the item
   * @return an ArrayList that contains all ingredients the menu item has
   */
  private ArrayList<String> getAllIngredients(String itemName)
  {
    ArrayList<String> ingredients = new ArrayList<>();
    try(Connection connection = DatabaseConnImp.getConnection())
    {

      //get akk the ingredients' ids
      String sql = "SELECT name FROM ingredient WHERE id IN (SELECT ingredientId FROM menuItemIngredient WHERE itemName = ?)";

      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, itemName);

      ResultSet resultSet = statement.executeQuery();


      while (resultSet.next())
      {
        String ing = resultSet.getString("name");

        ingredients.add(ing);
      }

    }
    catch (SQLException throwables)
    {
      throwables.printStackTrace();
    }

    return ingredients;
  }

  /**
   * The method first deletes all the items from the
   * cartUnselectedIngredientsTable for the specified cart and then inserts the
   * new given list of unwanted ingredients.
   * @param cartItem the cart item that ingredients should be marked as unwanted to
   * @param cartId  the cart id of the cartItem
   * @throws SQLException
   */
  private void updateUnselectedIngredientsTable(CartItem cartItem, int cartId)
      throws SQLException
  {
    ArrayList<String> unselected = cartItem.getUnselectedIngredients();

    try(Connection connection = DatabaseConnImp.getConnection())
    {

      //First delete all the unwanted ing that were there
      String sqlT0Delete = "DELETE from " +  CART_UNSELECTED_INGREDIENTS_TABLE + " WHERE cartId = " + cartId + " AND itemname = '" + cartItem.getName() + "';";

      PreparedStatement statementToDelete = connection.prepareStatement(sqlT0Delete);

      statementToDelete.executeUpdate();


      //Then insert all the unwanted ing
      String sql = "INSERT INTO cartItemUnselectedIngredients VALUES (?, ?, (SELECT id from ingredient WHERE name = ?));";

      for (int i = 0; i < unselected.size(); i++)
      {

        PreparedStatement statement = connection.prepareStatement(sql);

        String itemName = cartItem.getName();


        statement.setString(1, itemName);
        statement.setInt(2, cartId);
        statement.setString(3, unselected.get(i));

        statement.executeUpdate();
      }
      Log.log("CartDatabaseConn puts the unselected ingredients in the table");
    }

  }

  /**
   * The method extracts and returns the cart id of the user that has the
   * username given as a parameter
   * @param username the username of the user whose cart id is needed
   * @return and integer representing the cart id of the customer
   * @throws SQLException
   */
  private int getCartIdFromUsername(String username) throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql =  "SELECT cartId FROM " + CART_TABLE + " WHERE username = ? ";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, username);

      ResultSet set = statement.executeQuery();
      String cartId = "";
      if(set.next())
      {
        cartId = set.getString(1);
      }

      Log.log("CartDatabaseConn returns the Cart List items");
      return Integer.parseInt(cartId);
    }
  }

  /**
   * deletes all items stored in the cart table in the database
   * @throws SQLException
   */
  public void emptyAllCarts() throws SQLException
  {
    String deleteFromCartIngredients = "DELETE FROM cartitemunselectedingredients;";
    String deleteFromCartItems = "DELETE FROM cartItem;";

    PreparedStatement statement;
    try(Connection conn = DatabaseConnImp.getConnection()){
      statement = conn.prepareStatement(deleteFromCartIngredients);
      statement.execute();
      statement = conn.prepareStatement(deleteFromCartItems);
      statement.execute();
    }
  }
}
