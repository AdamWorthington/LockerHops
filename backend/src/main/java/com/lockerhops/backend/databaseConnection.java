package com.lockerhops.backend;

/**
 * Created by Scott on 2/24/2016.
 *
 * Functions to access the database
 *
 * These functions will retrieve information from the
 * SQL tables given a type of query and the information
 * required to fulfill that query
 *
 * SAMPLE FUNCTION FROM PREVIOUS PROJECT
 * Delete when you get the idea
 * The MyBean object is just to tell the calling API
 * function what happened. Makes for easier debugging later
 *
 * //Contact the database and store a single profile in it
     public static MyBean storeProfile(Profile input) {

         String url = Constants.DATABASE_URL;
         MyBean bean = new MyBean();
         String statement = "";
         int success = 0;

         try {
             // Load GoogleDriver class at runtime
             Class.forName(Constants.GOOGLE_DRIVER);

             // Open connection to database
             Connection conn = DriverManager.getConnection(url);
             try {
                 //Sanity check on argument
                 if (input == null) {
                     bean.setBool(false);
                     bean.setData("Sent Database a null Profile");
                     databaseError(Constants.DB_ERROR.BAD_INPUT_ERROR);
                     return bean;
                 }
                    else {
                     statement = "INSERT INTO profile (firstName, lastName, password, email, city, lat, lng, description)" +
                     " VALUES ('" +
                     input.getFirstName() + "', '" +
                     input.getLastName() + "', '" +
                     input.getPassword() + "', '" +
                     input.getEmail() + "', '" +
                     input.getCity() + "', " +
                     input.getLat() + ", " +
                     input.getLng() + ", '" +
                     input.getDescription() + "')";
                     PreparedStatement stmt = conn.prepareStatement(statement);

                     // Returns 1 on success, 0 on fail
                     success = stmt.executeUpdate();
                 }
             }
             finally {
                 // Always make sure to close database connection
                 conn.close();
             }
         }
         catch (Exception e){
            //Handle unsuccessful connection to database here
             e.printStackTrace();
         }

         if (success == 1) {
            bean.setBool(true);
            bean.setData("Successful insert");
         }
         else {
            databaseError(Constants.DB_ERROR.INSERT_ERROR);
            bean.setBool(false);
            bean.setData("Error: Inserting into database failed");
         }
         return bean;
    }
 *
 */


public class databaseConnection {
    //TODO
}
