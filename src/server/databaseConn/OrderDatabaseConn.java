package server.databaseConn;

import shared.Log;
import transferobjects.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDatabaseConn
{
  public ArrayList<OrderItem> placeOrder(String username) throws SQLException
  {
    Log.log("OrderDatabaseConn places order in the Database");
    ArrayList<OrderItem> orderItems = new ArrayList<>();

    try (Connection connection = DatabaseConnImp.getConnection())
    {

      //getting needed values for creating an order item

      String sql1 =
          "SELECT cart.cartid, cartItem.itemname, cartItem.quantity, menuItem.price, menuItem.imgpath FROM "
              + "cart, cartItem, menuItem WHERE cart.cartid = cartitem.cartid and cartitem.itemname = menuItem.name AND cart.username = '"
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

        //to get ingredients of a meal
        String sql2 = "SELECT name FROM ingredient WHERE id in (SELECT ingredientId FROM menuItemIngredient WHERE itemName = '" + itemName + "' ) ";

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ArrayList<String> ingredients = new ArrayList<>();

        while (resultSet2.next())
        {
          String ingredientName = resultSet2.getString("name");
          ingredients.add(ingredientName);
        }

        // to get ingredients unselected by the customer
        String sql3 = "SELECT ingredient.name "
            + "FROM cartitemunselectedingredients, ingredient "
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

        // deleting cart item and unselectedingredients tables

        String sql4 = "DELETE FROM cartitem WHERE cartid = " + code + "AND itemname = '" + itemName + "'"; // deletes the cartitem with specific order number (code) and item name

        PreparedStatement statement4 = connection.prepareStatement(sql4);

        statement4.execute();

        String sql5 = "DELETE FROM cartitemunselectedingredients WHERE cartid = " + code + "AND itemname = '" + itemName + "'"; // deletes unselectedingredients for this item

        PreparedStatement statement5 = connection.prepareStatement(sql5);

        statement5.execute();

        // inserting into orderitem and unselectedingredients tables

        LocalDate date = LocalDate.now(); //IDK if the sql6 is gonna work but whatever

        //create new order

        String sql6 = "INSERT INTO \"order\" VALUES (" + code + ", '" + username +"', '" + date +"', 'false')";

        PreparedStatement statement6 = connection.prepareStatement(sql6);

        statement6.execute();

        //insert into orderitem

        String sql7 = "INSERT INTO orderitem VALUES ('" + itemName + "', " + code + ", " + quantity + ")";

        PreparedStatement statement7 = connection.prepareStatement(sql7);

        statement7.execute();


        //inserting into unselectedingredients table
        for (String unselectedIngredient: unselectedIngredients
             )
        {
          String sql8 = "SELECT id FROM ingredient WHERE name = '" + unselectedIngredient + "'";

          PreparedStatement statement8 = connection.prepareStatement(sql8);

          ResultSet resultSet8 = statement8.executeQuery();

          while (resultSet8.next())
          {

            int ingredientId = resultSet8.getInt("id");

            String sql9 = "INSERT INTO orderitemunselectedingredients VALUES ('"
                + itemName + "', " + code + ", " + ingredientId + ")";

            PreparedStatement statement9 = connection.prepareStatement(sql9);

            statement9.execute();
          }
        }
      }
    }

    return orderItems;
  }

  public ArrayList<OrderItem> getUncollectedOrder(String username)
      throws SQLException
  {
    ArrayList<OrderItem> orderItems = new ArrayList<>();

    try (Connection connection = DatabaseConnImp.getConnection())
    {
      String sql1 =
          "SELECT orderitem.ordernumber, orderitem.itemname, orderitem.quantity, menuItem.price, menuItem.imgpath FROM "
              + "\"order\", orderitem, menuItem WHERE orderitem.itemname = menuitem.name AND \"order\".ordernumber = orderitem.ordernumber AND \"order\".username = '"
              + username + "'";

      PreparedStatement statement1 = connection.prepareStatement(sql1);

      ResultSet resultSet1 = statement1.executeQuery();

      while (resultSet1.next()){
        int code = resultSet1.getInt("ordernumber");

        String itemName = resultSet1.getString("itemname");

        int quantity = resultSet1.getInt("quantity");

        double price = resultSet1.getDouble("price");

        String imgPath = resultSet1.getString("imgPath");

        String sql2 = "SELECT name FROM ingredient WHERE id in (SELECT ingredientId FROM menuItemIngredient WHERE itemName = '" + itemName + "' ) ";

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        ResultSet resultSet2 = statement2.executeQuery();

        ArrayList<String> ingredients = new ArrayList<>();

        while (resultSet2.next())
        {
          String ingredientName = resultSet2.getString("name");
          ingredients.add(ingredientName);
        }

        // to get ingredients unselected by the customer
        String sql3 = "SELECT ingredient.name "
            + "FROM cartitemunselectedingredients, ingredient "
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
      }

    }
    return orderItems;
  }

  public void cancelOrder(String username) throws SQLException
  {
    Log.log("Order for " + username + " canceled.");
    try (Connection connection = DatabaseConnImp.getConnection())
    {
      String sql1 = "SELECT ordernumber from \"order\" WHERE username = '" + username + "'";

      PreparedStatement statement1 = connection.prepareStatement(sql1);

      ResultSet resultSet1 = statement1.executeQuery();

      while (resultSet1.next()){

        int orderNumber = resultSet1.getInt("ordernumber");


        //deletes the unselected ingredients
        String sql4 = "DELETE FROM orderitemunselectedingredients WHERE ordernumber = " + orderNumber;

        PreparedStatement statement4 = connection.prepareStatement(sql4);

        statement4.execute();

        //deletes items from orderitem table with specified order number
        String sql3 = "DELETE FROM orderitem WHERE ordernumber = " + orderNumber;

        PreparedStatement statement3 = connection.prepareStatement(sql3);

        statement3.execute();

        //deletes items from table order with specified order item
        String sql2 = "DELETE FROM \"order\" WHERE ordernumber = " + orderNumber;

        PreparedStatement statement2 = connection.prepareStatement(sql2);

        statement2.execute();


      }
    }
    }
  }
