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

  public void removeCartItem(CartItem cartItem) throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql = "DELETE FROM cartItem WHERE itemName = ?";
      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1,cartItem.getName());

      statement.executeUpdate();
      Log.log("CartDatabaseConn removes item from cart");

      //created a trigger to remove all the unselected items for this product
      // from the cartUnselectedIngredientsTable
    }
  }

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

            CartItem cartItem = new CartItem(itemName,ingredients,price,imgPath
                ,username,quantity,new ArrayList<>());

            cartItems.add(cartItem);
          }

        }
      }
    return cartItems;
  }

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

  private void updateUnselectedIngredientsTable(CartItem cartItem, int cartId)
      throws SQLException
  {
    ArrayList<String> unselected = cartItem.getUnselectedIngredients();

    try(Connection connection = DatabaseConnImp.getConnection())
    {

      //First delete all the unwanted ing that were there
      String sqlT0Delete = "DELETE from " +  CART_UNSELECTED_INGREDIENTS_TABLE + " WHERE cartId = " + cartId + " ;";

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
}
