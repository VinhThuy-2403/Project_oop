/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_oop.Connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Hp
 */
public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        return (Connection) SQLServerConnection.init();
    }
}
