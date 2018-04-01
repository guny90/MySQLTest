/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mysqltest.model.Database;

/**
 *
 * @author gunel
 */
public class DBHelper {
    
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String url;
    
    public DBHelper() {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch(Exception e) {
            e.getStackTrace();
        }
    }
    
    public Connection getConnection(Database db)    {
        Connection conn = null;
        Properties props = null;
        Statement statement = null;
        try {
            props = new Properties();
            props.setProperty("user",db.getUserName());
            props.setProperty("password",db.getPassword());
            props.setProperty("useSSL","false");
            props.setProperty("autoReconnect","true");
            props.setProperty("allowMultiQueries","true");
            
            conn = DriverManager.getConnection("jdbc:mysql://"+db.getHostName()+"/", props);
            statement = conn.createStatement();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return conn;
    }
    
}
