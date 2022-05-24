package server.databaseConn;

import shared.Log;
import transferobjects.OrderItem;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/** A class handling database connection for order requests made by customer
 * @author Simon
 * @version 1.0
 */
public class OrderDatabaseConn
{

  /**
   * Method which is called when an order is placed.
   * First is gets  all the required data from the cart of customer who placed the order,
   * then it creates an order and order items with the unselected ingredients.
   * All of these data are then inserted into the right tables.
   * Lastly, the method deletes the whole cart.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException
   */
  public void placeOrder(String username) throws SQLException
  {
    Log.log("OrderDatabaseConn places order in the Database");

    try (Connection connection = DatabaseConnImp.getConnection())
    {

      //getting needed values for creating an order item

      String sql1 =
          "SELECT cart.cartid, cartItem.itemname, cartItem.quantity FROM "
              + "cart, cartItem WHERE cart.cartid = cartitem.cartid AND cart.username = '"
              + username + "'";

      PreparedStatement statement1 = connection.prepareStatement(sql1);

      ResultSet resultSet1 = statement1.executeQuery();


      while (resultSet1.next())
      {

        int code = resultSet1.getInt("cartid");

        String itemName = resultSet1.getString("itemname");

        int quantity = resultSet1.getInt("quantity");

        // to get ingredients unselected by the customer
        String sql3 = "SELECT ingredient.name "
            + "FROM cartitemunselectedingredients, ingredient "
            + "WHERE cartitemunselectedingredients.ingredientid = ingredient.id and cartitemunselectedingredients.cartid = '" + code + "' AND cartitemunselectedingredients.itemname = '" + itemName + "'";

        PreparedStatement statement3 = connection.prepareStatement(sql3);

        ResultSet resultSet3 = statement3.executeQuery();

        ArrayList<String> unselectedIngredients = new ArrayList<>();

        while (resultSet3.next()){
          String ingredientName = resultSet3.getString("name");
          unselectedIngredients.add(ingredientName);
        }

        // deleting cart item and unselectedingredients tables

        String sql4 = "DELETE FROM cartitem WHERE cartid = " + code + "AND itemname = '" + itemName + "'"; // deletes the cartitem with specific order number (code) and item name

        PreparedStatement statement4 = connection.prepareStatement(sql4);

        statement4.execute();

        String sql5 = "DELETE FROM cartitemunselectedingredients WHERE cartid = " + code + "AND itemname = '" + itemName + "'"; // deletes unselectedingredients for this item

        PreparedStatement statement5 = connection.prepareStatement(sql5);

        statement5.execute();

        // inserting into orderitem and unselectedingredients tables

        LocalDate date = LocalDate.now(); //IDK if the sql6 is gonna work but whatever



        //checks if order already exists

        String sqlCheck = "SELECT ordernumber from \"order\"";

        PreparedStatement statementCheck = connection.prepareStatement(sqlCheck);

        ResultSet resultSetCheck = statementCheck.executeQuery();

        int codeCheck = 0;

        while (resultSetCheck.next()){
          codeCheck = resultSetCheck.getInt("ordernumber");
        }

        if(code != codeCheck){

          //create new order

          String sql6 = "INSERT INTO \"order\" VALUES (" + code + ", '" + username
              + "', '" + date + "', 'false')";


          PreparedStatement statement6 = connection.prepareStatement(sql6);

          statement6.execute();
        }

        //insert into orderitem

        String sql7 = "INSERT INTO orderitem VALUES ('" + itemName + "', " + code + ", " + quantity + ")";

        PreparedStatement statement7 = connection.prepareStatement(sql7);

        statement7.execute();


        //inserting into unselectedingredients table
        System.out.println("THERE IS THAT MANY UNSELECTED INGREDIENTS: " + unselectedIngredients.size() + "-----------------------------------------------------------------");
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
  }

  /**
   * Returns all data of the customer's order.
   * That is name of the ordered items, its ingredients, prices, image paths, quantities, unselected ingredients, username of the customer and code of the order
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException
   */
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

  /**
   * Cancels the whole order of the customer.
   * This means all the data about the order are deleted.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException
   */
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

  /**
   * Gets all the uncollected orders from all the customers and returns a list of each customer's
   * list of order items.
   * @return a list of each customer's list of order items.
   * @throws SQLException when an unexpected exception happens
   */
  public ArrayList<ArrayList<OrderItem>> getAllUncollectedOrders() throws SQLException {
    String sql = "SELECT o.ordernumber, o.username, o.date, oi.itemname, "
        +              " oi.quantity, m.price, imgpath, "
        +              " ARRAY_AGG(i.name) AS unselectedingr, "             //select unselected ingredients
        +              " (SELECT ARRAY_AGG(ingredient.name) "               //select all the ingredients (with a sub select)
        +              "  FROM ingredient "
        +              "   JOIN menuitemingredient m2 ON ingredient.id = m2.ingredientid "
        +              "   JOIN menuitem m3 ON m2.itemname = m3.name "
        +              "  WHERE m3.name = oi.itemname) as allingr "         // end of selecting all the ingredients
        +        "FROM \"order\" o "
        +        "  JOIN orderitem oi ON o.ordernumber = oi.ordernumber "
        +        "  LEFT OUTER JOIN orderitemunselectedingredients ou ON o.ordernumber = ou.ordernumber "
        +        "  LEFT OUTER JOIN ingredient i ON ou.ingredientid = i.id "
        +        "  LEFT OUTER JOIN menuitem m ON oi.itemname = m.name "
        +        "WHERE collected = FALSE "
        +        "GROUP BY o.ordernumber, o.username, o.date, oi.itemname, oi.quantity, m.price, imgpath;";

    try(Connection conn = DatabaseConnImp.getConnection()){
      PreparedStatement statement = conn.prepareStatement(sql);
      ResultSet set = statement.executeQuery(); // run the statement

      // put the items in this hashmap first (key is the username, value is a list of the
      // user's uncollected order items
      HashMap<String, ArrayList<OrderItem>> itemsSorted = new HashMap<>();

      while(set.next()){
        int orderNumber = set.getInt("ordernumber");
        int quantity = set.getInt("quantity");
        double price = set.getDouble("price");
        String username = set.getString("username");
        String itemname = set.getString("itemname");
        String imgPath = set.getString("imgpath");
        String unselectedIngredients = set.getString("unselectedingr");
        String allIngredients = set.getString("allIngr");

        ArrayList<String> unselected = convertStringToList(unselectedIngredients);
        ArrayList<String> all = convertStringToList(allIngredients);


        OrderItem orderItem = new OrderItem(itemname, all, price, imgPath, username, quantity, unselected, orderNumber);

        // check if the username is already added to the hashmap (if not then add it)
        if(!itemsSorted.containsKey(username)) {
          itemsSorted.put(username, new ArrayList<>());
        }
        itemsSorted.get(username).add(orderItem); // add the user's order item to their list
      }

      // convert the HashMap<String, ArrayList<OrderItem>> to ArrayList<ArrayList<OrderItem>>
      ArrayList<ArrayList<OrderItem>> finalItems = new ArrayList<>(){{
        addAll(itemsSorted.values());
      }};

      return finalItems;
    }
  }

  /**
   * Converts an sql returned list into a general java.util.ArrayList.
   * @param text the text to convert to list.
   * @return the list extracted from the text.
   */
  private static ArrayList<String> convertStringToList(String text){

    // sql list comes in this format: {item1,item2,item3}
    // so we split them and convert it to ArrayList. (if there isn't any item in the list,
    // then the text is exactly this: {NULL}, in this case, just create a new ArrayList)
    ArrayList<String> list;

    text = text.substring(1, text.length() - 1); // remove the { }
    if(text.equals("NULL")) { // check if list is empty
      list = new ArrayList<>();
    }
    else {
      String[] allIngredientsArray = text.split(",");
      List<String> simpleSecondList = Arrays.asList(allIngredientsArray);
      list = new ArrayList<>(simpleSecondList);
    }

    return list;
  }

  /**
   * Mark an uncollected order as collected by the employee.
   * @param orderCode the order's code to mark it collected.
   * @throws SQLException when the order was cancelled before this task.
   */
  public void collectOrder(int orderCode) throws SQLException {
    try (Connection conn = DatabaseConnImp.getConnection()){
      String sql = "UPDATE \"order\" SET collected = TRUE WHERE ordernumber = ?;";

      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setInt(1, orderCode);
      int updatedRows = statement.executeUpdate();

      if(updatedRows == 0){
        throw new SQLException("Cannot mark order " + orderCode + " as collected. Reason: order was cancelled.");
      }
    }
  }
}
