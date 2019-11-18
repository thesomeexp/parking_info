package com.someexp.parking_info.test;


import ch.hsr.geohash.GeoHash;
import com.someexp.parking_info.util.MyTools;

public class myGeoHashTest {
    public static void main(String[] args) throws Exception {
        String location = "110.343522,21.268997";
        double[] xyArray = MyTools.praseLocation(location);
        double x = xyArray[0];
        double y = xyArray[1];
        GeoHash myGeoHash = GeoHash.withBitPrecision(y, x, 30);
        String geostring = myGeoHash.geoHashStringWithCharacterPrecision(y, x, 6);
        myGeoHash.getNorthernNeighbour();
        System.out.println(geostring);
        GeoHash[] arryGeoHash = myGeoHash.getAdjacent();
        for (int i=0;i<8;i++){
            System.out.println(arryGeoHash[i].toBase32());
        }
    }
}