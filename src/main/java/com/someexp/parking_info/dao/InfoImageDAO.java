package com.someexp.parking_info.dao;

import java.util.List;

import com.someexp.parking_info.pojo.InfoImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoImageDAO extends JpaRepository<InfoImage,Integer>{
    public List<InfoImage> findByPidOrderById(int pid);

}