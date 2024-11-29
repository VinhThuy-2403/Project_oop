/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Hp
 */
public class SQLServerConnection {
    public static Connection init(String dbName, String dbusername, String dbpassword) throws SQLException, ClassNotFoundException{
        String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
	String dbURL = "jdbc:sqlserver://localhost:1433";

        String connectionURL = dbURL + ";databaseName=" + dbName + 
                ";encrypt=true;trustServerCertificate=true";
        Connection conn = null;
        
        try {
            Class.forName(dbDriver);
            conn = (Connection) DriverManager.getConnection(connectionURL, dbusername, dbpassword);
            System.out.println("Succesful!");
        }
        catch (ClassNotFoundException | SQLException ex){
            System.out.println("Failure!");
        }
        
        return conn;
    }
    
    public static Connection init() throws SQLException, ClassNotFoundException {
        return init("PROJECT", "sa", "123");
    }
}
