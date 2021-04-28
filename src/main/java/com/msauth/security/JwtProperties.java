package com.msauth.security;

public class JwtProperties {
		
    public static int EXPIRATION_TIME = 28800000; // 1 hour = 3600000 miliseconds
    public static String TOKEN_PREFIX = "Bearer ";
    public static String HEADER_STRING = "Authorization";
    public static String SECRET = "53cr3T@"; 
   
    
}
