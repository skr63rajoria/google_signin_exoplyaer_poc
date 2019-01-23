package com.rajouriya.shubham.exoplayerpoc.vediostream.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rajouriya.shubham.exoplayerpoc.R;
import com.rajouriya.shubham.exoplayerpoc.vediostream.database.VideoDatabase;
import com.rajouriya.shubham.exoplayerpoc.vediostream.database.VideoTable;
import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;
import com.rajouriya.shubham.exoplayerpoc.vediostream.presenter.RecyclerClickListesner;

import java.util.List;

public class VedioDetailActivity extends AppCompatActivity implements Player.EventListener, RecyclerClickListesner {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private Context mContext;
    private String url, videoId;
    private RecyclerView videoListRecyclerView;
    private TextView videoTitle, videoDescription;
    private VideoModel currentVideo;
    private DefaultDataSourceFactory defaultDataSourceFactory;
    private int startDuration = 0;
    private String currentVieoId;
    private long contentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_detail);
        playerView = (PlayerView) findViewById(R.id.player_view);
        videoTitle = (TextView) findViewById(R.id.video_detail_title_tv);
        videoDescription = (TextView) findViewById(R.id.video_detail_description);
        videoListRecyclerView = (RecyclerView) findViewById(R.id.video_list_recycler_view);
        mContext = this;
        Intent intent = getIntent();
        if (intent.hasExtra("vedio_url") && intent.hasExtra("vedio_id")) {
            url = intent.getStringExtra("vedio_url");
            videoId = intent.getStringExtra("vedio_id");
            currentVieoId = videoId;
            currentVideo = VideoTable.getVideoByID(videoId, VideoDatabase.getDBInstance(mContext).getWritableDatabase());
            setLastPlayerTime(currentVideo);
            videoTitle.setText(currentVideo.getTitle());
            videoDescription.setText(currentVideo.getDescription());
        }
        List<VideoModel> videoModels = VideoTable.getAllVideosExcludeSelectedVideo(videoId, VideoDatabase.getDBInstance(mContext).getWritableDatabase());
        makeRecyclerAniamtion();
        VideoListAdapter videoListAdapter = new VideoListAdapter(videoModels, mContext, R.layout.video_detail_recycler_cell, this);
        videoListRecyclerView.setAdapter(videoListAdapter);

    }

    private void setLastPlayerTime(VideoModel currentVideo) {
        if (currentVideo.getLastPlayerDuration() != null && !currentVideo.getLastPlayerDuration().equals("")) {
            startDuration = Integer.parseInt(currentVideo.getLastPlayerDuration());
        } else {
            startDuration = 0;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        playerView.setPlayer(player);
        defaultDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayer"));
        startPlayer(url);
    }

    private void startPlayer(String videoUrl) {
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(videoUrl));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.addListener(this);
        for (int i = 0; i < videoListRecyclerView.getChildCount(); i++) {
            View view = videoListRecyclerView.getChildAt(i);
            TextView titleTv = view.findViewById(R.id.video_thumbnail_title_tv);
            VideoModel videoModel = (VideoModel) view.getTag();
            if (videoModel.getId() == currentVieoId) {
                titleTv.setTextColor(getResources().getColor(R.color.red));
            } else {
                titleTv.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        playerView.setPlayer(null);
        player.release();
        player = null;
    }

    private void makeRecyclerAniamtion() {
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        videoListRecyclerView.setLayoutAnimation(animation);
        videoListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady) {
            player.seekTo(startDuration);
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onRecyclerClildClick(View view) {
        //VideoModel  tempVideoTable = VideoTable.getVideoByID(videoId,VideoDatabase.getDBInstance(mContext).getWritableDatabase());
        contentPosition = player.getContentPosition();
        VideoTable.updateSingleVideo(videoId, contentPosition + "", VideoDatabase.getDBInstance(mContext).getWritableDatabase());
        VideoModel videoModel = (VideoModel) view.getTag();
        videoId = videoModel.getId();
        videoTitle.setText(videoModel.getTitle());
        videoDescription.setText(videoModel.getDescription());
        currentVideo = VideoTable.getVideoByID(videoModel.getId(), VideoDatabase.getDBInstance(mContext).getWritableDatabase());
        setLastPlayerTime(currentVideo);
        startPlayer(videoModel.getUrl());
    }

    @Override
    protected void onPause() {
        super.onPause();
        contentPosition = player.getContentPosition();
        VideoTable.updateSingleVideo(videoId, contentPosition + "", VideoDatabase.getDBInstance(mContext).getWritableDatabase());
    }
}
