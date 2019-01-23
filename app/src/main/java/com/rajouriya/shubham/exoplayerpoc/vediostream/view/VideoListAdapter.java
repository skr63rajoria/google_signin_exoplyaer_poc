package com.rajouriya.shubham.exoplayerpoc.vediostream.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.rajouriya.shubham.exoplayerpoc.R;
import com.rajouriya.shubham.exoplayerpoc.vediostream.model.VideoModel;
import com.rajouriya.shubham.exoplayerpoc.vediostream.presenter.RecyclerClickListesner;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder>  {
    private List<VideoModel> videoModels;
    private Context mContext;
    private int resourceId;
    private RecyclerClickListesner clickListesner;

    public VideoListAdapter(List<VideoModel> videoModels, Context mContext,int resourceId,RecyclerClickListesner clickListesner) {
        this.videoModels = videoModels;
        this.mContext = mContext;
        this.resourceId = resourceId;
        this.clickListesner = clickListesner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);
        return new VideoListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final VideoModel itemModel = (VideoModel)videoModels.get(position);
        holder.videoTitle.setText(itemModel.getTitle());
        holder.videoDescription.setText(itemModel.getDescription());
        holder.itemView.setTag(itemModel);

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.image_not_found);
        if (mContext != null) {
            Glide.with(mContext)
                    .load(itemModel.getUrl())
                    .apply(requestOptions)
                    .into(holder.videoThumbnail);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListesner.onRecyclerClildClick(holder.itemView);
                }
            });
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView videoTitle, videoDescription;
        public ImageView videoThumbnail;

        public MyViewHolder(View view) {
            super(view);
            videoTitle = (TextView) view.findViewById(R.id.video_thumbnail_title_tv);
            videoDescription = (TextView) view.findViewById(R.id.video_thumbnail_description);
            videoThumbnail = (ImageView) view.findViewById(R.id.video_thumbnail_iv);
        }
    }

}
