package com.hanling.utilsapp.MVPdemo.model;

import com.hanling.utilsapp.MVPdemo.bean.UserBean;

/**
 * Created by XueYang on 2017/6/12.
 */

public interface IUserModel {
    void setID(int id);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setUser(int id,String firstName,String lastName);

    UserBean load(int id);
}
