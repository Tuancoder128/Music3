package com.bkav.demo.music.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.bkav.demo.music.ThongTinBaiHat;

public class Database extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "db_luubaihat";
    private static final String TABLE_NAME = "bang_baihat";
    private static final String TABLE_TENBAIHAT = "tb_tenbaihat";
    private static final String TABLE_THELOAI = "tb_theloai";
    private static final String TABLE_THOIGIAN = "tb_thoigian";
    private static final String TABLE_SOTHUTU = "tb_sothutu";
    private static final String TABLE_HINHALBUM = "tb_hinhalbum";
    private static final int VERSION = 1;

    String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
            TABLE_SOTHUTU + " integer primary key, " +
            TABLE_TENBAIHAT + " TEXT, " +
            TABLE_THELOAI + " TEXT, " +
            TABLE_HINHALBUM + " TEXT, " +
            TABLE_THOIGIAN + " TEXT)";

    public void addMusic(ThongTinBaiHat thongTinBaiHat) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_TENBAIHAT, thongTinBaiHat.getTenBaiHat());
        contentValues.put(TABLE_THELOAI, thongTinBaiHat.getTheloai());
        contentValues.put(TABLE_THOIGIAN, thongTinBaiHat.getThoigian());
        contentValues.put(TABLE_HINHALBUM, thongTinBaiHat.getHinhAlbum());
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

    }


    public Database(Context context) {

        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
