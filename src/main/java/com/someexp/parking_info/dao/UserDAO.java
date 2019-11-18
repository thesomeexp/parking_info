package com.someexp.parking_info.dao;

import com.someexp.parking_info.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User,Integer> {

    User findByName(String name);
    User findByPhone(String phone);

    User getByNameAndPassword(String name, String password);


}