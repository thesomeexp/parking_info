package com.someexp.parking_info.filter;

import ch.qos.logback.core.pattern.color.MagentaCompositeConverter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.Result;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

public class ShiroAuthFilter extends FormAuthenticationFilter {

    // 重写shiro权限过滤后的页面变为返回json数据
   @Override
   protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
       try {
           HttpServletResponse httpServletResponse = (HttpServletResponse)response;
           httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
           PrintWriter out = httpServletResponse.getWriter();
           JSONObject responseJSONObject = JSONObject.fromObject(Result.fail(MagicVariable.UN_LOGIN));
           out.println(responseJSONObject);
           out.flush();
           out.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return false;
   }


}
