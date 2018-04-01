/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltest.model;

import com.mysql.jdbc.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author gunel
 */
public class Database {
    private String hostName;
    private String userName;
    private String password;
    private ArrayList<Connection> connList;
    private ArrayList<Thread> threadList;
    public final static String DUMMY = "dummy";
    public final static String CLOB = "0x89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796";

    public Database() {
    }

    public Database(String hostName, String userName, String password) {
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Connection> getConn() {
        return connList;
    }

    public void setConn(ArrayList<Connection> connList) {
        this.connList = connList;
    }

    public ArrayList<Thread> getThread() {
        return threadList;
    }

    public void setThread(ArrayList<Thread> threadList) {
        this.threadList = threadList;
    }

    @Override
    public String toString() {
        return "Database{" + "hostName=" + hostName + ", userName=" + userName + 
                ", password=FUCKYOU:)" + ", connList=" + connList + ", threadList=" + threadList + '}';
    }
    
    
    
}
