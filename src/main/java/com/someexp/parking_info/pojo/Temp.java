package com.someexp.parking_info.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "temp")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Temp  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int uid;

    private int pid;

    private Date submitDate;

    private int state;

    @Transient
    private String username;

    @Transient
    private String infoname;

    @Override
    public String toString() {
        return "Temp{" +
                "id=" + id +
                ", uid=" + uid +
                ", pid=" + pid +
                ", createDate=" + submitDate +
                ", state=" + state +
                ", username='" + username + '\'' +
                ", infoname='" + infoname + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfoname() {
        return infoname;
    }

    public void setInfoname(String infoname) {
        this.infoname = infoname;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
