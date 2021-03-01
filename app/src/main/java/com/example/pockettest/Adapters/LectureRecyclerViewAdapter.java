/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Activites.YoutubeVideoPlayer;
import com.example.pockettest.Model.VideoDetails;
import com.example.pockettest.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.Format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class LectureRecyclerViewAdapter extends RecyclerView.Adapter<LectureRecyclerViewAdapter.ViewHolder>{

    private List<VideoDetails> videoList;
    private Context ctx;

    public LectureRecyclerViewAdapter(List<VideoDetails> itemList, Context ctx) {
        this.videoList = itemList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public LectureRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list, parent, false);
        return new ViewHolder(view, ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureRecyclerViewAdapter.ViewHolder holder, int position) {
        VideoDetails videoDetails = videoList.get(position);
        holder.title.setText(videoDetails.getTitle());
        Picasso.get()
                .load(videoDetails.getThumbnailURL())
                .into(holder.thumbnail);
        holder.desc.setText(videoDetails.getDesc());
        holder.publishedAt.setText(videoDetails.getPublishedDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private ImageView thumbnail;
        private TextView desc;
        private TextView publishedAt;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.video_title);
            thumbnail = itemView.findViewById(R.id.thumbnail_id);
            desc = itemView.findViewById(R.id.video_desc);
            publishedAt = itemView.findViewById(R.id.video_publishedAt);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            VideoDetails videoDetails = videoList.get(getAdapterPosition());
            Intent intent = new Intent(context, YoutubeVideoPlayer.class);
            intent.putExtra("videoId", videoDetails.getVideoId());
            context.startActivity(intent);

        }
    }
}
