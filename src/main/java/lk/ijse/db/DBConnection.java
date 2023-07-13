package lk.ijse.db;

import javafx.beans.property.Property;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.PropertyPermission;

public class DBConnection {
    private static DBConnection dbConnection;

    private final static String url = "jdbc:mysql://localhost:3306/play_tech";
    private final static Properties props = new Properties();

    private Connection connection;


    {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(url, props);
    }

    public static DBConnection getInstance() throws SQLException {
        if(dbConnection == null){
            return dbConnection = new DBConnection();
        }else{
            return dbConnection;
        }
    }

    public Connection getConnection(){
        return connection;
    }
}
