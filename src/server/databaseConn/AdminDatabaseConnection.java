package server.databaseConn;

import javafx.util.converter.LocalTimeStringConverter;
import shared.UserType;
import transferobjects.MenuItem;
import transferobjects.OrderItem;
import transferobjects.Statistics;
import transferobjects.User;
import util.DateHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/** A class handling database connection for requests made by admin
 * @author Beatricia
 * @version 1.0
 */
public class AdminDatabaseConnection
{

    /**
     * Updating the employee table from the database. If the employee is:
     * accepted -> set the accepted column to true
     * declined -> delete the employee from the table
     *
     * @param username the username of the employee that it is on hold
     * @param accept   true if the employee is accepted
     * @throws SQLException when the table does not exist
     */
    public void handlePendingEmployee(String username, boolean accept)
        throws SQLException
    {
        try (Connection connection = DatabaseConnImp.getConnection())
        {
            PreparedStatement statement;
            if (accept)
            {
                String str = "UPDATE employee SET accepted = true\n"
                    + "WHERE username = '" + username + "'";
                statement = connection.prepareStatement(str);
            }
            else
            {
                String str =
                    "DELETE from employee\n" + "where username = '" + username + "'";
                statement = connection.prepareStatement(str);
            }
            statement.execute();
        }
    }

    /**
     * Getting all the pending employees from the database that are not accepted yet
     *
     * @return an arrayList of employees that are not accepted
     * @throws SQLException when the table does not exist
     */
    public ArrayList<User> getAllPendingEmployees() throws SQLException
    {
        ArrayList<User> pendingEmployees = new ArrayList<>();
        try (Connection connection = DatabaseConnImp.getConnection())
        {
            String str =
                "SELECT username, firstName, lastName " + "FROM employee\n"
                    + "WHERE employee.accepted=false;";
            PreparedStatement statement = connection.prepareStatement(str);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {
                String userName = set.getString("username");
                String firstName = set.getString("firstname");
                String lastName = set.getString("lastname");

                User user = new User(userName, UserType.EMPLOYEE, firstName,
                    lastName);
                pendingEmployees.add(user);

            }
            return pendingEmployees;
        }
    }

    /**
     * Setting both opening and closing time of the canteen in the Database
     *
     * @param from LocalTime object which stores the opening time of the canteen
     * @param to   LocalTime object which stores the closing time of the canteen
     * @throws SQLException when the table does not exist
     */
    public void setOpeningHours(LocalTime from, LocalTime to)
        throws SQLException
    {
        try (Connection connection = DatabaseConnImp.getConnection())
        {
            String sql = "UPDATE openinghours SET \"from\" = '" + from + "', \"to\" = '"
                + to + "' WHERE \"from\" = (SELECT \"from\" FROM openinghours ORDER BY \"from\" LIMIT 1)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.execute();
        }
    }

