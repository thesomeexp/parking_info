package com.someexp.parking_info.service;

import com.someexp.parking_info.dao.ReviewDAO;
import com.someexp.parking_info.pojo.Review;
import com.someexp.parking_info.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
//@CacheConfig(cacheNames="reviews")
public class ReviewService {
    @Autowired
    ReviewDAO reviewDAO;
    @Autowired
    UserService userService;

//    @CacheEvict(allEntries=true)
    public void add(Review bean) {
        reviewDAO.save(bean);
    }

//    @Cacheable(key="'reviews-page-'+#p0+'-'+#p1+#p2+#p3")
    public Page4Navigator<Review> list(int pid, int start, int size, int navigatePages) throws Exception{
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA = reviewDAO.findByPidOrderByIdDesc(pid, pageable);
        List<Review> listReview = pageFromJPA.getContent();
        for (Review review : listReview){
            setReviewUsername(review); // 给评论添加用户名
        }
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public void setReviewUsername(Review review){
        review.setUsername(userService.getUsernameById(review.getUid()));
    }

}
