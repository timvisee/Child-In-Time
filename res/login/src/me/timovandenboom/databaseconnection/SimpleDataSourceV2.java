package me.timovandenboom.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
   A simple data source for getting database connections. 
*/
public class SimpleDataSourceV2
{

   private static String dbserver;
   public static String database;
   private static String username;
   private static String password;

   private static Connection activeConn;

   /**
      Initializes the data source.
      Checks if MySQL Driver is found
      contains the database driver,
      Fill variabels dbserver, database, username, and password
    *
    * TODO get variabels from a configuration file!!! 
    * or credentials manager
    * Hardcoded is bad code!!!
   */
   private static void init(String username1, String password1)
         
   {
      try {
        String driver = "com.mysql.jdbc.Driver";
        Class.forName(driver);
      }
      catch (ClassNotFoundException e) {
          System.out.println(e);
      }


      dbserver="localhost";
      database="n6";
      username = username1;
      password = password1;
      
      
   }

   /**
      Gets a connection to the database.
     * @param username
     * @param password
      @return the database connection
     * @throws java.sql.SQLException
   */
   public static Connection getConnection(String username, String password) throws SQLException
   {
       if (activeConn==null) {
           init(username, password);
           activeConn=createConnection();
       }
       else {
           if (!activeConn.isValid(0)) {
               activeConn=createConnection();
           }
       }

       return activeConn;

   }

   private static Connection createConnection() throws SQLException
   {

        String connectionString = "jdbc:mysql://" + dbserver + "/" + database + "?" +
                "user=" + username + "&password=" + password;

       return DriverManager.getConnection(connectionString);
   }
   
   public static void closeConnection() {
       if (activeConn!=null) {
           try {
                activeConn.close();
           }
           catch(SQLException e) {
               //to catch and do nothing is the best option
               //don't know how to recover from this exception
               
           }
           finally {
               activeConn=null;
           }
               
       }
       
   }
}