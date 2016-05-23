/**
 * Created by harshit on 22/5/16.
 */
import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.MqttChat;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.UUID;

public class SqlHelper {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL;

    //  Database credentials
    final String USER;
    final String PASS;

    public SqlHelper(String dbUrl, String dbUser, String dbPwd) {
        DB_URL = dbUrl;
        USER = dbUser;
        PASS = dbPwd;
    }

    public void select(String sql, PublisherImpl publisher, MqttChatMessage mqttChatMessage, String clientId) throws UnsupportedEncodingException {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String output = "";
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    output = output + columnValue + " " + rsmd.getColumnName(i);
                    output = output + "\t";
                }
                output = output + "\n";
            }
            output = URLEncoder.encode(output, "UTF-8");
            new MqttChat(mqttChatMessage.senderUid,
                    UUID.randomUUID().toString(), output, false, ChatConstant.ChatType.TEXT, clientId).sendStanza(publisher);
            resultSet.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            String output = URLEncoder.encode(se.getMessage(), "UTF-8");
            new MqttChat(mqttChatMessage.senderUid,
                    UUID.randomUUID().toString(), output, false, ChatConstant.ChatType.TEXT, clientId).sendStanza(publisher);
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            String output = URLEncoder.encode(e.getMessage(), "UTF-8");
            new MqttChat(mqttChatMessage.senderUid,
                    UUID.randomUUID().toString(), output, false, ChatConstant.ChatType.TEXT, clientId).sendStanza(publisher);
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main

    public void genericSql(String sql, PublisherImpl publisher, MqttChatMessage mqttChatMessage, String clientId) throws UnsupportedEncodingException {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();
            int update = stmt.executeUpdate(sql);
            System.out.println("Inserted records into the table...");
            new MqttChat(mqttChatMessage.senderUid,
                    UUID.randomUUID().toString(), "done", false, ChatConstant.ChatType.TEXT, clientId).sendStanza(publisher);

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            String output = URLEncoder.encode(se.getMessage(), "UTF-8");
            new MqttChat(mqttChatMessage.senderUid,
                    UUID.randomUUID().toString(), output, false, ChatConstant.ChatType.TEXT, clientId).sendStanza(publisher);
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            String output = URLEncoder.encode(e.getMessage(), "UTF-8");
            new MqttChat(mqttChatMessage.senderUid,
                    UUID.randomUUID().toString(), output, false, ChatConstant.ChatType.TEXT, clientId).sendStanza(publisher);
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end SqlHelper