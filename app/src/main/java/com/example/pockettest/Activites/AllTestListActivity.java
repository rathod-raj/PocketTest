/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Adapters.AllTestListRecyclerViewAdapter;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllTestListActivity extends AppCompatActivity {
    private List<Quiz> quizList;
    private AdView mAdView;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView textView;
    private AllTestListRecyclerViewAdapter adapter;
    String slug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_test_list);
        toolbar = findViewById(R.id.activity_all_test_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            getSupportActionBar().setTitle(bundle.getString("name").toUpperCase());
            slug = bundle.getString("slug");
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.activity_all_test_list_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        textView = findViewById(R.id.alltest_list_textview);
        recyclerView = findViewById(R.id.alltest_list_recyclerview);
        quizList = new ArrayList<>();
        if(bundle.getBoolean("isGiven")){
            Toast.makeText(AllTestListActivity.this, "isGiven", Toast.LENGTH_SHORT).show();
            getGivenQuiz();
        }else if(bundle.getBoolean("isAll")) {
            Toast.makeText(AllTestListActivity.this, "isAll", Toast.LENGTH_SHORT).show();
            getAllQuiz();

        }
        adapter = new AllTestListRecyclerViewAdapter(AllTestListActivity.this, quizList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllTestListActivity.this));
        recyclerView.setAdapter(adapter);

    }

    public void getAllQuiz(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + slug + Urls.GET_QUIZ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.length() <= 2){
                    textView.setVisibility(View.VISIBLE);
                }else {
                    try {
                        JSONArray parentArray = new JSONArray(response);
                        for (int i = 0; i < parentArray.length(); i++) {
                            JSONObject quizObj = parentArray.getJSONObject(i);
                            Quiz quiz = new Quiz();
                            quiz.setTitle(quizObj.getString("title"));
                            quiz.setDescription(quizObj.getString("description"));
                            quiz.setTotal_marks(quizObj.getString("total_marks"));
                            quiz.setPrimary_key(quizObj.getString("pk"));
                            quiz.setPublish_date(LocalDateTime.parse(quizObj.getString("publish_date")));
                            quiz.setEnd_time(LocalDateTime.parse(quizObj.getString("end_date")));
                            LocalDateTime dateTime = LocalDateTime.now();
                            int endTimeDiff = dateTime.compareTo(quiz.getEnd_time());
                            Log.d("datetime", String.valueOf(endTimeDiff));
                            if (endTimeDiff > 0) {
                                quizList.add(quiz);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        textView.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(AllTestListActivity.this).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(AllTestListActivity.this).addToRequestQueue(stringRequest);
    }

    public void getGivenQuiz(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + slug + Urls.MY_QUIZ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.length() <= 2){
                    textView.setVisibility(View.VISIBLE);
                }else {
                    try {
                        JSONArray parentArray = new JSONArray(response);
                        for (int i = 0; i < parentArray.length(); i++) {
                            JSONObject quizObj = parentArray.getJSONObject(i);
                            Quiz quiz = new Quiz();
                            quiz.setTitle(quizObj.getString("title"));
                            quiz.setDescription(quizObj.getString("description"));
                            quiz.setTotal_marks(quizObj.getString("total_marks"));
                            quiz.setPrimary_key(quizObj.getString("pk"));
                            quiz.setPublish_date(LocalDateTime.parse(quizObj.getString("publish_date")));
                            quiz.setEnd_time(LocalDateTime.parse(quizObj.getString("end_date")));
                            LocalDateTime dateTime = LocalDateTime.now();
                            int endTimeDiff = dateTime.compareTo(quiz.getEnd_time());
                            Log.d("datetime", String.valueOf(endTimeDiff));
                            if (endTimeDiff > 0) {
                                quizList.add(quiz);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        textView.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(AllTestListActivity.this).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(AllTestListActivity.this).addToRequestQueue(stringRequest);
    }
}
