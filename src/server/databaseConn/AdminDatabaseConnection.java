package server.databaseConn;

import shared.UserType;
import transferobjects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** A class handling database connection for requests made by admin
 * @author Beatricia
 * @version 1.0
 */
public class AdminDatabaseConnection {

    /**
     * Updating the employee table from the database. If the employee is:
     * accepted -> set the accepted column to true
     * declined -> delete the employee from the table
     * @param username the username of the employee that it is on hold
     * @param accept true if the employee is accepted
     * @throws SQLException when the table does not exist
     */
    public void handlePendingEmployee(String username, boolean accept) throws SQLException {
        try(Connection connection = DatabaseConnImp.getConnection()) {
            PreparedStatement statement;
            if(accept) {
                String str = "UPDATE employee SET accepted = true\n" +
                        "WHERE username = '" + username  + "'";
                statement = connection.prepareStatement(str);
            } else {
                String str = "DELETE from employee\n" +
                        "where username = '" + username + "'";
                statement = connection.prepareStatement(str);
            }
            statement.execute();
        }
    }

    /**
     * Getting all the pending employees from the database that are not accepted yet
     * @return an arrayList of employees that are not accepted
     * @throws SQLException when the table does not exist
     */
    public ArrayList<User> getAllPendingEmployees() throws SQLException {
        ArrayList<User> pendingEmployees = new ArrayList<>();
        try(Connection connection = DatabaseConnImp.getConnection()) {
            String str = "SELECT username, firstName, lastName " +
                    "FROM employee\n" +
                    "WHERE employee.accepted=false;";
            PreparedStatement statement = connection.prepareStatement(str);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {
                String userName = set.getString("username");
                String firstName = set.getString("firstname");
                String lastName = set.getString("lastname");

                    User user = new User(userName, UserType.EMPLOYEE, firstName, lastName);
                    pendingEmployees.add(user);

            }
            return pendingEmployees;
        }
    }
}

