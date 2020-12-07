package gov.usaf;

import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    //TODO: how do we want to store data? Database should work, as should files. Ideally databases.
    public static void main(String[] args) throws Exception {
        //System.out.println("This application is a library. To use, please add the library to your application and create a new LeaveTracker object for managing leave.");

        HashMap<String, String> opt = Options.splitArgs(args);
        Connection connection = DriverManager.getConnection(opt.getOrDefault('d',"./leave.sqlite"));
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        // TODO: database stuff in all classes. Pass database to constructors probably.
    }
}
