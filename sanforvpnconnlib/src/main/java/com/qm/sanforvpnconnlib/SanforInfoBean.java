package com.qm.sanforvpnconnlib;

/**
 * Created by XueYang on 2017/6/22.
 */

public class SanforInfoBean {

    private int id;
    private String name;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private String mode;
    private String IP;
    private String port;
    private String user;
    private String pwd;
    private String certname;
    private String certpwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCertname() {
        return certname;
    }

    public void setCertname(String certname) {
        this.certname = certname;
    }

    public String getCertpwd() {
        return certpwd;
    }

    public void setCertpwd(String certpwd) {
        this.certpwd = certpwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return name;
    }
}
