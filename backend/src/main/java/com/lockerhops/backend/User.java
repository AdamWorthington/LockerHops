package com.lockerhops.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Daniel on 3/18/2016.
 */
public class User {
    int userID;
    String username;
    String password;
    String email;

    //Database information
    static final String driver 		= DatabaseAccessors.driver;
    static final String jdbcUrl 	= DatabaseAccessors.jdbcUrl;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public static int newUser(String username, String password, String email) {
        //PERFORM ARGUMENT VALIDATION HERE
        if(!DatabaseAccessors.stringIsValid(username)) {
            System.out.println("Invalid username in newUser");
            return -1;
        }
        if(!DatabaseAccessors.stringIsValid(password)) {
            System.out.println("Invalid password in newUser");
            return -1;
        }
        if(!DatabaseAccessors.stringIsValid(email)) {
            System.out.println("Invalid email in newUser");
            return -1;
        }


        //The prepared statement for this order
        PreparedStatement stmt	= null;

        //The connection to the database
        Connection conn	= null;

        //The SQL query to update this order's information
        String 				query = "INSERT INTO Users (`Username`, `Password`, `Email`) VALUES (?, ?, ?);";

        //Check we can get the driver
        try {
            System.out.print("Checking driver in newUser: ");
            Class.forName(driver);
            System.out.println("SUCCESS");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Cannot find the driver in the classpath:    ");
            e.printStackTrace();
            return -1;
        }

        try {
            //Acquire a connection using the specified driver
            System.out.print("Creating connection: ");
            conn = DriverManager.getConnection(jdbcUrl);
            System.out.println("SUCCESS");

            //Prepare the statement based on the given query
            System.out.print("Preparing statement: ");
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            System.out.println("SUCCESS");

            //Add values to the prepared statement
            System.out.print("Setting statement values: ");
            stmt.setString(1, username);
            System.out.print("1 ");
            stmt.setString(2, password);
            System.out.print("2 ");
            stmt.setString(3, email);
            System.out.println("3");

            //Execute the statement to insert this order into the database
            System.out.print("Executing statement: ");
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();


            int ret = rs.getInt(1);

            //
            if (ret > 0) {
                System.out.println("SUCCESS");
                return ret;
            }
        }
        catch(SQLException e) {
            System.out.println("Unable to create and execute statement:    ");
            e.printStackTrace();
            return -1;
        }

        //This is only reached if there was no value assigned to the insertion, meaning it failed, or an exception occured
        System.out.println("FAILURE");
        return -1;
    }
}