    /**
     * Getting the opening hours of canteen from the Database
     *
     * @return an arrayList of LocalTime objects representing opening and closing time of the canteen
     * @throws SQLException when the table does not exist
     */
    public ArrayList<LocalTime> getOpeningHours() throws SQLException
    {
        ArrayList<LocalTime> openingHours = new ArrayList<>();

        try (Connection connection = DatabaseConnImp.getConnection())
        {
            String sql = "SELECT \"from\", \"to\" FROM openinghours";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                LocalTime from = LocalTime.parse(resultSet.getString("from"));

                LocalTime to = LocalTime.parse(resultSet.getString("to"));

                openingHours.add(from);
                openingHours.add(to);
            }
        }
        return openingHours;
    }

    /**
     * Removes a User object from database by a given username
     *
     * @param username employee's username
     * @throws SQLException
     */
    public void removeEmployee(String username) throws SQLException
    {
        try (Connection connection = DatabaseConnImp.getConnection())
        {
            String sql = "DELETE\n" + "FROM employee\n" + "WHERE username = '" + username
                + "';";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
        }
    }

    /**
     * Gets a list with all accepted employees
     *
     * @return the list containing all accepted employees
     * @throws SQLException
     */

    public ArrayList<User> getAllAcceptedEmployees() throws SQLException
    {
        ArrayList<User> acceptedEmployees = new ArrayList<>();
        try (Connection connection = DatabaseConnImp.getConnection())
        {

            String sql =
                "SELECT username, firstName, lastName\n" + "    FROM employee\n"
                    + "        WHERE employee.accepted = true;";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                String userName = resultSet.getString("username");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");

                User user = new User(userName, UserType.EMPLOYEE, firstName,
                    lastName);
                acceptedEmployees.add(user);
            }
        }
        return acceptedEmployees;
    }

    /**
     * Gets a list with all accepted collected orders
     *
     * @return the list containing all collected orders
     * @throws SQLException
     */
    public ArrayList<ArrayList<OrderItem>> getPurchaseHistory()
        throws SQLException
    {
        String sql = "SELECT o.ordernumber, o.username, o.date, oi.itemname, "
            + " oi.quantity, m.price, imgpath, "
            + " ARRAY_AGG(i.name) AS unselectedingr, "
            //select unselected ingredients
            + " (SELECT ARRAY_AGG(ingredient.name) "
            //select all the ingredients (with a sub select)
            + "  FROM ingredient "
            + "   JOIN menuitemingredient m2 ON ingredient.id = m2.ingredientid "
            + "   JOIN menuitem m3 ON m2.itemname = m3.name "
            + "  WHERE m3.name = oi.itemname) as allingr "
            // end of selecting all the ingredients
            + "FROM \"order\" o "
            + "  JOIN orderitem oi ON o.ordernumber = oi.ordernumber "
            + "  LEFT OUTER JOIN orderitemunselectedingredients ou ON o.ordernumber = ou.ordernumber AND ou.itemname = oi.itemname "
            + "  LEFT OUTER JOIN ingredient i ON ou.ingredientid = i.id "
            + "  LEFT OUTER JOIN menuitem m ON oi.itemname = m.name "
            + "WHERE collected = TRUE "
            + "GROUP BY o.ordernumber, o.username, o.date, oi.itemname, oi.quantity, m.price, imgpath;";

        try (Connection conn = DatabaseConnImp.getConnection())
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet set = statement.executeQuery(); // run the statement

            // put the items in this hashmap first (key is the username, value is a list of the
            // user's uncollected order items
            HashMap<String, ArrayList<OrderItem>> itemsSorted = new HashMap<>();

            while (set.next())
            {
                int orderNumber = set.getInt("ordernumber");
                int quantity = set.getInt("quantity");
                double price = set.getDouble("price");
                String username = set.getString("username");
                String itemname = set.getString("itemname");
                String imgPath = set.getString("imgpath");
                String unselectedIngredients = set.getString("unselectedingr");
                String allIngredients = set.getString("allIngr");

                ArrayList<String> unselected = convertStringToList(
                    unselectedIngredients);
                ArrayList<String> all = convertStringToList(allIngredients);

                OrderItem orderItem = new OrderItem(itemname, all, price,
                    imgPath, username, quantity, unselected, orderNumber);

                // check if the username is already added to the hashmap (if not then add it)
                if (!itemsSorted.containsKey(username))
                {
                    itemsSorted.put(username, new ArrayList<>());
                }
                itemsSorted.get(username).add(orderItem); // add the user's order item to their list
            }

            // convert the HashMap<String, ArrayList<OrderItem>> to ArrayList<ArrayList<OrderItem>>
            ArrayList<ArrayList<OrderItem>> finalItems = new ArrayList<>()
            {{
                addAll(itemsSorted.values());
            }};

            return finalItems;
        }
    }

    /**
     * Converts an sql returned list into a general java.util.ArrayList.
     *
     * @param text the text to convert to list.
     * @return the list extracted from the text.
     */

    public static ArrayList<String> convertStringToList(String text)
    {

        // sql list comes in this format: {item1,item2,item3}
        // so we split them and convert it to ArrayList. (if there isn't any item in the list,
        // then the text is exactly this: {NULL}, in this case, just create a new ArrayList)
        ArrayList<String> list;

        text = text.substring(1, text.length() - 1); // remove the { }
        if (text.equals("NULL"))
        { // check if list is empty
            list = new ArrayList<>();
        }
        else
        {
            String[] allIngredientsArray = text.split(",");
            List<String> simpleSecondList = Arrays.asList(allIngredientsArray);
            list = new ArrayList<>(simpleSecondList);
        }

        return list;
    }

    /**
     * Gets a list with top three dishes
     *
     * @return the list containing menu items
     * @throws SQLException
     */
    public ArrayList<MenuItem> getTopThreeDishes() throws SQLException
    {
        ArrayList<MenuItem> topThreeDishes = new ArrayList<>();

        try (Connection connection = DatabaseConnImp.getConnection())
        {
            String sqlTopDishes =
                "SELECT orderItem.itemName, sum(orderItem.quantity) as sum FROM orderItem WHERE orderNumber in "
                    + "(SELECT orderNumber FROM \"order\" WHERE collected = true) GROUP BY itemName ORDER BY sum DESC LIMIT 3";

            PreparedStatement statement = connection.prepareStatement(
                sqlTopDishes);
            ResultSet set = statement.executeQuery();

            while (set.next())
            {

                String itemName = set.getString("itemname");

                ArrayList<String> ingredients = new ArrayList<>();

                String sqlIngredients =
                    "SELECT name FROM ingredient WHERE id IN (SELECT ingredientId FROM menuItemIngredient WHERE itemName = '"
                        + itemName + "'";
                PreparedStatement statementIngredient = connection.prepareStatement(
                    sqlIngredients);
                ResultSet setIngredient = statementIngredient.executeQuery();

                while (setIngredient.next())
                {
                    String name = setIngredient.getString("name");
                    ingredients.add(name);
                }

                String sqlGetMenuItem =
                    "SELECT * FROM menuItem WHERE name = '" + itemName + "'";

                PreparedStatement statement1 = connection.prepareStatement(
                    sqlGetMenuItem);
                ResultSet set1 = statement1.executeQuery();

                while (set.next())
                {
                    double price = set1.getDouble("price");
                    String imgPath = set1.getString("imgpath");

                    MenuItem menuItem = new MenuItem(itemName, ingredients,
                        price, imgPath);
                    topThreeDishes.add(menuItem);
                }
            }

        }
        return topThreeDishes;
    }

    /**
     * Gets a list with number of orders for each week day(Monday-Friday)
     * @return the list containing Integer
     * @throws SQLException
     */
    public ArrayList<Integer> getNumberOfOrders() throws SQLException
    {
        ArrayList<Integer> ordersNumber = new ArrayList<>();

        try (Connection connection = DatabaseConnImp.getConnection())
        {
            LocalDate date = DateHelper.getCurrentAvailableMondayForStatistic();
            for (int i = 0; i < 5; i++)
            {
                String sql =
                    "SELECT count(\"order\") FROM \"order\" WHERE collected = true AND date = '"
                        + date + "'";

                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet set = statement.executeQuery();

                while (set.next())
                {
                    Integer number = set.getInt("count");
                    ordersNumber.add(number);
                }

                date = date.plusDays(1);
            }
        }
        return ordersNumber;
    }

    /**
     * Gets a list with income of each week day (Monday-Friday)
     * @return the list containing Integer
     * @throws SQLException
     */
    public ArrayList<Double> getIncome() throws SQLException
    {
        ArrayList<Double> incomes = new ArrayList<>();

        try (Connection connection = DatabaseConnImp.getConnection())
        {
            LocalDate date = DateHelper.getCurrentAvailableMondayForStatistic();
            for (int i = 0; i < 5; i++)
            {
                String sql = "SELECT orderItem.itemName, sum(orderItem.quantity) * menuItem.price as totalPrice "
                    + "FROM orderItem, menuItem "
                    + "WHERE orderItem.itemName = menuItem.name "
                    + "AND orderItem.orderNumber in (SELECT orderNumber FROM \"order\" WHERE date = '" + date +"') "
                    + "GROUP BY orderItem.itemName, menuItem.price";

                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet set = statement.executeQuery();

                double sum = 0;

                while (set.next())
                {
                   double totalPrice = set.getDouble("totalprice");
                   sum += totalPrice;
                }

                incomes.add(sum);
                date = date.plusDays(1);
            }
        }
        return incomes;
    }
}

