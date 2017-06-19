package com.hanling.mlibrary.greendaodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hanling.mlibrary.R;
import com.hanling.mlibrary.greendaodemo.entity.QMTester;
import com.hanling.mlibrary.greendaodemo.gen.DaoMaster;
import com.hanling.mlibrary.greendaodemo.gen.DaoSession;
import com.hanling.mlibrary.greendaodemo.gen.QMTesterDao;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreenDaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);

        /**
         *初始化数据库
         */
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplication(), "qmtest-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        QMTesterDao qmTesterDao = daoSession.getQMTesterDao();

        /**
         * 清空
         */
        qmTesterDao.deleteAll();
        /**
         *当Query没有返回语气结果，故障排查
         */
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        /**
         * 插入数据  不能用同一个对象进行插入
         */
        QMTester qmTester = new QMTester();
        qmTester.setAge(18);
        qmTester.setName("Qyc");
        qmTester.setId(null);
        qmTester.setSex("男");
        qmTesterDao.insertOrReplace(qmTester);

        QMTester QM = new QMTester();
        QM.setAge(26);
        QM.setName("QM");
        QM.setId(null);
        QM.setSex("男");
        qmTesterDao.insertOrReplace(QM);

        QMTester WH = new QMTester();

        WH.setAge(27);
        WH.setName("WH");
        WH.setId(null);
        WH.setSex("女");
        qmTesterDao.insertOrReplace(WH);

        QMTester XYT = new QMTester();
        XYT.setAge(25);
        XYT.setName("XYT");
        XYT.setId(null);
        XYT.setSex("女");
        qmTesterDao.insertOrReplace(XYT);

        QMTester Tina = new QMTester();
        Tina.setAge(25);
        Tina.setName("Tina");
        Tina.setId(null);
        Tina.setSex("女");
        qmTesterDao.insertOrReplace(Tina);

        QMTester QT = new QMTester();
        QT.setAge(100);
        QT.setName("QT");
        QT.setId(null);
        QT.setSex("男");
        qmTesterDao.insertOrReplace(QT);


        /**
         *  查找数据
         */

        List<QMTester> userList = qmTesterDao.queryBuilder()
                .where(QMTesterDao.Properties.Id.notEq(999))
//                .where(QMTesterDao.Properties.Id.between(1, 3))
                .orderAsc(QMTesterDao.Properties.Id)
                .limit(5)
                .build()
                .list();
        //.list 表示查询为一个集合 .unique表示查询一个对象
        Logger.d("修改前的数据集合"+userList);

        /**
         * 取年龄大于等于20的第一条数据
         */
        List<QMTester> unique = qmTesterDao.queryBuilder().where(QMTesterDao.Properties.Age.ge(20)).build().list();
        Logger.d("需要修改的单条数据 （条件年龄大于等于20）"+ unique);
        unique.get(0).setName("testtesttest");
        qmTesterDao.update(unique.get(0));

        userList = qmTesterDao.queryBuilder()
                .where(QMTesterDao.Properties.Id.notEq(999))
//                .where(QMTesterDao.Properties.Id.between(1,3))
                .orderAsc(QMTesterDao.Properties.Id)
                .limit(5)
                .build()
                .list();
        Logger.d("修改后的数据集合"+userList);
    }
}
