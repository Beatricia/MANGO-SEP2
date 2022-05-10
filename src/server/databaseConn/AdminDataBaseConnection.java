package server.databaseConn;

import transferobjects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDataBaseConnection {
    public void handlePendingEmployee(String username, boolean accept) throws SQLException {
        try(Connection connection = DatabaseConnImp.getConnection()) {
            PreparedStatement statement;
            if(accept) {
                String str = "UPDATE employee SET accepted = true\n" +
                        "WHERE username = " + username;
                statement = connection.prepareStatement(str);
            } else {
                String str = "DELETE from employee\n" +
                        "where username = " + username;
                statement = connection.prepareStatement(str);
            }
            statement.execute();
        }
    }

    public ArrayList<User> getAllPendingEmployees() throws SQLException {
        ArrayList<User> pendingEmployees;
        try(Connection connection = DatabaseConnImp.getConnection()) {
            String str = "SELECT username, employee.accepted\n" +
                    "FROM employee\n" +
                    "WHERE employee.accepted=false;";
            PreparedStatement statement = connection.prepareStatement(str);
            statement.execute();
        }
        return null;
    }
}

class Main {
    public static void main(String[] args) throws SQLException {
        boolean accept = true;
        String username = "Greg";

        try(Connection connection = DatabaseConnImp.getConnection()) {
            PreparedStatement statement;
            if(accept) {
                String str = "UPDATE employee SET accepted = true\n" +
                        "WHERE username = '" + username + "'";
                statement = connection.prepareStatement(str);
            } else {
                String str = "DELETE from employee\n" +
                        "where username = '" + username +"'";
                statement = connection.prepareStatement(str);
            }
            statement.execute();
        }

    }
}
