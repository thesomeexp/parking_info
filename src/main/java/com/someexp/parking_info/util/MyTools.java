package com.someexp.parking_info.util;


import ch.hsr.geohash.GeoHash;
import org.slf4j.LoggerFactory;

public class MyTools {


    public static final double[] praseLocation (String location) throws Exception{
        String[] locationArray = location.split(",");
        String x = locationArray[0];
        String y = locationArray[1];
        double dou_x = Double.valueOf(x).doubleValue();
        double dou_y = Double.valueOf(y).doubleValue();
        double[] xyArray = new double[2];
        xyArray[0] = dou_x;
        xyArray[1] = dou_y;
        return xyArray;
    }

    public static final boolean isStringEmpty(String... strArray) throws Exception{
            for (String str : strArray)
                if (str == null || str.equals("") || str.equals("null"))
                    return true;
            return false;
    }

    public static final boolean isValueLengthLegal(String val, int len){
        if (val.length() > len)
            return false;
        return true;
    }

    public static final boolean isXYLegal(double x, double y) throws Exception{
        if (x < MagicVariable.MINIMUM_X ||
                x > MagicVariable.MAXIMUM_X ||
                y < MagicVariable.MINIMUM_Y ||
                y > MagicVariable.MAXIMUM_Y) {
            return false;
        } else {
            return true;
        }
    }

    public static final boolean isStateLegal(int... states) throws Exception{
        for (int state : states)
            if (state < MagicVariable.MINIMUM_STATE ||
                    state > MagicVariable.MAXIMUM_STATE)
                return false;
        return true;
    }
    public static final boolean isStarLegal(int... stars) throws Exception{
        for (int star : stars)
            if (star < MagicVariable.MINIMUM_STAR ||
                    star > MagicVariable.MAXIMUM_STAR)
                return false;
        return true;
    }

    public static boolean isUserGeoHashInArray(String userGeoHash, String infoGeoHash, GeoHash[] arryGeoHash) {
        if (userGeoHash.equals(infoGeoHash))
            return true;
        for (GeoHash geoHash : arryGeoHash){
            if (geoHash.toBase32().equals(userGeoHash))
                return true;
        }
        return false;
    }
}
