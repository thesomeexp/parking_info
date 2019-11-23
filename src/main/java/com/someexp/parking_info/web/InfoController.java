package com.someexp.parking_info.web;

import ch.hsr.geohash.GeoHash;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.MyTools;
import com.someexp.parking_info.pojo.*;
import com.someexp.parking_info.service.*;
import com.someexp.parking_info.util.ImageUtil;
import com.someexp.parking_info.util.Page4Navigator;
import com.someexp.parking_info.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import javax.print.attribute.IntegerSyntax;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class InfoController {
    @Autowired
    InfoService infoService;
    @Autowired
    InfoImageService infoImageService;
    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;

    /**
     * 检查timestamp
     * 根据提交的location查询对应geohash和邻接geohash的停车场
     * 检查位置是否为空, 合法
     * 查询location的geohash块中的停车场, 再查询邻接的块放进infos
     * 查询成功返回对应map
     * @param location
     * @return
     */
    @GetMapping("/listNearbyInfos")
    public Object listNearbyInfos(@RequestParam(value = "location") String location, HttpServletRequest request) {
        try {
            long req_timestamp = Long.parseLong(request.getHeader("timestamp"));
            long server_timestamp = System.currentTimeMillis();
            if (server_timestamp - req_timestamp > MagicVariable.REPLAY_ATTACK_INTERVAL){
                return Result.fail(MagicVariable.REPLAY_ATTACK_DETECT);
            }

            if (MyTools.isStringEmpty(location))
                return Result.fail(MagicVariable.BAD_REQUEST);

            double[] xyArray = MyTools.praseLocation(location);
            double x = xyArray[0];
            double y = xyArray[1];
            if (!MyTools.isXYLegal(x, y))
                return Result.fail(MagicVariable.LOCATION_ILLEGAL);

            GeoHash geoHash = GeoHash.withBitPrecision(y, x, MagicVariable.SEARCH_GEOHASH_LIMIT*5);
            GeoHash[] arryGeoHash = geoHash.getAdjacent();
            List<Info> infos = infoService.searchInfoByGeoHashAndVerified(GeoHash.geoHashStringWithCharacterPrecision(y, x,
                    MagicVariable.SEARCH_GEOHASH_LIMIT));
            for(int i=0; i<8 ;i++){
                infos.addAll(infoService.searchInfoByGeoHashAndVerified(arryGeoHash[i].toBase32()));
            }
            Map<String, String> req_map = new HashMap<>();
            req_map.put("location", location);
            req_map.put("req_timestamp", Long.toString(req_timestamp));
            Map<String, Object> map = new HashMap<>();
            map.put("infos", infos);
            map.put("timestamp", Long.toString(server_timestamp));
            return Result.success(map, req_map);
        } catch (Exception e) {
            return Result.fail(MagicVariable.LOGGER_ERROR_NO_LOGIN);
        }
    }


    /**
     * 检查timestamp
     * 从请求获取session然后得到当前用户
     * 检查名字, 位置, 图片是否为空
     * 检查名字, 内容是否超出
     * 判断位置是否合法, 是否存在
     * 不存在则添加记录
     * 添加首张图片
     * 清除同区域Redis缓存
     * @param name
     * @param location
     * @param image
     * @param content
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/foreAddInfo")
    public Object addInfo(String name, String location, MultipartFile image,
                          String content, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.NO_USER_IN_SESSION);
        if (image == null || image.isEmpty())
            return Result.fail(MagicVariable.INFO_IMAGE_IS_EMPTY);
        if (MyTools.isStringEmpty(name))
            return Result.fail(MagicVariable.INFO_NAME_IS_EMPTY);
        if (MyTools.isStringEmpty(location))
            return Result.fail(MagicVariable.INFO_LOCATION_EMPTY);

        if (!MyTools.isValueLengthLegal(name, MagicVariable.INFO_NAME_MAX_LEN) ||
                !MyTools.isValueLengthLegal(content, MagicVariable.INFO_CONTENT_MAX_LEN))
            return Result.fail(MagicVariable.PARAM_VALUES_TOO_MAX);

        double[] xyArray = MyTools.praseLocation(location);
        double x = xyArray[0];
        double y = xyArray[1];
        if (!MyTools.isXYLegal(x, y))
            return Result.fail(MagicVariable.LOCATION_ILLEGAL);

        if (infoService.isLocationExist(x, y))
            return Result.fail(MagicVariable.PARKING_IS_EXIST);
        Info newInfo = new Info();
        newInfo.setName(name);
        newInfo.setContent(content);
        newInfo.setLongitude(x);
        newInfo.setLatitude(y);
        newInfo.setGeoHash(GeoHash.geoHashStringWithCharacterPrecision(y, x, MagicVariable.INFO_GEOHASH_LIMIT));
        newInfo.setUid(user.getId());
        Date curr = new Date();
        newInfo.setInfoSubmitDate(curr);
        newInfo.setStateUpdateDate(curr);
        newInfo.setState(MagicVariable.NO_VERIFIED);
        infoService.setDefaultT(newInfo);
        infoService.add(newInfo);
        saveOrUpdateImageFile(newInfo, image, request);// 添加首张图片
        infoService.delGeoHashCache(GeoHash.geoHashStringWithCharacterPrecision(newInfo.getLatitude(), newInfo.getLongitude(),
                MagicVariable.SEARCH_GEOHASH_LIMIT));// 注解清除同区域geohash缓存
        return Result.success(MagicVariable.INFO_ADD_SUCCESS);
    }
    // 添加图片bug: 如果不是png, jpg文件就会抛出异常, 后期可以做一下图片压缩
    public void saveOrUpdateImageFile(Info bean, MultipartFile image, HttpServletRequest request)
            throws Exception {
        File imageFolder = new File(request.getServletContext().getRealPath(MagicVariable.INFO_IMAGE_PATH));
        File file = new File(imageFolder, bean.getId() + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    /**
     * 查询所有的停车场, 按照id逆序排序
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping("/adminListAllInfos")
    public Object list(@RequestParam(value = "start", defaultValue = "0") String start,
                       @RequestParam(value = "size", defaultValue = "5") String size,
                        @RequestParam(value = "navigatePage", defaultValue = "5") String navigatePage) throws Exception {
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
        Page4Navigator<Info> page = infoService.list(int_start, int_size, int_navigatePage);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return Result.success(page, req_map);
    }

    @GetMapping("/adminListNoVerifiedInfo")
    public Object adminListNoVerifiedInfo(@RequestParam(value = "start", defaultValue = "0") String start,
                                     @RequestParam(value = "size", defaultValue = "5") String size,
                                     @RequestParam(value = "navigatePage", defaultValue = "5") String navigatePage) throws Exception{
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
        Page4Navigator<Info> page = infoService.listInfoNoVerified(int_start, int_size, int_navigatePage);
        return Result.success(page, req_map);
    }

    @GetMapping("/adminListDisableInfo")
    public Object adminListDisableInfo(@RequestParam(value = "start", defaultValue = "0") String start,
                                       @RequestParam(value = "size", defaultValue = "5") String size,
                                       @RequestParam(value = "navigatePage", defaultValue = "5") String navigatePage) throws Exception{
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
        Page4Navigator<Info> page = infoService.listInfoDisadble(int_start, int_size, int_navigatePage);
        return Result.success(page, req_map);
    }

    @PostMapping("/adminVerifiedInfo")
    public Object adminVerifiedInfo(String pid, HttpServletRequest request){
        if (pid == null)
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        Info info = infoService.getById(int_pid);
        if (info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        info.setState(MagicVariable.VERIFIED);
        infoService.update(info);
        infoService.delGeoHashCache(GeoHash.geoHashStringWithCharacterPrecision(info.getLatitude(), info.getLongitude(),
                        MagicVariable.SEARCH_GEOHASH_LIMIT));// 注解清除同区域geohash缓存
        return Result.success();
    }

    @PostMapping("/adminDisableInfo")
    public Object adminDisableInfo(String pid, HttpServletRequest request){
        if (pid == null)
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        Info info = infoService.getById(int_pid);
        if (info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        info.setState(MagicVariable.INFO_DISABLE);
        infoService.update(info);
        infoService.delGeoHashCache(GeoHash.geoHashStringWithCharacterPrecision(info.getLatitude(), info.getLongitude(),
                MagicVariable.SEARCH_GEOHASH_LIMIT));// 注解清除同区域geohash缓存
        return Result.success();
    }

    @PostMapping("/adminEnableInfo")
    public Object adminEnableInfo(String pid, HttpServletRequest request){
        if (pid == null)
            return Result.fail(MagicVariable.PARAM_VALUES_IS_EMPTY);
        int int_pid = Integer.parseInt(pid);
        Info info = infoService.getById(int_pid);
        if (info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        info.setState(MagicVariable.VERIFIED);
        infoService.update(info);
        infoService.delGeoHashCache(GeoHash.geoHashStringWithCharacterPrecision(info.getLatitude(), info.getLongitude(),
                MagicVariable.SEARCH_GEOHASH_LIMIT));// 注解清除同区域geohash缓存
        return Result.success();
    }
    /**
     * 先拿id查询出整个记录, 查询出所有
     * 删除记录再删除两个缓存geohash, one
     * 如果没有报异常删除对应的主文件
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/adminDeleteInfos")
    public Object deleteInfo(int id, HttpServletRequest request) throws Exception{
        Info info = infoService.getById(id);
        try {
            infoService.delete(id);
            infoService.delOneCache(info.getId());
            infoService.delGeoHashCache(GeoHash.geoHashStringWithCharacterPrecision(info.getLatitude(),
                    info.getLongitude(), MagicVariable.SEARCH_GEOHASH_LIMIT));
        } catch (DataIntegrityViolationException e){
            return Result.fail(MagicVariable.LOGGER_DATA_INTEGRITY_VIOLATION_EXCEPTION);// 违反约束无法删除
        }
        File imageFolder = new File(request.getServletContext().getRealPath(MagicVariable.INFO_IMAGE_PATH));
        File file = new File(imageFolder, id + ".jpg");
        file.delete();
        return Result.success(MagicVariable.INFO_DELETE_SUCCESS);
    }


    /**
     * 根据传来的pid返回该停车场详情
     * 验证登录, 参数, 是否存在
     * 根据id获取一个info
     * 为info添加提交者名字
     * @param pid
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("infoDetail")
    public Object infoDetail(@RequestParam(value = "pid") String pid, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        if (MyTools.isStringEmpty(pid))
            return Result.fail(MagicVariable.BAD_REQUEST);

        int int_pid = Integer.parseInt(pid);
        if (!infoService.isPidExist(int_pid))
            return Result.fail(MagicVariable.RESULT_NO_EXIST);
        Map<String, String> req_map = new HashMap<>();
        req_map.put("pid", pid);

        Info info = infoService.getById(int_pid);
        infoService.addUsername(info);

        Map<String, Object> data_map = new HashMap<>();
        data_map.put("info", info);

        return Result.success(data_map, req_map);
    }

    /**
     * 查询用户自己提交的所有停车场信息
     * 是否登录, 验证参数
     * 根据uid和分页情况查询数据
     * 返回查询的分页信息
      */
    @GetMapping("/mySubmitInfos")
    public Object mySubmitInfos(@RequestParam(value = "start", defaultValue = "0") String start,
                                @RequestParam(value = "size", defaultValue = "5") String size,
                                HttpSession session) throws Exception{
        User user = (User) session.getAttribute("user");
        if (user == null)
            return Result.fail(MagicVariable.UN_LOGIN);
        if (MyTools.isStringEmpty(start, size))
            return Result.fail(MagicVariable.BAD_REQUEST);

        Map<String, String> req_map = new HashMap<>();
        req_map.put("start", start);
        req_map.put("size", size);
        int int_start = Integer.parseInt(start);
        int_start = int_start<0?0:int_start;
        int int_size = Integer.parseInt(size);

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(int_start, int_size, sort);
        Page pageFromJPA = infoService.listInfoByUidOrderBySubmitDateDesc(user.getId(), pageable);
        Page4Navigator page = new Page4Navigator<Info> (pageFromJPA, 5);
        return Result.success(page, req_map);
    }




///////////////////////




}
