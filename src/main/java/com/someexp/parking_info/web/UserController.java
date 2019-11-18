package com.someexp.parking_info.web;

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

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    InfoService infoService;

    /**
     * 检查参数是否为空, 用户名手机号是否存在
     * 生成加密后的密码
     * 插入新用户
     * @param name
     * @param password
     * @param phone
     * @return
     */
    @PostMapping("/register")
    public Object register(String name, String password, String phone) {
        try {
            if (MyTools.isStringEmpty(name))
                return Result.fail(MagicVariable.USERNAME_IS_EMPTY);
            if (MyTools.isStringEmpty(password))
                return Result.fail(MagicVariable.PASSWORD_IS_EMPTY);
            if (MyTools.isStringEmpty(phone))
                return Result.fail(MagicVariable.PHONE_IS_EMPTY);

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
     *检查手机号, 密码
     *登录后设置user到sesseion里
     * @param phone
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Object login(String phone, String password, HttpSession session) {
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
            session.setAttribute("user", user);
            return Result.success(MagicVariable.LOGIN_SUCCESS);
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
     * 注销, 销毁session里的user
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public Object logout(HttpSession session) {
        try {
            Subject subject = SecurityUtils.getSubject();
            // shiro的一个bug?在logout前执行就会报错
//            session.invalidate();
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
