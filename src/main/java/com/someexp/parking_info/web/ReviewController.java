package com.someexp.parking_info.web;

import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.MyTools;
import com.someexp.parking_info.pojo.Review;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.service.ReviewService;
import com.someexp.parking_info.util.Page4Navigator;
import com.someexp.parking_info.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    InfoService infoService;

    @PostMapping("/addReview")
    public Object addInfo(String pid, String accuracy, String easyToFind, String content, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.UN_LOGIN);

        if (MyTools.isStringEmpty(pid, accuracy, easyToFind))
            return Result.fail(MagicVariable.BAD_REQUEST);

        if (MyTools.isStringEmpty(content))
            content = MagicVariable.DEFAULT_REVIEW;

        int int_pid = Integer.parseInt(pid);
        int int_accuracy = Integer.parseInt(accuracy);
        int int_easyToFind = Integer.parseInt(easyToFind);

        if (!infoService.isPidExist(int_pid))
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        if (!MyTools.isStarLegal(int_accuracy, int_easyToFind))
            return Result.fail(MagicVariable.STAR_ILLEGAL);

        Review review = new Review();
        review.setPid(int_pid);
        review.setUid(user.getId());
        review.setSubmitDate(new Date());
        review.setAccuracy(int_accuracy);
        review.setEasyToFind(int_easyToFind);
        review.setContent(HtmlUtils.htmlEscape(content));
        reviewService.add(review);
        infoService.delOneCache(int_pid);
        return Result.success(MagicVariable.REVIEW_ADD_SUCCESS);
    }

    @GetMapping("/listReviews")
    public Object list(@RequestParam(value = "pid") String pid,
                       @RequestParam(value = "start", defaultValue = "0") String start,
                       @RequestParam(value = "size", defaultValue = "5") String size,
                       HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        if (MyTools.isStringEmpty(pid, start, size))
            return Result.fail(MagicVariable.BAD_REQUEST);

        int int_pid = Integer.parseInt(pid);
        if (!infoService.isPidExist(int_pid))
            return Result.fail(MagicVariable.RESULT_NO_EXIST);
        Map<String, String> req_map = new HashMap<>();
        req_map.put("pid", pid);
        req_map.put("start", start);
        req_map.put("size", size);
        int int_start = Integer.parseInt(start);
        int_start = int_start<0?0:int_start;
        int int_size = Integer.parseInt(size);
        Page4Navigator<Review> page =reviewService.list(int_pid, int_start, int_size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return Result.success(page, req_map);
    }


}
