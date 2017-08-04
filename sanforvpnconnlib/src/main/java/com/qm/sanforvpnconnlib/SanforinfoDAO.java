package com.qm.sanforvpnconnlib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.FocusFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XueYang on 2017/6/22.
 */

public class SanforinfoDAO {
    private static final String TAG = "SanforinfoDAO";
    private SanforDBHelper sanforDBHelper;

    /**
     * dao类需要实例化数据库Help类,只有得到帮助类的对象我们才可以实例化 SQLiteDatabase
     *
     * @param context
     */
    public SanforinfoDAO(Context context) {
        sanforDBHelper = new SanforDBHelper(context);
    }

    // 将数据库打开帮帮助类实例化，然后利用这个对象
    // 调用谷歌的api去进行增删改查

    // 增加的方法吗，返回的的是一个long值
    public long addDate(String name, String mode, String ip, String port, String user, String pwd) {
        // 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
        // getWritableDatabase(); 执行后数据库文件才会生成
        // 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看
        SQLiteDatabase sqLiteDatabase = sanforDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("sanformode", mode);
        contentValues.put("sanforip", ip);
        contentValues.put("sanforport", port);
        contentValues.put("sanforuser", user);
        contentValues.put("sanforpwd", pwd);
        // 返回,显示数据添加在第几行
        // 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
        // 因为自增长
        long rowid = sqLiteDatabase.insert("sanfortab", null, contentValues);

        sqLiteDatabase.close();
        return rowid;
    }

    public List<SanforInfoBean> loadData() {

        List<SanforInfoBean> list = new ArrayList<>();
        SQLiteDatabase readableDatabase = sanforDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.query("sanfortab", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            SanforInfoBean sanforInfoBean = new SanforInfoBean();

            sanforInfoBean.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            sanforInfoBean.setName(cursor.getString(cursor.getColumnIndex("name")));
            sanforInfoBean.setMode(cursor.getString(cursor.getColumnIndex("sanformode")));
            sanforInfoBean.setIP(cursor.getString(cursor.getColumnIndex("sanforip")));
            sanforInfoBean.setPort(cursor.getString(cursor.getColumnIndex("sanforport")));
            sanforInfoBean.setUser(cursor.getString(cursor.getColumnIndex("sanforuser")));
            sanforInfoBean.setPwd(cursor.getString(cursor.getColumnIndex("sanforpwd")));
            list.add(sanforInfoBean);
        }
        readableDatabase.close();
        return list;
    }

    public int deletedata(String name) {

        SQLiteDatabase sqLiteDatabase = sanforDBHelper.getWritableDatabase();
        int status = sqLiteDatabase.delete("sanfortab", "name=?", new String[]{name});
        return status;
    }
}
