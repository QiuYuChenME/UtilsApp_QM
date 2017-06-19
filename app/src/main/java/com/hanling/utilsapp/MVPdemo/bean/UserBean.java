package com.hanling.utilsapp.MVPdemo.bean;

/**
 * Created by XueYang on 2017/6/12.
 */

public class UserBean {
    private String FirstName;
    private String LastName;

    public UserBean(String firstName, String lastName) {
        FirstName = firstName;
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }


    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
