package com.rajouriya.shubham.exoplayerpoc.vediostream.presenter;

import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoSourceDataService {
    private static ApiInterface apiInterface;
    private static VideoDataService videoDataService;
    private static VideoSourceDataService videoSourceDataService = null;

    private VideoSourceDataService(VideoDataService videoDataService) {
        this.videoDataService = videoDataService;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public static VideoSourceDataService getVideoDataSourceInstance(VideoDataService videoDataService){
        if(videoSourceDataService == null){
            videoSourceDataService = new VideoSourceDataService(videoDataService);
        }
        return videoSourceDataService;
    }

    public void callVideoDataService(){
       videoDataService.onVideoDataDownloadStart();
        Call<List<VideoModel>> call = apiInterface.doGetListResources();
        call.enqueue(new Callback<List<VideoModel>>() {
            @Override
            public void onResponse(Call<List<VideoModel>> call, Response<List<VideoModel>> response) {
              videoDataService.onVideoDataAvailable(response.body());
              call.cancel();
            }

            @Override
            public void onFailure(Call<List<VideoModel>> call, Throwable t) {
                videoDataService.onDataDownloadFaliur(t.getMessage());
            }
        });
    }
}
