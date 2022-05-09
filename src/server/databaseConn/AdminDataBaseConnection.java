package server.databaseConn;

import transferobjects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDataBaseConnection {
    public void handlePendingEmployee(String username, boolean accept) throws SQLException {
        try(Connection connection = DatabaseConnImp.getConnection()) {
            String str = "INSERT INTO EMPLOYEE_PENDING (username, accept) VALUES (?, ?);";// DO WE INSERT IN THE EXISTING TABLE ?
            PreparedStatement statement = connection.prepareStatement(str);

            statement.setString(1,username);
            statement.setBoolean(2, accept);
        }
    }

    public ArrayList<User> getAllPendingEmployees() throws SQLException {
        return null;
    }
}
