package com.someexp.parking_info.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Review {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", uid=" + uid +
                ", pid=" + pid +
                ", accuracy=" + accuracy +
                ", easyToFind=" + easyToFind +
                ", content='" + content + '\'' +
                ", submitDate=" + submitDate +
                '}';
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
