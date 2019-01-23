package com.rajouriya.shubham.exoplayerpoc.vediostream.presenter;

import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;

import java.util.List;

public interface VideoDataService {
    public void onVideoDataAvailable(List<VideoModel> videoModels);
    public void onDataDownloadFaliur(String errorMsg);
    public void onVideoDataDownloadStart();
}
