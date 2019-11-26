package com.someexp.parking_info.service;

import com.someexp.parking_info.dao.TempDAO;
import com.someexp.parking_info.pojo.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TempService {
    @Autowired
    TempDAO tempDAO;

    public void add(Temp temp) {
        tempDAO.save(temp);
    }

    public Temp getById(int id) {
        return tempDAO.findById(id);
    }

    public List<Temp> getByPidAndSubmitDateBetween(int pid, Date date1, Date date2) {
        return tempDAO.findByPidAndSubmitDateBetween(pid, date1, date2);
    }




}
