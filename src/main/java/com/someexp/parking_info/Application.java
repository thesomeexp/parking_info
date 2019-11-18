package com.someexp.parking_info;

import com.someexp.parking_info.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@EnableCaching
public class Application {

//    static {
//        PortUtil.checkPort(6379,"Redis 服务端",true);
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}