package com.someexp.parking_info.service;

import com.someexp.parking_info.dao.UserDAO;
import com.someexp.parking_info.pojo.Info;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
//@CacheConfig(cacheNames="users")
public class UserService {

    @Autowired
    UserDAO userDAO;
    @Autowired
    InfoService infoService;

    public boolean isNameExist(String name){
        User user = getByName(name);
        return null!=user;
    }

    public boolean isPhoneExist(String phone){
        User user = getByPhone(phone);
        return null!=user;
    }


    public User getByName(String name) {
        return userDAO.findByName(name);
    }

    public User getByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    public void add(User user) {
        userDAO.save(user);
    }

    public User get(String name, String password) {
        return userDAO.getByNameAndPassword(name,password);
    }

    // 通过id查询用户名
//    @Cacheable(key="'users-username-'+#p0")
    public String getUsernameById(int id){
        User user = userDAO.getOne(id);
        return user.getName();
    }

    public boolean isUserLocationNearParking(int pid, double x, double y) throws Exception{
        double limit = MagicVariable.INFO_ADD_TEMP_LIMIT;
        Info info = infoService.getById(pid);
        if (info.getLongitude() > x-limit && info.getLongitude() < x+limit)
            if (info.getLatitude() > y-limit && info.getLatitude() < y+limit)
                return true;
        return false;
    }


}