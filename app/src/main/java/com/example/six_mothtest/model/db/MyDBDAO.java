package com.example.six_mothtest.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.six_mothtest.MyApp;
import com.example.six_mothtest.model.bean.GuShiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/4/30 0030
 * Time: 14:45
 * Describe: ${as}
 */
public class MyDBDAO {
    private MyDBHelper myDBHelper;

    public MyDBDAO(){
        myDBHelper = new MyDBHelper(MyApp.getContext());
    }

    public void inserDao(ContentValues values){
        SQLiteDatabase writableDatabase = myDBHelper.getWritableDatabase();
        try {
            writableDatabase.replace(DBNames.TABLE,null,values);
            Log.i("GC","插入成功");
        } catch (Exception e) {
            Log.i("GC","插入失败");
        }finally {
            writableDatabase.close();
        }
    }
    public List<GuShiBean.ResultBean> selectDao(){
        Log.i("GC","读取数据了");
        SQLiteDatabase readableDatabase = myDBHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from " + DBNames.TABLE, null);
        List<GuShiBean.ResultBean> list = new ArrayList<>();
        while(cursor.moveToNext()){
            GuShiBean.ResultBean bean = new GuShiBean.ResultBean();
            bean.setTitle(cursor.getString(cursor.getColumnIndex(DBNames.COL_TITLE)));
            bean.setAuthors(cursor.getString(cursor.getColumnIndex(DBNames.COL_AUTHOR)));
            bean.setContent(cursor.getString(cursor.getColumnIndex(DBNames.COL_CONTENT)));
            list.add(bean);
        }
        cursor.close();
        return list;
    }
}
