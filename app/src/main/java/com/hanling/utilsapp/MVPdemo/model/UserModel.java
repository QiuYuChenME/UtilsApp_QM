package com.hanling.utilsapp.MVPdemo.model;

import android.util.SparseArray;

import com.hanling.utilsapp.MVPdemo.bean.UserBean;

/**
 * Created by XueYang on 2017/6/12.
 */

public class UserModel implements IUserModel {
    private String mFristName;
    private String mLastName;
    private int mID;
    private SparseArray<UserBean> mUsererArray = new SparseArray<UserBean>();

    @Override
    public void setID(int id) {
        mID = id;
    }

    @Override
    public void setFirstName(String firstName) {
        mFristName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    @Override
    public void setUser(int id, String firstName, String lastName) {
        UserBean userBean = new UserBean(firstName,lastName);
        mUsererArray.append(id,userBean);
    }


    @Override
    public UserBean load(int id) {
        mID = id;
        UserBean userBean = mUsererArray.get(mID, new UserBean("not found",
                "not found"));
        return userBean;
    }
}
