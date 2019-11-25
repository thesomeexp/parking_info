package com.someexp.parking_info.web;

import com.someexp.parking_info.pojo.Info;
import com.someexp.parking_info.pojo.InfoImage;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoImageService;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.util.*;
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
     * 校验: pid是否存在, image是否为空, 停车场是否存在
     * 插入数据, 存储图片
     * @param pid
     * @param image
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/foreAddInfoImage")
    public Object add(String pid, MultipartFile image, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
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
        newInfoImage.setState(MagicVariable.NO_VERIFIED);
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
    @GetMapping("/foreListInfoImages")
    public Object list(@RequestParam(value = "pid") String pid, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (MyTools.isStringEmpty(pid))
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);

        int int_pid = Integer.parseInt(pid);
        if (!infoService.isPidExist(int_pid))
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        List<InfoImage> infoImages = infoImageService.listInfoImagesByPid(int_pid);
        for (InfoImage image : infoImages){
            image.setUrl(request.getContextPath() + "data/img/info_detail/" + image.getPid() + "/" +
                    image.getId() + ".jpg");
        }
        Map<String, String> req_map = new HashMap<>();
        req_map.put("pid", pid);
        Map<String, Object> map = new HashMap<>();
        map.put("infoImages", infoImages);
        return Result.success(map, req_map);
    }

    @GetMapping("adminListNoVerifiedInfoImages")
    public Object adminListNoVerifiedInfoImages(@RequestParam(value = "start", defaultValue = "0") String start,
                          @RequestParam(value = "size", defaultValue = "5") String size,
                          @RequestParam(value = "navigatePage", defaultValue = "5") String navigatePage,
            HttpServletRequest request)throws Exception{
        Map<String, String> req_map = new HashMap<>();
        req_map.put("start", start);
        req_map.put("size", size);
        req_map.put("navigatePage", navigatePage);
        int int_start = Integer.parseInt(start);
        int_start = int_start<0?0:int_start;
        int int_size = Integer.parseInt(size);
        int_size = int_size<1?1:int_size;
        int int_navigatePage = Integer.parseInt(navigatePage);
        int_navigatePage = int_navigatePage<1?1:int_navigatePage;
        Page4Navigator<InfoImage> page = infoImageService.listInfoImageNoVerified(int_start, int_size, int_navigatePage, request);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return Result.success(page, req_map);
    }

    @GetMapping("adminListVerifiedInfoImages")
    public Object adminListVerifiedInfoImages(@RequestParam(value = "start", defaultValue = "0") String start,
                          @RequestParam(value = "size", defaultValue = "5") String size,
                          @RequestParam(value = "navigatePage", defaultValue = "5") String navigatePage,
                          HttpServletRequest request)throws Exception{
        Map<String, String> req_map = new HashMap<>();
        req_map.put("start", start);
        req_map.put("size", size);
        req_map.put("navigatePage", navigatePage);
        int int_start = Integer.parseInt(start);
        int_start = int_start<0?0:int_start;
        int int_size = Integer.parseInt(size);
        int_size = int_size<1?1:int_size;
        int int_navigatePage = Integer.parseInt(navigatePage);
        int_navigatePage = int_navigatePage<1?1:int_navigatePage;
        Page4Navigator<InfoImage> page = infoImageService.listInfoImageVerified(int_start, int_size, int_navigatePage, request);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return Result.success(page, req_map);
    }

    @GetMapping("adminListDisableInfoImages")
    public Object adminListDisableInfoImages(@RequestParam(value = "start", defaultValue = "0") String start,
                                              @RequestParam(value = "size", defaultValue = "5") String size,
                                              @RequestParam(value = "navigatePage", defaultValue = "5") String navigatePage,
                                              HttpServletRequest request)throws Exception{
        Map<String, String> req_map = new HashMap<>();
        req_map.put("start", start);
        req_map.put("size", size);
        req_map.put("navigatePage", navigatePage);
        int int_start = Integer.parseInt(start);
        int_start = int_start<0?0:int_start;
        int int_size = Integer.parseInt(size);
        int_size = int_size<1?1:int_size;
        int int_navigatePage = Integer.parseInt(navigatePage);
        int_navigatePage = int_navigatePage<1?1:int_navigatePage;
        Page4Navigator<InfoImage> page = infoImageService.listInfoImageDisable(int_start, int_size, int_navigatePage, request);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return Result.success(page, req_map);
    }

    @PostMapping("/adminVerifiedInfoImage")
    public Object adminVerifiedInfoImage(String pid) throws Exception{
        if (pid == null)
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        InfoImage infoImage = infoImageService.get(int_pid);
        if (infoImage == null)
            return Result.fail(MagicVariable.INFO_IMAGE_NO_EXIST);
        infoImage.setState(MagicVariable.VERIFIED);
        infoImageService.update(infoImage);
        return Result.success();
    }

    @PostMapping("/adminDisableInfoImage")
    public Object adminDisableInfoImage(String pid) throws Exception{
        if (pid == null)
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        InfoImage infoImage = infoImageService.get(int_pid);
        if (infoImage == null)
            return Result.fail(MagicVariable.INFO_IMAGE_NO_EXIST);
        infoImage.setState(MagicVariable.DISABLE);
        infoImageService.update(infoImage);
        return Result.success();
    }

    @PostMapping("/adminEnableInfoImage")
    public Object adminEnableInfoImage(String pid) throws Exception{
        if (pid == null)
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        InfoImage infoImage = infoImageService.get(int_pid);
        if (infoImage == null)
            return Result.fail(MagicVariable.INFO_IMAGE_NO_EXIST);
        infoImage.setState(MagicVariable.VERIFIED);
        infoImageService.update(infoImage);
        return Result.success();
    }

}
