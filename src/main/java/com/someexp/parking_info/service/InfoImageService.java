package com.someexp.parking_info.service;

import com.someexp.parking_info.dao.InfoDAO;
import com.someexp.parking_info.dao.InfoImageDAO;
import com.someexp.parking_info.pojo.Info;
import com.someexp.parking_info.pojo.InfoImage;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public InfoImage getById(int id){
        InfoImage infoImage = infoImageDAO.findOne(id);
        addUsername(infoImage);
        addInfoname(infoImage);
        return infoImage;
    }

    public void addUsername(InfoImage infoImage){
        infoImage.setUsername(userService.getUsernameById(infoImage.getUid()));
    }

    public void addUsername(List<InfoImage> infoImages){
        for (InfoImage infoImage : infoImages)
            addUsername(infoImage);
    }

    public void addInfoname(InfoImage infoImage){
        infoImage.setInfoname(infoService.getInfoNameById(infoImage.getPid()));
    }

    public void addInfoname(List<InfoImage> infoImages){
        for (InfoImage infoImage : infoImages)
            addInfoname(infoImage);
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

    public void update(InfoImage infoImage) {
        infoImageDAO.save(infoImage);
    }

    public Page4Navigator<Info> list(int int_start, int int_size, int int_navigatePage, HttpServletRequest request) throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(int_start, int_size, sort);
        Page pageFromJPA = infoImageDAO.findAll(pageable);
        List<InfoImage> listInfoImage = pageFromJPA.getContent();
        addUsername(listInfoImage);
        addInfoname(listInfoImage);
        for (InfoImage image : listInfoImage){
            image.setUrl(request.getContextPath() + "data/img/info_detail/" + image.getPid() + "/" +
                    image.getId() + ".jpg");
        }
        return new Page4Navigator<>(pageFromJPA, int_navigatePage);
    }

    public Page4Navigator<InfoImage> listInfoImageNoVerified(int int_start, int int_size, int int_navigatePage, HttpServletRequest request) throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(int_start, int_size, sort);
        Page pageFromJPA = infoImageDAO.findByState(MagicVariable.NO_VERIFIED, pageable);
        List<InfoImage> listInfoImage = pageFromJPA.getContent();
        addUsername(listInfoImage);
        addInfoname(listInfoImage);
        for (InfoImage image : listInfoImage){
            image.setUrl(request.getContextPath() + "data/img/info_detail/" + image.getPid() + "/" +
                    image.getId() + ".jpg");
        }
        return new Page4Navigator<>(pageFromJPA, int_navigatePage);
    }

    public Page4Navigator<InfoImage> listInfoImageVerified(int int_start, int int_size, int int_navigatePage, HttpServletRequest request) throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(int_start, int_size, sort);
        Page pageFromJPA = infoImageDAO.findByState(MagicVariable.VERIFIED, pageable);
        List<InfoImage> listInfoImage = pageFromJPA.getContent();
        addUsername(listInfoImage);
        addInfoname(listInfoImage);
        for (InfoImage image : listInfoImage){
            image.setUrl(request.getContextPath() + "data/img/info_detail/" + image.getPid() + "/" +
                    image.getId() + ".jpg");
        }
        return new Page4Navigator<>(pageFromJPA, int_navigatePage);
    }

    public Page4Navigator<InfoImage> listInfoImageDisable(int int_start, int int_size, int int_navigatePage, HttpServletRequest request) throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(int_start, int_size, sort);
        Page pageFromJPA = infoImageDAO.findByState(MagicVariable.DISABLE, pageable);
        List<InfoImage> listInfoImage = pageFromJPA.getContent();
        addUsername(listInfoImage);
        addInfoname(listInfoImage);
        for (InfoImage image : listInfoImage){
            image.setUrl(request.getContextPath() + "data/img/info_detail/" + image.getPid() + "/" +
                    image.getId() + ".jpg");
        }
        return new Page4Navigator<>(pageFromJPA, int_navigatePage);
    }
}
