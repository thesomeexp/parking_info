package com.someexp.parking_info.util;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class CSRFTokenUtil {
    public static String generate(){
        return UUID.randomUUID().toString();
    }
}
