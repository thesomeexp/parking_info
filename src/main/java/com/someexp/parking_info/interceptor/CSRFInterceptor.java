package com.someexp.parking_info.interceptor;

import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.service.UserService;
import com.someexp.parking_info.util.CSRFTokenUtil;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CSRFInterceptor implements HandlerInterceptor {

    /**除了login和listNearBy不需要验证csrf_token其余都要
     * 判断当前session是否登录
     * 获取请求头token, 获取服务器session存储的token
     * 验证成功生成新token, 一个放在session一个在返回的时候在header带回去
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (request.getRequestURI().endsWith("/error"))
            return true;
        if (request.getRequestURI().endsWith("Page"))
            return true;

        String req_token = request.getHeader("_csrf_token");
        HttpSession session = request.getSession();
        String server_token = (String) session.getAttribute("_csrf_token");
        if (req_token != null) {
            if (req_token.equals(server_token)) {
                String csrfToken = CSRFTokenUtil.generate();
                session.setAttribute("_csrf_token", csrfToken);
                response.addHeader("_csrf_token", csrfToken);
                System.out.println("CSRF_TOKEN验证成功:" + req_token + "新" + csrfToken);
                return true;
            } else {
                System.out.println("==========" + request.getRequestURI() + "==========");
                System.err.println("CSRF拦截器token验证失败");
                System.out.println("客户端提供的token" + req_token);
                System.out.println("服务端提供的token" + server_token);
                return false;
            }
        } else {
            System.out.println("==========" + request.getRequestURI() + "==========");
            System.err.println("CSRF拦截器:token为空");
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
