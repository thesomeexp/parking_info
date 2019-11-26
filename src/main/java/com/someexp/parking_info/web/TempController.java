package com.someexp.parking_info.web;

import ch.hsr.geohash.GeoHash;
import com.someexp.parking_info.pojo.Info;
import com.someexp.parking_info.util.MagicVariable;
import com.someexp.parking_info.util.MyTools;
import com.someexp.parking_info.pojo.Temp;
import com.someexp.parking_info.pojo.User;
import com.someexp.parking_info.service.InfoService;
import com.someexp.parking_info.service.TempService;
import com.someexp.parking_info.service.UserService;
import com.someexp.parking_info.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.internal.crypto.RsaMd5CksumType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class TempController {
    @Autowired
    TempService tempService;
    @Autowired
    InfoService infoService;
    @Autowired
    UserService userService;

    @PostMapping("/foreAddTemp")
    public Object addInfo(String pid, String location, String state, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (MyTools.isStringEmpty(pid))
            return Result.fail(MagicVariable.INFO_ID_IS_EMPTY);
        if (MyTools.isStringEmpty(location))
            return Result.fail(MagicVariable.LOCATION_IS_EMPTY);
        if (MyTools.isStringEmpty(state))
            return Result.fail(MagicVariable.STAR_IS_EMPTY);

        double[] xyArray = MyTools.praseLocation(location);
        double x = xyArray[0];
        double y = xyArray[1];
        int int_pid = Integer.parseInt(pid);
        int int_state = Integer.parseInt(state);
        if (!MyTools.isStarLegal(int_state))
            return Result.fail(MagicVariable.INFO_STATE_IS_ERROR);
        if (!MyTools.isXYLegal(x, y))
            return Result.fail(MagicVariable.INFO_LOCATION_ILLEGAL);
        Info info = infoService.getById(int_pid);
        if (info == null)
            return Result.fail(MagicVariable.INFO_NOT_EXIST);
        double info_x = info.getLongitude();
        double info_y = info.getLatitude();
        String userGeoHash = GeoHash.geoHashStringWithCharacterPrecision(y, x,
                MagicVariable.ADD_TEMP_GEOHASH_LIMIT);

        GeoHash geoHash = GeoHash.withBitPrecision(info_y, info_x, MagicVariable.ADD_TEMP_GEOHASH_LIMIT*5);
        GeoHash[] arryGeoHash = geoHash.getAdjacent();
        if(!MyTools.isUserGeoHashInArray(userGeoHash, geoHash.toBase32(), arryGeoHash))
            return Result.fail(MagicVariable.LOCATION_NOT_NEAR_PARKING);

        Date now = new Date();
        long interval_time = MagicVariable.INTERVAL_TIME;
        Date beforeDate = new Date(now.getTime() - interval_time);
        Date afterDate = new Date(now.getTime() + interval_time);
        List<Temp> old_temp = tempService.getByPidAndSubmitDateBetween(int_pid, beforeDate, afterDate);
        if (!old_temp.isEmpty())
            return Result.fail(MagicVariable.INTERVAL_TIME_ERROR);

        Temp temp = new Temp();
        temp.setUid(user.getId());
        temp.setPid(int_pid);
        temp.setSubmitDate(now);
        temp.setState(int_state);
        tempService.add(temp);

        SimpleDateFormat formatter= new SimpleDateFormat("HH");
        String str_hour = formatter.format(now).toString();
        int int_hour = Integer.parseInt(str_hour);
        int_hour = int_hour - 1;
        Field f1 = null;
        f1 = info.getClass().getDeclaredField("t" + int_hour);
        f1.setAccessible(true);
        int old_state = Integer.parseInt(f1.get(info).toString());
        f1.set(info, (old_state + int_state)/2);
        infoService.update(info);
        return Result.success(MagicVariable.TEMP_ADD_SUCCESS);
    }


}
