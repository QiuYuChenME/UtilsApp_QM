package com.hanling.utilsapp.bean;

import java.util.List;
import java.util.Objects;

/**
 * Created by 20170341 on 2018/1/16.
 */

public class ResultBean {

    /**
     * msg : {"username":["该字段是必填项。"],"password":["该字段是必填项。"]}
     * code : 4
     * resultObj : []
     */

    private Object msg;
    private int code;
    private List<?> resultObj;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<?> getResultObj() {
        return resultObj;
    }

    public void setResultObj(List<?> resultObj) {
        this.resultObj = resultObj;
    }

}
