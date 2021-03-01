/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:41 PM
 */

package com.example.pockettest.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Adapters.LectureRecyclerViewAdapter;
import com.example.pockettest.Adapters.PlayListRecyclerViewAdapter;
import com.example.pockettest.Model.PlayListDetails;
import com.example.pockettest.Model.VideoDetails;
import com.example.pockettest.R;
import com.example.pockettest.Util.Constants;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LecturesFragment extends Fragment {
    private Context context;
    private Toolbar toolbar;
    private Skeleton skeleton;
    private List<PlayListDetails> playList;
    private RecyclerView recyclerView;
    private PlayListRecyclerViewAdapter playListRecyclerViewAdapter;

    public LecturesFragment() {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_search,menu);
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lectures, container, false);
        context = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        skeleton = view.findViewById(R.id.lecture_skeletonLayout);


        recyclerView = view.findViewById(R.id.lectures_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        skeleton = SkeletonLayoutUtils.applySkeleton(recyclerView, R.layout.youtube_playlist, 3);
        skeleton.showSkeleton();
        skeleton.setShimmerDurationInMillis(1000);
        playList = new ArrayList<>();
        fetchPlayList();
    }

    private void fetchPlayList(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.YOUTUBE_API_PLAYLIST_URL + Urls.YOUTUBE_API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                skeleton.showOriginal();
                try{
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("items");

                    for(int i =0 ;i < items.length(); i++){
                        JSONObject parentObj = items.getJSONObject(i);
                        JSONObject parentId = parentObj.getJSONObject("id");
                        String playListId = parentId.getString("playlistId");
                        JSONObject snippet = parentObj.getJSONObject("snippet");
                        String title = snippet.getString("title");
                        String description = snippet.getString("description");
                        JSONObject parentThumbnailsObj = snippet.getJSONObject("thumbnails");
                        JSONObject highThumbnailObj = parentThumbnailsObj.getJSONObject("high");
                        String thumbnailUrl = highThumbnailObj.getString("url");

                        PlayListDetails playListDetails = new PlayListDetails();
                        playListDetails.setPlayListId(playListId);
                        playListDetails.setTitle(title);
                        playListDetails.setDesc(description);
                        playListDetails.setThumbnailURL(thumbnailUrl);
                        playList.add(playListDetails);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
                playListRecyclerViewAdapter = new PlayListRecyclerViewAdapter(context, playList);
                recyclerView.setAdapter(playListRecyclerViewAdapter);
                playListRecyclerViewAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", error.getMessage());
                Toast.makeText(context, "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
