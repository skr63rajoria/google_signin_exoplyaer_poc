package com.rajouriya.shubham.exoplayerpoc.vediostream.presenter;

import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/media.json?print=pretty")
    Call<List<VideoModel>> doGetListResources();
}
