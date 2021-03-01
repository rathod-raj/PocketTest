/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Adapters.LectureRecyclerViewAdapter;
import com.example.pockettest.Model.VideoDetails;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoListAcivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Skeleton skeleton;
    private List<VideoDetails> videoList;
    private String playlistID;
    private Bundle bundle;
    private AdView mAdView;
    private LectureRecyclerViewAdapter lectureRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list_acivity);
        bundle = getIntent().getExtras();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.activity_video_list_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        recyclerView = findViewById(R.id.video_recycler_view);
        skeleton = findViewById(R.id.video_skeletonLayout);
        playlistID = bundle.getString("playlistID");
        videoList = new ArrayList<>();
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoListAcivity.this));
        skeleton = SkeletonLayoutUtils.applySkeleton(recyclerView, R.layout.skeleton_video_list, 5);
        skeleton.showSkeleton();
        skeleton.setShimmerDurationInMillis(1000);
        fetchVideoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search,menu);
        MenuItem item=menu.findItem(R.id.search_filter);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchVideoList(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.YOUTUBE_API_VIDEO_URL + playlistID + "&key="+ Urls.YOUTUBE_API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                skeleton.showOriginal();
                try{
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("items");

                    for(int i =0 ;i < items.length(); i++){
                        //Log.d("response", items.getJSONObject(i).toString());
                        JSONObject parentObj = items.getJSONObject(i);
                        JSONObject snippet = parentObj.getJSONObject("snippet");
                        JSONObject resourceID = snippet.getJSONObject("resourceId");
                        String videoId = resourceID.getString("videoId");
                        String title = snippet.getString("title");
                        String description = snippet.getString("description");
                        JSONObject parentThumbnailsObj = snippet.getJSONObject("thumbnails");
                        JSONObject highThumbnailObj = parentThumbnailsObj.getJSONObject("high");
                        String thumbnailUrl = highThumbnailObj.getString("url");
                        String publishedDate = snippet.getString("publishedAt");
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                        VideoDetails videoDetails = new VideoDetails();
                        videoDetails.setVideoId(videoId);
                        videoDetails.setTitle(title);
                        videoDetails.setDesc(description);
                        videoDetails.setThumbnailURL(thumbnailUrl);
                        videoDetails.setPublishedDate(LocalDateTime.parse(publishedDate, dateTimeFormatter));
                        videoList.add(videoDetails);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
                Log.d("list",videoList.toString());
                lectureRecyclerViewAdapter = new LectureRecyclerViewAdapter(videoList, VideoListAcivity.this);
                recyclerView.setAdapter(lectureRecyclerViewAdapter);
                lectureRecyclerViewAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", error.getMessage());
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(VideoListAcivity.this).addToRequestQueue(stringRequest);
    }
}
