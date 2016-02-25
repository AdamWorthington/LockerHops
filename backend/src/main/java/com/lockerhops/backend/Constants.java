package com.lockerhops.backend;

/**
 * Contains the client IDs and scopes for allowed clients consuming the lockerHopsAPI.
 */
public class Constants {
  public static final String WEB_CLIENT_ID = "replace this with your web client ID";
  public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
  public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
  public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
  public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
  public static final String API_EXPLORER_CLIENT_ID = com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID;

  // URL to connect to the database TODO: Once we set up company google acc
  public static final String DATABASE_URL = "";

  // Import path for the Google Driver to enable the google cloud api
  public static final String GOOGLE_DRIVER = "com.mysql.jdbc.GoogleDriver";
}
