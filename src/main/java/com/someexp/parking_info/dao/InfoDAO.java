package com.someexp.parking_info.dao;

import com.someexp.parking_info.pojo.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InfoDAO extends JpaRepository<Info,Integer> {
    public List<Info> findByLongitudeBetweenAndLatitudeBetween(double xMin,
                                                double xMax,
                                                double yMin,
                                                double yMax);
    public Page findByUidOrderByInfoSubmitDateDesc(int Uid, Pageable pageable);

    public List<Info> findByGeohashLike(String s);
    public Info getByLongitudeAndLatitude(double longitude, double latitude);
}
