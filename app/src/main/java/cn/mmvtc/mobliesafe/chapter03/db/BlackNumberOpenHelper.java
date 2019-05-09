package cn.mmvtc.mobliesafe.chapter03.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2019/4/25.
 * 创建数据库：balckNumber
 * 数据表：blackNumber(id,number,name,mode)
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    public BlackNumberOpenHelper(Context context) {
        super(context, "blackNumber", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blackNumber(id integer primary key autoincrement," +
                "number varchar(20),name varchar(200),mode integer");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
