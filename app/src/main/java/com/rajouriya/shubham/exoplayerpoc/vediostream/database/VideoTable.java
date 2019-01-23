package com.rajouriya.shubham.exoplayerpoc.vediostream.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class VideoTable {
    public static String VIDEO_TABLE = "video";
    public static String VIDEO_TABLE_CREATION = "create table video (video_id text primary key,video_url text, video_title text, video_description text,last_played_duration text)";

    public static void addAllVideo(List<VideoModel> videoModels, SQLiteDatabase database){
        for(VideoModel videoModel: videoModels){
            ContentValues values = new ContentValues();
            values.put("video_id",videoModel.getId());
            values.put("video_url",videoModel.getUrl());
            values.put("video_title",videoModel.getTitle());
            values.put("video_description",videoModel.getDescription());
            values.put("last_played_duration","");
            database.insert(VIDEO_TABLE,null,values);
        }
    }

    public static void updateSingleVideo(String videoId,String duration,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("last_played_duration",duration);
        database.update(VIDEO_TABLE,values,"video_id=?", new String[]{videoId});
    }

    public static VideoModel getVideoByID(String videoId,SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from video where video_id = ?", new String[]{videoId});
        VideoModel videoModel = new VideoModel();
        try {
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    videoModel.setId(cursor.getString(cursor.getColumnIndex("video_id")));
                    videoModel.setUrl(cursor.getString(cursor.getColumnIndex("video_url")));
                    videoModel.setTitle(cursor.getString(cursor.getColumnIndex("video_title")));
                    videoModel.setDescription(cursor.getString(cursor.getColumnIndex("video_description")));
                    videoModel.setLastPlayerDuration(cursor.getString(cursor.getColumnIndex("last_played_duration")));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return videoModel;
    }

    public static List<VideoModel> getAllVideosExcludeSelectedVideo(String excldeVideoId,SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from video",null); // where video_id <>"+excldeVideoId
        List<VideoModel> videoList = new ArrayList();
        try {
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    VideoModel videoModel = new VideoModel();
                    videoModel.setId(cursor.getString(cursor.getColumnIndex("video_id")));
                    videoModel.setUrl(cursor.getString(cursor.getColumnIndex("video_url")));
                    videoModel.setTitle(cursor.getString(cursor.getColumnIndex("video_title")));
                    videoModel.setDescription(cursor.getString(cursor.getColumnIndex("video_description")));
                    videoModel.setLastPlayerDuration(cursor.getString(cursor.getColumnIndex("last_played_duration")));
                    videoList.add(videoModel);
                }
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return videoList;
        }

    }
