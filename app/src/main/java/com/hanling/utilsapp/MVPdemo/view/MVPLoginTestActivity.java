package com.hanling.utilsapp.MVPdemo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hanling.utilsapp.MVPdemo.model.UserModel;
import com.hanling.utilsapp.MVPdemo.presenter.UserPresenter;
import com.hanling.utilsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MVPLoginTestActivity extends AppCompatActivity implements IUserView {
    @BindView(R.id.btn_get)
    Button btn_get;
    @BindView(R.id.btn_set)
    Button btn_set;
    @BindView(R.id.et_first)
    EditText et_first;
    @BindView(R.id.et_last)
    EditText et_last;
    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvplogin_test);
        ButterKnife.bind(this);
        mUserPresenter = new UserPresenter(this);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserPresenter.loadUser(0);
            }
        });
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserPresenter.saveUser(0,getFristName(),getLastName());
            }
        });
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public String getFristName() {
        return et_first.getText().toString().trim();
    }

    @Override
    public String getLastName() {
        return et_last.getText().toString().trim();
    }

    @Override
    public void setFirstName(String firstName) {
        et_first.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        et_last.setText(lastName);
    }
}
