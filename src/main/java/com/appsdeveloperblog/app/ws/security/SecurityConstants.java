package com.appsdeveloperblog.app.ws.security;

import com.appsdeveloperblog.app.ws.SpringApplicationContext;

public class SecurityConstants {
  public static final long EXPIRATION_TIME = 864000000; // 10 days
  public static final String TOKEN_PREFIX = "Bearer ";
  public final static String HEADER_STRING = "Authorization";
  public final static String SIGN_UP_URL = "/users";

  public static String getTokenSecret() {
    AppProperties appProperties = (AppProperties)SpringApplicationContext.getBean("AppProperties");

    return appProperties.getTokenSecret();
  }
}
