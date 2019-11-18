package com.someexp.parking_info.service;

import com.someexp.parking_info.dao.InfoDAO;
import com.someexp.parking_info.dao.InfoImageDAO;
import com.someexp.parking_info.pojo.Info;
import com.someexp.parking_info.pojo.InfoImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@CacheConfig(cacheNames="images")
public class InfoImageService {
    @Autowired InfoImageDAO infoImageDAO;
    @Autowired UserService userService;
    @Autowired InfoService infoService;

//    @CacheEvict(allEntries=true)
    public void add(InfoImage bean) {
        infoImageDAO.save(bean);
    }

//    @CacheEvict(allEntries=true)
    public void delete(int id) {
        infoImageDAO.delete(id);
    }

    public InfoImage get(int id) {
        return infoImageDAO.findOne(id);
    }

//    @Cacheable(key="'images-pid-'+#p0")
    public List<InfoImage> listInfoImagesByPid(int pid) {
        List<InfoImage> listInfoImage = infoImageDAO.findByPidOrderById(pid);
        for (InfoImage infoImage : listInfoImage){
            infoImage.setUsername(userService.getUsernameById(infoImage.getUid()));
            infoImage.setInfoname(infoService.getInfoNameById(infoImage.getPid()));
        }
        return listInfoImage;
    }

}
