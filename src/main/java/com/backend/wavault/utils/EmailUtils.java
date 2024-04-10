package com.backend.wavault.utils;

import jakarta.servlet.http.HttpServletRequest;

public class EmailUtils {

    public static String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
    public static String frontEndAppUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":9092"+request.getContextPath();
    }
}
