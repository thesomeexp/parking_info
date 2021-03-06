package com.someexp.parking_info.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "review")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Review  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int uid;

    private int pid;

    private Integer accuracy;

    private Integer easyToFind;

    private String content;

    private Date submitDate;

    @Transient
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public void setEasyToFind(Integer easyToFind) {
        this.easyToFind = easyToFind;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getEasyToFind() {
        return easyToFind;
    }

    public void setEasyToFind(int easyToFind) {
        this.easyToFind = easyToFind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
