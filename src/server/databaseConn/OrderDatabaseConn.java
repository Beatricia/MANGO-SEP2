package server.databaseConn;

import shared.Log;
import transferobjects.OrderItem;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/** A class handling database connection for order requests made by customer
 * @author Simon
 * @version 1.0
 */
public class OrderDatabaseConn implements Serializable
{

  /**
   * Method which is called when an order is placed.
   * First its gets all the required data from the cart of customer who placed the order,
   * then it creates an order and order items with the unselected ingredients.
   * All of these data are then inserted into the right tables.
   * Lastly, the method deletes the whole cart.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  public void placeOrder(String username) throws SQLException
  {
    /*
        Table of Contents:
         - 1: get all the cart items with their ingredients from the cart tables
         - 2: check if the cart is empty (if empty: throw exception)
         - 3: insert new order (if customer has uncollected order: throw exception (by sql trigger))
         - 4: get new order's number
         - 5: remove all items and unselected ingredients from the cart
         - 6: add the items from step 1 into the order items and unselected ingredients
     */



    Log.log("OrderDatabaseConn places order in the Database");

    try(Connection conn = DatabaseConnImp.getConnection()){

      // 1: getting the cart items with unselected ingredients
      String sql =
          "SELECT c.cartid, ci.itemname, ci.quantity, ARRAY_AGG(cu.ingredientid) "
              + "FROM cart c "
              + " JOIN cartitem ci ON c.cartid = ci.cartid "
              + " LEFT OUTER JOIN cartitemunselectedingredients cu ON ci.cartid = cu.cartid AND ci.itemname = cu.itemname "
              + "WHERE c.username = ? "
              + "GROUP BY c.cartid, ci.itemname, ci.quantity;";

      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1, username);
      ResultSet cartItems = statement.executeQuery();

      // 2: check if there are any items in the cart
      if(!cartItems.next())
        throw new SQLException("No items in the cart");


      // 3: creating a new order (this throws an exception if the customer already has an uncollected
      //    order (due to the oneOrderPerCustomer trigger)
      String createNewOrderSql = "INSERT INTO \"order\"(username, date) VALUES (?, ?);";
      statement = conn.prepareStatement(createNewOrderSql);
      statement.setString(1, username);
      statement.setDate(2, Date.valueOf(LocalDate.now()));
      try{
        statement.execute();
      } catch (SQLException e) {
        throw new SQLException("You already have an uncollected order!");
      }


      // 4: get the new order's number
      String getNewOrderNumber = "SELECT ordernumber FROM \"order\" "
          + "WHERE collected = FALSE AND username = ?;";
      statement = conn.prepareStatement(getNewOrderNumber);
      statement.setString(1, username);
      ResultSet newOrderNumberRes = statement.executeQuery();
      newOrderNumberRes.next();

      int newOrderNum = newOrderNumberRes.getInt("ordernumber");
      int cartId = cartItems.getInt("cartid");



      // 5: remove items and ingredients from the cart
      String deleteIngredients = "DELETE FROM cartitemunselectedingredients WHERE cartid = ?;";
      statement = conn.prepareStatement(deleteIngredients);
      statement.setInt(1, cartId);
      statement.execute();

      String deleteCartItems = "DELETE FROM cartitem WHERE cartid = ?;";
      statement = conn.prepareStatement(deleteCartItems);
      statement.setInt(1, cartId);
      statement.execute();


      // 6: add the cartitems to the order
      do {
        String itemName = cartItems.getString("itemname");
        int quantity = cartItems.getInt("quantity");
        List<String> unselectedIngredients = OrderDatabaseConn.convertStringToList(cartItems.getString("array_agg"));

        // insert order item
        String insertItem = "INSERT INTO orderitem(itemname, ordernumber, quantity) VALUES (?, ?, ?);";
        statement = conn.prepareStatement(insertItem);
        statement.setString(1, itemName);
        statement.setInt(2, newOrderNum);
        statement.setInt(3, quantity);
        statement.execute();

        // insert unselected ingredients (itemname, ordernumber, ingredientid)
        if(unselectedIngredients.size() > 0){
          String insertUnselectedIngredients = "INSERT INTO orderitemunselectedingredients(itemname, ordernumber, ingredientid) VALUES ";
          List<String> inputs = unselectedIngredients.stream()
              .map(
                  ingredientId -> String.format("('%s',%s,%s)", itemName, newOrderNum, ingredientId)
              )
              .collect(Collectors.toList());

          insertUnselectedIngredients += String.join(",", inputs);
          statement = conn.prepareStatement(insertUnselectedIngredients);
          statement.execute();
        }
      } while (cartItems.next());
    }
  }


  /**
   * Returns all data of the customer's order.
   * That is name of the ordered items, its ingredients, prices, image paths, quantities, unselected ingredients, username of the customer and code of the order
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
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
              + username + "' and \"order\".collected = false";

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
            + "FROM orderitemunselectedingredients, ingredient "
            + "WHERE orderitemunselectedingredients.ingredientid = ingredient.id "
            + "and orderitemunselectedingredients.itemname = '" + itemName + "'"
            + "and orderitemunselectedingredients.ordernumber = '" + code + "'";

        PreparedStatement statement3 = connection.prepareStatement(sql3);
        ResultSet resultSet3 = statement3.executeQuery();

        ArrayList<String> unselectedIngredients = new ArrayList<>();

        while (resultSet3.next()){
          String ingredientName = resultSet3.getString("name");
          unselectedIngredients.add(ingredientName);
        }

        OrderItem orderItem = new OrderItem(itemName, ingredients, price, imgPath, username, quantity, unselectedIngredients, code, LocalDate.now());
        orderItems.add(orderItem);
      }

    }
    return orderItems;
  }

  /**
   * Cancels the whole order of the customer.
   * This means all the data about the order are deleted.
   * @param username unique identifier of the customer who makes the order
   * @throws SQLException on unexpected exception
   */
  public void cancelOrder(String username) throws SQLException
  {
    Log.log("Order for " + username + " canceled.");
    try (Connection connection = DatabaseConnImp.getConnection())
    {
      String sql1 = "SELECT ordernumber from \"order\" WHERE collected = FALSE";

      if(username != null) {
        sql1 += " AND username = '" + username + "'";
      }

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
        +        "  LEFT OUTER JOIN orderitemunselectedingredients ou ON o.ordernumber = ou.ordernumber AND ou.itemname = oi.itemname "
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


        OrderItem orderItem = new OrderItem(itemname, all, price, imgPath, username, quantity, unselected, orderNumber, LocalDate.now());

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
  public static ArrayList<String> convertStringToList(String text){

    // sql list comes in this format: {item1,item2,item3}
    // so we split them and convert it to ArrayList. (if there isn't any item in the list,
    // then the text is exactly this: {NULL}, in this case, just create a new ArrayList)
    ArrayList<String> list;

    if (text==null)
    {
      return new ArrayList<>();
    }

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
