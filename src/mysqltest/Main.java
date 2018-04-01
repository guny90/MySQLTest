/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltest;

import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;
import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import mysqltest.db.DBHelper;
import mysqltest.model.Database;

/**
 *
 * @author gunel
 */
public class Main {

    public static int N;
    public static int dbN;
    public static int tableN;
    public static int rowN;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = null;
        Console console = null;

        ArrayList<Database> dbs = new ArrayList<>();

        if (args.length != 4) {
            System.out.println("Please enter the arguments: 1 - number of hosts, 2 - number of dbs, 3 - number of tables for each db, 4 - number of rows for each table");
            System.exit(-1);
        }

        N = Integer.parseInt(args[0]);
        dbN = Integer.parseInt(args[1]);
        tableN = Integer.parseInt(args[2]);
        rowN = Integer.parseInt(args[3]);

        console = System.console();

        if (console != null) {
            for (int i = 1; i <= N; i++) {
                Database db = new Database();
                db.setHostName(console.readLine("HOSTNAME of DB" + i + ": "));
                db.setUserName(console.readLine("USERNAME for " + db.getHostName() + ": "));
                db.setPassword(new String(console.readPassword("PASSWORD for " + db.getHostName() + ": ")));
                dbs.add(db);
            }
        } else {
            scanner = new Scanner(System.in);
            for (int i = 1; i <= N; i++) {
                Database db = new Database();
                System.out.println("HOSTNAME of DB" + i);
                db.setHostName(scanner.nextLine());
                System.out.println("USERNAME for " + db.getHostName());
                db.setUserName(scanner.nextLine());
                System.out.println("PASSWORD for " + db.getHostName());
                db.setPassword(scanner.nextLine());
                dbs.add(db);
            }
        }

        for (Database db : dbs) {
            db.setConn(new ArrayList<>());
            db.setThread(new ArrayList<>());
            for (int i = 1; i <= dbN; i++) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String host = null;
                        Connection conn = null;
                        String query = "select substr(@@hostname, 1,3)";
                        String tableQuery = null;
                        String insertQuery = null;
                        PreparedStatement statement = null;
                        ResultSet resultSet = null;
                        int resultCode = 0;
                        int dbRandom = new Random().nextInt(10000000) + 1000;
                        try {
                            conn = new DBHelper().getConnection(db);
                            db.getConn().add(conn);
                            statement = conn.prepareStatement(query);
                            resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                host = resultSet.getString(1);
                            }

                            statement = conn.prepareStatement("set autocommit=off");
                            statement.executeQuery();

                            query = "create database " + host + "_" + dbRandom;
                            resultCode = statement.executeUpdate(query);
                            Logger.getLogger(Main.class.getName()).log(Level.INFO, query);
                            for (int k = 1; k <= tableN; k++) {
                                tableQuery = "create table " + host + "_" + dbRandom
                                        + ".tbl_" + k + " (col int not null AUTO_INCREMENT, "
                                        + "v1 varchar(255), "
                                        + "v2 varchar(255), "
                                        + "v3 varchar(255), " + "v4 varchar(255), "
                                        + "v5 varchar(255), " + "v6 varchar(255), "
                                        + "v7 varchar(255), "
                                        + "primary key(col))";
                                resultCode = statement.executeUpdate(tableQuery);
                                Logger.getLogger(Main.class.getName()).log(Level.INFO, tableQuery);
                                for (int j = 1; j <= rowN; j++) {
                                    insertQuery = "insert delayed into "
                                            + host + "_" + dbRandom + ".tbl_" + k
                                            + " (v1, v2, v3, v4, v5, v6, v7) "
                                            + " values (" //+ j + ", "
                                            + Database.CLOB + ", "
                                            + Database.CLOB + ", "
                                            + Database.CLOB + ", "
                                            + Database.CLOB + ", "
                                            + Database.CLOB + ", "
                                            + Database.CLOB + ", "
                                            + Database.CLOB + ") ";

                                    try {
                                        statement = conn.prepareStatement(insertQuery);
                                        resultCode = statement.executeUpdate(insertQuery);

                                        Logger.getLogger(Main.class.getName()).log(Level.INFO, insertQuery);
                                        Logger.getLogger(Main.class.getName()).log(Level.INFO, "resultCode=" + resultCode);
                                    } catch (MySQLTransactionRollbackException ex) {
                                        try {
                                            statement = conn.prepareStatement(insertQuery);
                                            resultCode = statement.executeUpdate(insertQuery);
                                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (SQLException ex1) {
                                            statement = conn.prepareStatement("commit");
                                            statement.executeQuery();
                                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
                                        } finally {
                                            try {
                                                statement.close();
                                                conn.close();
                                            } catch (SQLException e) {
                                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
                                            }
                                        }
                                    }
                                }
                            }

                            statement = conn.prepareStatement("commit");
                            statement.executeQuery();

                        } catch (SQLException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                if (statement != null) {
                                    statement.close();
                                }

                                if (conn != null) {
                                    conn.close();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                db.getThread().add(thread);
            }

        }

        for (Database db : dbs) {
            for (Thread thread : db.getThread()) {
                thread.start();
            }
        }

    }

}
