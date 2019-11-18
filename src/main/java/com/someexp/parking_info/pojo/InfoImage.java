package com.someexp.parking_info.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Entity
@Table(name = "infoImage")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class InfoImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int pid;

    private int uid;

    private Date submitDate;

    @Transient
    private String username;
    @Transient
    private String infoname;

    public String getInfoname() {
        return infoname;
    }

    public void setInfoname(String infoname) {
        this.infoname = infoname;
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

    @Override
    public String toString() {
        return "InfoImage{" +
                "id=" + id +
                ", pid=" + pid +
                ", uid=" + uid +
                ", submitDate=" + submitDate +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
