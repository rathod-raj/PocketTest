/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Activites.VideoListAcivity;
import com.example.pockettest.Model.PlayListDetails;
import com.example.pockettest.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<PlayListDetails> playList;

    public PlayListRecyclerViewAdapter(Context context, List<PlayListDetails> playList) {
        this.context = context;
        this.playList = playList;
    }

    @NonNull
    @Override
    public PlayListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_playlist, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListRecyclerViewAdapter.ViewHolder holder, int position) {
        PlayListDetails playListDetails = playList.get(position);
        holder.title.setText(playListDetails.getTitle());
        holder.desc.setText(playListDetails.getDesc());
        Picasso.get()
                .load(playListDetails.getThumbnailURL())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView desc;
        private ImageView thumbnail;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            title = itemView.findViewById(R.id.playlist_title);
            desc = itemView.findViewById(R.id.playlist_desc);
            thumbnail = itemView.findViewById(R.id.playlist_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            PlayListDetails playListDetails = playList.get(getAdapterPosition());
            Intent intent = new Intent(context, VideoListAcivity.class);
            intent.putExtra("playlistID", playListDetails.getPlayListId());
            context.startActivity(intent);
        }
    }
}
