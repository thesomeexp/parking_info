package com.someexp.parking_info.interceptor;

import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.service.UserService;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSRFInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            System.out.println("拦截器:未登录");
            return false;
        }
        System.out.println("preHandle获取的token" + request.getHeader("_csrf_token"));
        String req_token = request.getHeader("_csrf_token");
        String session_token = (String) request.getSession().getAttribute("_csrf_token");
        if (req_token != null) {
            if (req_token.equals(session_token)) {
                System.out.println("拦截器token验证成功");
                return true;
            } else {
                System.out.println("拦截器token验证失败");
                return false;
            }
        } else {
            System.out.println("拦截器:token为空");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion");
    }
}
