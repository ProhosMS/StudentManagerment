package datamanager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Prohos
 */
public class ConnectionManager {
    
    private static Connection con;
    
    public static Connection  getConnection() throws ClassNotFoundException, SQLException{
        // Load Driver
        Class.forName("org.postgresql.Driver");
        String url  = "jdbc:postgresql://192.168.1.2:5432/StudentManagement";
        //String url  = "jdbc:postgresql://localhost:5432/StudentMS";
        if(con==null)
            con = DriverManager.getConnection(url, "postgres", "123");
        return con;
    }
    
}
