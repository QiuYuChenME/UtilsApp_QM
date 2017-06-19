package com.hanling.utilsapp.MVPdemo.presenter;


import com.hanling.utilsapp.MVPdemo.bean.UserBean;
import com.hanling.utilsapp.MVPdemo.model.IUserModel;
import com.hanling.utilsapp.MVPdemo.model.UserModel;
import com.hanling.utilsapp.MVPdemo.view.IUserView;

public class UserPresenter {
	private IUserView mUserView;
	private IUserModel mUserModel;

	public UserPresenter(IUserView view) {
		mUserView = view;
		mUserModel = new UserModel();
	}

	public void saveUser(int id, String firstName, String lastName) {
		mUserModel.setUser(id,firstName,lastName);
	}

	public void loadUser(int id) {
		UserBean user = mUserModel.load(id);
		mUserView.setFirstName(user.getFirstName());
		mUserView.setLastName(user.getLastName());
	}
}
