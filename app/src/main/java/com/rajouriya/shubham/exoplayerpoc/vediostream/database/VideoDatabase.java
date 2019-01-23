package com.rajouriya.shubham.exoplayerpoc.vediostream.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoDatabase extends SQLiteOpenHelper {

    private Context context;
    private static VideoDatabase mVideoDatabase = null;

    private VideoDatabase(Context context) {
        super(context, "VideoDB", null, 1);
        this.context = context;
    }

    public static VideoDatabase getDBInstance(Context context){
        if(mVideoDatabase == null){
            mVideoDatabase = new VideoDatabase(context);
        }
        return mVideoDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VideoTable.VIDEO_TABLE_CREATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists video");
    }
}
