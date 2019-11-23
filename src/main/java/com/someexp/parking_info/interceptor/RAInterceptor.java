package com.someexp.parking_info.interceptor;

import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.util.MagicVariable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class RAInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (request.getRequestURI().endsWith("/error"))
            return true;
        if (request.getRequestURI().endsWith("Page"))
            return true;

        System.out.println("==========" + request.getRequestURI() + "==========");
//        Enumeration enu=request.getHeaderNames();
//        while(enu.hasMoreElements()){
//            String paraName=(String)enu.nextElement();
//            System.out.println(paraName+": "+request.getHeader(paraName));
//        }

        String req_timestamp = request.getHeader("timestamp");
        if (req_timestamp != null) {
            long timestamp = Long.parseLong(request.getHeader("timestamp"));
            long server_timestamp = System.currentTimeMillis();
            if (server_timestamp - timestamp > MagicVariable.REPLAY_ATTACK_INTERVAL){
                System.out.println("==========" + request.getRequestURI() + "==========");
                System.err.println("重放拦截器:req_timestamp超时");
                System.out.println("客户端提供的timestamp" + req_timestamp);
                System.out.println("服务端提供的timestamp" + server_timestamp);
                return false;
            } else {
                return true;
            }
        } else {
            System.out.println("==========" + request.getRequestURI() + "==========");
            System.err.println("重放拦截器:req_timestamp为空");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

}
