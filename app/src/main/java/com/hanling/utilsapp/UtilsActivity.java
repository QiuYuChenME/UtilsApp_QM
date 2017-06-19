package com.hanling.utilsapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hanling.mlibrary.LibMainActivity;
import com.hanling.utilsapp.MVPdemo.view.MVPLoginTestActivity;
import com.hanling.utilsapp.bean.TestBean;
import com.hanling.utilsapp.utils.Constant;
import com.hanling.xfvoicelibrary.XFLibVoiceActivity;
import com.qm.sanforvpnconnlib.SanforConnActivity;
import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SangforAuth;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UtilsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.btn_utils)
    Button btn_utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(UtilsActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                1);
        btn_utils.setText(R.string.btnutils);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TestBean testBean = new TestBean();
        testBean.setA("A");
        testBean.setB("B");
        testBean.setC("C");
        Gson gson  = new Gson();
        String s = gson.toJson(testBean).toString();
//        Logger.t("mytag").json(Constant.JsonStringTest);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utils, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, LibMainActivity.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, XFLibVoiceActivity.class));
            finish();
        } else if (id == R.id.nav_slideshow) {

            startActivity(new Intent(this, MVPLoginTestActivity.class));
        } else if (id == R.id.nav_manage) {
                if(SangforAuth.getInstance().vpnQueryStatus() != IVpnDelegate.VPN_STATUS_AUTH_OK){
                    Intent i = new Intent(this, SanforConnActivity.class);
                    startActivity(i);
                }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 黄油刀点击事件
     * @param button
     */
    @OnClick(R.id.btn_utils)
    public void A(Button button){
        Toast.makeText(this, "被点击了"+button.getText(), Toast.LENGTH_SHORT).show();

    }

}
