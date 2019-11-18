package com.someexp.parking_info.dao;

import com.someexp.parking_info.pojo.Temp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TempDAO extends JpaRepository<Temp,Integer> {

    public Temp findById(int id);

    public List<Temp> findBySubmitDateBetween(Date date1, Date date2);
}
