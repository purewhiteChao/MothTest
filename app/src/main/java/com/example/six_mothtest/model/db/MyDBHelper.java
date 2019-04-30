package com.example.six_mothtest.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/4/30 0030
 * Time: 14:37
 * Describe: ${as}
 */
public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(@Nullable Context context) {
        super(context, "mothtest.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DBNames.TABLE+"("+DBNames.COL_TITLE+" text primary key,"+DBNames.COL_AUTHOR+" text,"+DBNames.COL_CONTENT+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
