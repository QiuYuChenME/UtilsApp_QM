package com.qm.sanforvpnconnlib;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by XueYang on 2017/6/22.
 */

public class SanforDBHelper extends SQLiteOpenHelper {


    public SanforDBHelper(Context context){

        /**
         * 参数说明：
         *
         * 第一个参数： 上下文
         * 第二个参数：数据库的名称
         * 第三个参数：null代表的是默认的游标工厂
         * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
         */
        super(context, "sanforinfo.db", null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sanfortab ( _id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(20),sanformode varchar(20),sanforip varchar(20),sanforport varchar(10),sanforuser varhcar(20),sanforpwd varchar(20),sanforcertname varchar(50),sanforcertpwd varchar(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("alter table sanfortab");
    }
}
