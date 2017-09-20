package com.example.carouselpicandvideodemo.bean;

/**
 * Created by 孙亚斌 on 2017/2/14 ${time}.
 * com.ceilcell.yuelu.bean
 * 用户实体类
 */

public class UserBean {
    public static final String MARK = "user_info";

    //用户id,即手机号码
    private String cuid="";
    //用户登录密码
    private String cpassword="";
    //姓名
    private String cname="";
    //所属工社
    private String cecpid="";
    //所属工社名称
    private String cecpidname="";
    //头像
    private String cimg="";
    //性别
    private String cgender="";
    //出生日期
    private String ctime="";
    //用户地址
    private String caddress="";
    //农业关注种类
    private String protype="";



    public UserBean() {
    }

    public UserBean(String cuid, String cpassword, String cname, String cecpid, String cecpidname, String cimg) {
        this.cuid=cuid;
        this.cpassword=cpassword;
        this.cname=cname;
        this.cecpid=cecpid;
        this.cecpidname=cecpidname;
        this.cimg=cimg;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCecpid() {
        return cecpid;
    }

    public void setCecpid(String cecpid) {
        this.cecpid = cecpid;
    }

    public String getCecpidname() {
        return cecpidname;
    }

    public void setCecpidname(String cecpidname) {
        this.cecpidname = cecpidname;
    }

    public String getCimg() {
        return cimg;
    }

    public void setCimg(String cimg) {
        this.cimg = cimg;
    }
    public String getPassword() {
        return cpassword;
    }

    public void setPassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getCgender() {
        return cgender;
    }

    public void setCgender(String cgender) {
        this.cgender = cgender;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }
    public String getProtype() {
        return protype;
    }

    public void setProtype(String protype) {
        this.protype = protype;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "cuid='" + cuid + '\'' +
                ", cpassword='" + cpassword + '\'' +
                ", cname='" + cname + '\'' +
                ", cecpid='" + cecpid + '\'' +
                ", cecpidname='" + cecpidname + '\'' +
                ", cimg='" + cimg + '\'' +
                ", cgender='" + cgender + '\'' +
                ", ctime='" + ctime + '\'' +
                ", caddress='" + caddress + '\'' +
                ", protype='" + protype + '\'' +
                '}';
    }
}
