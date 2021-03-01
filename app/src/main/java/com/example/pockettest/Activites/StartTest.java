/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.Model.Answer;
import com.example.pockettest.Model.Questions;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartTest extends AppCompatActivity {

    private Button starttest;
    private Quiz quiz;
    private TextView name;
    private TextView desc;
    private TextView marks;
    private TextView publish_time;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        starttest=findViewById(R.id.start_test_button);

        Bundle bundle = getIntent().getExtras();
        quiz = (Quiz) bundle.getSerializable("quiz");
        name = findViewById(R.id.start_test_name);
        desc = findViewById(R.id.start_test_description);
        marks = findViewById(R.id.start_total_marks);
        name.setText(quiz.getTitle());
        desc.setText(quiz.getDescription());
        marks.setText("Total marks : " +quiz.getTotal_marks());

        starttest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             starttest();
            }
        });

    }
    private void starttest(){
        Intent intent=new Intent(StartTest.this,MainTest.class);
        intent.putExtra("quiz", quiz);
        startActivity(intent);

    }

}