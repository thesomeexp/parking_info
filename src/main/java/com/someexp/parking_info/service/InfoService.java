package com.someexp.parking_info.service;

import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.dao.InfoDAO;
import com.someexp.parking_info.pojo.Info;
import com.someexp.parking_info.util.MyTools;
import com.someexp.parking_info.util.Page4Navigator;
import com.someexp.parking_info.util.SpringContextUtil;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
//@CacheConfig(cacheNames="infos")
public class InfoService {
    @Autowired
    InfoDAO infoDAO;
    @Autowired
    UserService userService;

//    @Caching(evict = {@CacheEvict(key="'infos-geohash-'+#p0"), @CacheEvict(key="'infos-one-'+#p1")})



    public boolean isLocationExist(double longitude, double latitude) throws Exception{
        Info result = infoDAO.getByLongitudeAndLatitude(longitude, latitude);
        if (result != null)
            return true;
        return false;
    }

    public Info add(Info bean) {
        return infoDAO.save(bean);
    }

//    @CacheEvict(allEntries=true)
    public void delete(int id) {
        infoDAO.delete(id);
    }

//    @CacheEvict(key="'infos-geohash-'+#p0")
    public void delGeoHashCache(String geoHash){}

//    @CacheEvict(allEntries=true)
    public void update(Info info) {
        infoDAO.save(info);
    }

//    @Cacheable(key="'infos-one-'+#p0")
    public Info getById(int id) {
        Info info = infoDAO.findOne(id);
        if (info != null)
            addUsername(info);
        return info;
    }

//    @CacheEvict(key="'infos-one-'+#p0")
    public void delOneCache(int id){}

// 管理员查询所有
//    @Cacheable(key="'infos-page-'+#p0+'-'+#p1")
    public Page4Navigator<Info> list(int start, int size, int navigatePages) throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = infoDAO.findAll(pageable);
        List<Info> listInfo = pageFromJPA.getContent();
        addUsername(listInfo);// 得到车位提交用户名
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    // 管理员查询所有没有认证的停车场
    public Page4Navigator<Info> listInfoNoVerified(int start, int size, int navigatePages)throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = infoDAO.findByState(MagicVariable.NO_VERIFIED, pageable);
        List<Info> listInfo = pageFromJPA.getContent();
        addUsername(listInfo);// 得到车位提交用户名
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    // 管理员查询所有禁用的停车场
    public Page4Navigator<Info> listInfoDisadble(int start, int size, int navigatePages)throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = infoDAO.findByState(MagicVariable.DISABLE, pageable);
        List<Info> listInfo = pageFromJPA.getContent();
        addUsername(listInfo);// 得到车位提交用户名
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    // 查询自己提交的停车位(不好加缓存)
    public Page listInfoByUidOrderBySubmitDateDesc(int Uid, Pageable pageable)
            throws Exception{
        Page pageFromJPA = infoDAO.findByUidOrderByInfoSubmitDateDesc(Uid, pageable);
        List<Info> listInfo = pageFromJPA.getContent();
        addUsername(listInfo);// 得到车位提交用户名
        return pageFromJPA;
    }


    public String getInfoNameById(int pid) {
        Info info = infoDAO.getOne(pid);
        return info.getName();
    }

    public List<Info> searchInfoBetweenLongitudeAndLatitude(double x, double y, double limit) {
        List<Info> infos = infoDAO.findByLongitudeBetweenAndLatitudeBetween(x - limit,
                x + limit, y - limit, y + limit
        );
        addUsername(infos);
        return infos;
    }



//    @Cacheable(key="'infos-geohash-'+#p0")
    public List<Info> searchInfoByGeoHash(String geoHash) {
        List<Info> infos = infoDAO.findByGeoHashLike(geoHash + "%");
        addUsername(infos);
        return infos;
    }

    public List<Info> searchInfoByGeoHashAndVerified(String geoHash){
        List<Info> infos = infoDAO.findByGeoHashLikeAndState(geoHash + "%", MagicVariable.VERIFIED);
        addUsername(infos);
        return infos;
    }

    public boolean isPidExist(int pid) {
        InfoService infoService = SpringContextUtil.getBean(InfoService.class);// 饶一绕触发切面
        Info info = infoService.getById(pid);
        if (info == null)
            return false;
        else
            return true;
    }

    public void addUsername(List<Info> infos) {
        for (Info info : infos) {
            addUsername(info);
        }
    }


    public void addUsername(Info info) {
        info.setUsername(userService.getUsernameById(info.getUid()));
    }

    public void setDefaultT(Info newInfo) throws Exception {
        Field f1 = null;
        for (int i = 0; i < 24; i++) {
            f1 = newInfo.getClass().getDeclaredField("t" + i);
            f1.setAccessible(true);
            f1.set(newInfo, MagicVariable.INFO_DEFAULT_STATE_VALUES);
        }
    }

}
