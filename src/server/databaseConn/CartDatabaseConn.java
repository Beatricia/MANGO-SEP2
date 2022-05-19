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
  private final static String CART_ITEM_TABLE = "cartitem";
  private final static String CART_UNSELECTED_INGREDIENTS_TABLE = "cartitemunselectedingredients";


  public void addItemToCart(String cartItemName, String username)
      throws SQLException
  {
    Log.log("CartDatabaseConn adds item to Cart");

    try (Connection connection = DatabaseConnImp.getConnection())
    {

      int cartId = getCartIdFromUsername(username);


      String insertSql = "INSERT INTO " + CART_ITEM_TABLE + " (itemName, cartId) VALUES(?,?);";
      PreparedStatement statement = connection.prepareStatement(insertSql);

      statement.setString(1, cartItemName);
      statement.setInt(2,cartId);
      statement.executeUpdate();
      Log.log("CartDatabaseConn adds item to Cart");
    }
  }

  private int getCartIdFromUsername(String username) throws SQLException
  {
    try(Connection connection = DatabaseConnImp.getConnection())
    {
      String sql =  "SELECT cartId FROM " + CART_TABLE + " WHERE username = '" + username + "');";
      PreparedStatement statement = connection.prepareStatement(sql);

      ResultSet set = statement.executeQuery();
      String cartId = set.getString(1);
      return Integer.parseInt(cartId);
    }
  }

  public void editCartItem(CartItem cartItem)
  {
  }

  public void removeCartItem(CartItem cartItem)
  {
  }

  public ArrayList<CartItem> getCartList(String username)
  {
    return null;
  }
}
