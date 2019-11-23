package com.someexp.parking_info.web;

import com.someexp.parking_info.util.CSRFTokenUtil;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.MyTools;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.service.UserService;
import com.someexp.parking_info.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    InfoService infoService;

    /**
     * 检查timestamp
     * 检查参数是否为空
     * 检查用户名, 手机号, 密码长度是否合法
     * 用户名手机号是否存在
     * 生成加密后的密码
     * 插入新用户
     * @param name
     * @param password
     * @param phone
     * @return
     */
    @PostMapping("/register")
    public Object register(String name, String password, String phone, HttpServletRequest request) {
        try {
            long req_timestamp = Long.parseLong(request.getHeader("timestamp"));
            long server_timestamp = System.currentTimeMillis();
            if (server_timestamp - req_timestamp > MagicVariable.REPLAY_ATTACK_INTERVAL){
                return Result.fail(MagicVariable.REPLAY_ATTACK_DETECT);
            }

            if (MyTools.isStringEmpty(name))
                return Result.fail(MagicVariable.USERNAME_IS_EMPTY);
            if (MyTools.isStringEmpty(password))
                return Result.fail(MagicVariable.PASSWORD_IS_EMPTY);
            if (MyTools.isStringEmpty(phone))
                return Result.fail(MagicVariable.PHONE_IS_EMPTY);

            if (!MyTools.isValueLengthLegal(name, MagicVariable.USER_NAME_MAX_LEN) ||
                    !MyTools.isValueLengthLegal(phone, MagicVariable.USER_PASSWORD_MAX_LEN) ||
                    !MyTools.isValueLengthLegal(password, MagicVariable.USER_PASSWORD_MAX_LEN))
                return Result.fail(MagicVariable.PARAM_VALUES_TOO_MAX);

            if (userService.isNameExist(name))
                return Result.fail(MagicVariable.USERNAME_IS_EXIST);
            if (userService.isPhoneExist(phone))
                return Result.fail(MagicVariable.PHONE_IS_EXIST);

            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = MagicVariable.HASH_TIMES;
            String algorithmName = MagicVariable.ALGORITHM_NAME;
            String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setSalt(salt);
            user.setPassword(encodedPassword);
            user.setRole(MagicVariable.DEFAULT_ROLE);
            userService.add(user);
            return Result.success(MagicVariable.REGISTER_SUCCESS);
        } catch (Exception e) {
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }
    }

    /**
     * 检查timestamp
     * 检查手机号, 密码是否为空
     * 登录后设置user, _csrf_token到sesseion里
     * 返回_csrf_token
     * @param phone
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Object login(String phone, String password, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (MyTools.isStringEmpty(phone))
                return Result.fail(MagicVariable.PHONE_IS_EMPTY);
            if (MyTools.isStringEmpty(password))
                return Result.fail(MagicVariable.PASSWORD_IS_EMPTY);
            /**
             * 使用shiro编写认证操作
             */
            // 1.获取Subject
            Subject subject = SecurityUtils.getSubject();
            // 2.封装用户数据
            UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
            // 3.执行登录方法
            subject.login(token);
            User user = (User)subject.getPrincipal();
            String csrfToken = CSRFTokenUtil.generate();
            session.setAttribute("user", user);
            session.setAttribute("_csrf_token", csrfToken);
            response.addHeader("_csrf_token", csrfToken);
//            map.put("_csrf_token", csrfToken);
            return Result.success();
        } catch (UnknownAccountException e){
            // 登录失败用户名不存在
            return Result.fail(MagicVariable.USER_PHONE_NOT_EXIST);
        } catch (IncorrectCredentialsException e){
            // 密码错误
            return Result.fail(MagicVariable.USER_PASSWORD_ERROR);
        } catch (AuthenticationException e) {
            return Result.fail(MagicVariable.LOGIN_FAIL);
        } catch (Exception e) {
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }
    }

    /**
     * 注销, 销毁session
     * @return
     */
    @PostMapping("/logout")
    public Object logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                subject.logout();
                return Result.success(MagicVariable.LOGOUT_SUCCESS);
            } else {
                return Result.fail(MagicVariable.UN_LOGIN);
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }
    }

}
