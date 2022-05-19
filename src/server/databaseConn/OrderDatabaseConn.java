package server.databaseConn;

import shared.Log;
import transferobjects.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDatabaseConn
{
  public ArrayList<OrderItem> placeOrder(String username) throws SQLException
  {
    Log.log("OrderDatabaseConn places order in the Database");
    ArrayList<OrderItem> orderItems = new ArrayList<>();

    try (Connection connection = DatabaseConnImp.getConnection())
    {

      //to get the name and quantity

      String sql1 =
          "SELECT cart.cartid, cartItem.itemname, cartItem.quantity, menuItem.price, menuItem.imgpath FROM"
              + "cartItem INNER JOIN cart ON cartItem.cartid = cart.cartid INNER JOIN "
              + "menuItem ON cartItem.itemname = menuItem.name WHERE cart.username = '"
              + username + "'";

      PreparedStatement statement1 = connection.prepareStatement(sql1);

      ResultSet resultSet1 = statement1.executeQuery();

      while (resultSet1.next())
      {

        int code = resultSet1.getInt("cartid");

        String itemName = resultSet1.getString("itemname");

        int quantity = resultSet1.getInt("quantity");

        double price = resultSet1.getDouble("price");

        String imgPath = resultSet1.getString("imgpath");

        String sql2 = "SELECT name FROM ingredient WHERE id in (SELECT ingredientId FROM menuItemIngredient WHERE itemName = '" + itemName + "' ) ";

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ArrayList<String> ingredients = new ArrayList<>();

        while (resultSet2.next())
        {
          String ingredientName = resultSet2.getString("name");
          ingredients.add(ingredientName);
        }

        String sql3 = "SELECT ingredient.name\n"
            + "FROM cartitemunselectedingredients, ingredient\n"
            + "WHERE cartitemunselectedingredients.ingredientid = ingredient.id and cartitemunselectedingredients.cartid = '" + code + "'";

        PreparedStatement statement3 = connection.prepareStatement(sql3);

        ResultSet resultSet3 = statement3.executeQuery();

        ArrayList<String> unselectedIngredients = new ArrayList<>();

        while (resultSet3.next()){
          String ingredientName = resultSet3.getString("name");
          unselectedIngredients.add(ingredientName);
        }

        OrderItem orderItem = new OrderItem(itemName, ingredients, price, imgPath, username, quantity, unselectedIngredients, code);
        orderItems.add(orderItem);

        //TODO ADD stuff to ORDER TABLES (MAYBE REMOVE FROM CART TABLES? - CAN I DO THIS WITH UAFAS CONNECTION?)
      }
    }

    return orderItems;
  }
}
