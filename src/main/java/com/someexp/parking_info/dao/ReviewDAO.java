package com.someexp.parking_info.dao;

import com.someexp.parking_info.pojo.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review,Integer> {

    public Page findByPidOrderByIdDesc(int pid, Pageable pageable);
}

