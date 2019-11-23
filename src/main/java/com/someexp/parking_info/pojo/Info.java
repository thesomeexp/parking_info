package com.someexp.parking_info.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "info")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Info implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;

    private String content;

    private Double longitude;

    private Double latitude;

    private String geoHash;

    private int uid;

    private Date infoSubmitDate;

    private Date stateUpdateDate;

    private String state;

    @Transient
    private String username;

    private int t0;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private int t5;
    private int t6;
    private int t7;
    private int t8;
    private int t9;
    private int t10;
    private int t11;
    private int t12;
    private int t13;
    private int t14;
    private int t15;
    private int t16;
    private int t17;
    private int t18;
    private int t19;
    private int t20;
    private int t21;
    private int t22;
    private int t23;

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", geoHash='" + geoHash + '\'' +
                ", uid=" + uid +
                ", infoSubmitDate=" + infoSubmitDate +
                ", stateUpdateDate=" + stateUpdateDate +
                ", state='" + state + '\'' +
                ", username='" + username + '\'' +
                ", t0=" + t0 +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t4=" + t4 +
                ", t5=" + t5 +
                ", t6=" + t6 +
                ", t7=" + t7 +
                ", t8=" + t8 +
                ", t9=" + t9 +
                ", t10=" + t10 +
                ", t11=" + t11 +
                ", t12=" + t12 +
                ", t13=" + t13 +
                ", t14=" + t14 +
                ", t15=" + t15 +
                ", t16=" + t16 +
                ", t17=" + t17 +
                ", t18=" + t18 +
                ", t19=" + t19 +
                ", t20=" + t20 +
                ", t21=" + t21 +
                ", t22=" + t22 +
                ", t23=" + t23 +
                '}';
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public Date getInfoSubmitDate() {
        return infoSubmitDate;
    }

    public void setInfoSubmitDate(Date infoSubmitDate) {
        this.infoSubmitDate = infoSubmitDate;
    }

    public Date getStateUpdateDate() {
        return stateUpdateDate;
    }

    public void setStateUpdateDate(Date stateUpdateDate) {
        this.stateUpdateDate = stateUpdateDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getT0() {
        return t0;
    }

    public void setT0(int t0) {
        this.t0 = t0;
    }

    public int getT1() {
        return t1;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public int getT2() {
        return t2;
    }

    public void setT2(int t2) {
        this.t2 = t2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public int getT4() {
        return t4;
    }

    public void setT4(int t4) {
        this.t4 = t4;
    }

    public int getT5() {
        return t5;
    }

    public void setT5(int t5) {
        this.t5 = t5;
    }

    public int getT6() {
        return t6;
    }

    public void setT6(int t6) {
        this.t6 = t6;
    }

    public int getT7() {
        return t7;
    }

    public void setT7(int t7) {
        this.t7 = t7;
    }

    public int getT8() {
        return t8;
    }

    public void setT8(int t8) {
        this.t8 = t8;
    }

    public int getT9() {
        return t9;
    }

    public void setT9(int t9) {
        this.t9 = t9;
    }

    public int getT10() {
        return t10;
    }

    public void setT10(int t10) {
        this.t10 = t10;
    }

    public int getT11() {
        return t11;
    }

    public void setT11(int t11) {
        this.t11 = t11;
    }

    public int getT12() {
        return t12;
    }

    public void setT12(int t12) {
        this.t12 = t12;
    }

    public int getT13() {
        return t13;
    }

    public void setT13(int t13) {
        this.t13 = t13;
    }

    public int getT14() {
        return t14;
    }

    public void setT14(int t14) {
        this.t14 = t14;
    }

    public int getT15() {
        return t15;
    }

    public void setT15(int t15) {
        this.t15 = t15;
    }

    public int getT16() {
        return t16;
    }

    public void setT16(int t16) {
        this.t16 = t16;
    }

    public int getT17() {
        return t17;
    }

    public void setT17(int t17) {
        this.t17 = t17;
    }

    public int getT18() {
        return t18;
    }

    public void setT18(int t18) {
        this.t18 = t18;
    }

    public int getT19() {
        return t19;
    }

    public void setT19(int t19) {
        this.t19 = t19;
    }

    public int getT20() {
        return t20;
    }

    public void setT20(int t20) {
        this.t20 = t20;
    }

    public int getT21() {
        return t21;
    }

    public void setT21(int t21) {
        this.t21 = t21;
    }

    public int getT22() {
        return t22;
    }

    public void setT22(int t22) {
        this.t22 = t22;
    }

    public int getT23() {
        return t23;
    }

    public void setT23(int t23) {
        this.t23 = t23;
    }
}
