package com.someexp.parking_info.web;

import com.someexp.parking_info.pojo.InfoImage;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoImageService;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.util.ImageUtil;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.MyTools;
import com.someexp.parking_info.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class InfoImageController {
    @Autowired
    InfoImageService infoImageService;
    @Autowired
    InfoService infoService;

    /**
     * 添加停车场的详情图片
     * 校验: 是否登录, pid是否存在, image
     * 插入数据, 存储图片
     * @param pid
     * @param image
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/addInfoImage")
    public Object add(String pid, MultipartFile image, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        if (MyTools.isStringEmpty(pid))
            return Result.fail(MagicVariable.BAD_REQUEST);
        if (image.isEmpty())
            return Result.fail(MagicVariable.INFO_IMAGE_IS_EMPTY);

        int int_pid = Integer.parseInt(pid);
        if (!infoService.isPidExist(int_pid))
            return Result.fail(MagicVariable.INFO_NOT_EXIST);

        InfoImage newInfoImage = new InfoImage();
        newInfoImage.setUid(user.getId());
        newInfoImage.setPid(int_pid);
        newInfoImage.setSubmitDate(new Date());
        infoImageService.add(newInfoImage);
        saveOrUpdateImageFile(newInfoImage, image, request);// 添加多张详情图片
        return Result.success();
    }
    public void saveOrUpdateImageFile(InfoImage bean, MultipartFile image, HttpServletRequest request)
            throws Exception {
        File imageFolder= new File(request.getServletContext().getRealPath("data/img/info_detail/" + bean.getPid()));
        File file = new File(imageFolder,bean.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    /**
     * 根据pid列出详情图片的信息, id, 提交时间, 提交者姓名
     *
     * @param pid
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/listInfoImages")
    public Object list(@RequestParam(value = "pid") String pid, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        if (MyTools.isStringEmpty(pid))
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);

        int int_pid = Integer.parseInt(pid);
        if (!infoService.isPidExist(int_pid))
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        List<InfoImage> infoImages = infoImageService.listInfoImagesByPid(int_pid);
        Map<String, String> req_map = new HashMap<>();
        req_map.put("pid", pid);
        Map<String, Object> map = new HashMap<>();
        map.put("infoImages", infoImages);
        return Result.success(map, req_map);
    }


}
