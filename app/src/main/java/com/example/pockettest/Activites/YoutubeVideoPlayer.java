/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeVideoPlayer extends YouTubeBaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private YouTubePlayerView youTubePlayerView;
    private Bundle extras;
    private String videoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);
        youTubePlayerView = findViewById(R.id.youtube_video_player);
        extras = getIntent().getExtras();
        if(extras != null) {
            videoId = extras.getString("videoId");
        }
        youTubePlayerView.initialize(Urls.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(YoutubeVideoPlayer.this, "Opps! Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
