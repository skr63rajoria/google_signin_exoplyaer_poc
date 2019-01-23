package com.rajouriya.shubham.exoplayerpoc.vediostream.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rajouriya.shubham.exoplayerpoc.R;
import com.rajouriya.shubham.exoplayerpoc.Util.Utility;
import com.rajouriya.shubham.exoplayerpoc.auth.model.User;
import com.rajouriya.shubham.exoplayerpoc.auth.presenter.GoogleSignUpProvider;
import com.rajouriya.shubham.exoplayerpoc.auth.view.SignInSignUpActivity;
import com.rajouriya.shubham.exoplayerpoc.vediostream.database.VideoDatabase;
import com.rajouriya.shubham.exoplayerpoc.vediostream.database.VideoTable;
import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;
import com.rajouriya.shubham.exoplayerpoc.vediostream.presenter.RecyclerClickListesner;
import com.rajouriya.shubham.exoplayerpoc.vediostream.presenter.VideoDataService;
import com.rajouriya.shubham.exoplayerpoc.vediostream.presenter.VideoSourceDataService;

import java.util.List;

public class VedioListActivity extends AppCompatActivity implements VideoDataService,RecyclerClickListesner {
    private TextView userNameTv;
    private ImageView logoutIv;
    private RecyclerView videoThumbRecycler;
    private Context mContext = null;
    private ProgressDialog progressDialog;
    VideoListAdapter videoListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_list);
        userNameTv = (TextView)findViewById(R.id.user_name_tv);
        //logoutIv = (ImageView)findViewById(R.id.logout_iv);
        videoThumbRecycler = (RecyclerView)findViewById(R.id.video_list_recycler_view);
        initializeContext();
        userNameTv.setText("Welcome "+Utility.getPref(mContext,"user_name").toUpperCase());
        if (Utility.isOnline(mContext)) {
            VideoSourceDataService.getVideoDataSourceInstance(this).callVideoDataService();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Exit");
            builder.setMessage("Please check your internet connection and try again");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    android.os.Process.killProcess(Process.myPid());
                }
            });
            builder.show();
        }

    }

    private void initializeContext() {
        if (mContext == null) {
            mContext = this;
        }
    }

    @Override
    public void onVideoDataAvailable(List<VideoModel> videoModels) {
       progressDialog.dismiss();
        makeRecyclerAniamtion();
        VideoTable.addAllVideo(videoModels,VideoDatabase.getDBInstance(mContext).getWritableDatabase());
        videoListAdapter = new VideoListAdapter(videoModels,mContext,R.layout.video_cell,this);
        videoThumbRecycler.setAdapter(videoListAdapter);
    }

    @Override
    public void onDataDownloadFaliur(String errorMsg) {
      progressDialog.dismiss();
        Toast.makeText(mContext,errorMsg,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoDataDownloadStart() {
        try {
            initializeContext();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private void makeRecyclerAniamtion() {
        initializeContext();
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        videoThumbRecycler.setLayoutAnimation(animation);
        videoThumbRecycler.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Exit");
        builder.setMessage("You want to exit application");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                android.os.Process.killProcess(Process.myPid());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRecyclerClildClick(View view) {
        Intent intent = new Intent(mContext,VedioDetailActivity.class);
        VideoModel videoModel = (VideoModel) view.getTag();
        intent.putExtra("vedio_url",videoModel.getUrl());
        intent.putExtra("vedio_id",videoModel.getId());
        startActivity(intent);
    }
}
