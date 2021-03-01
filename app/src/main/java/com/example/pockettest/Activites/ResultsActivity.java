/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.pockettest.Model.Quiz;
import com.example.pockettest.Model.UserQuiz;
import com.example.pockettest.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ResultsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView quiz_name;
    private TextView quiz_desc;
    private TextView user_score;
    private TextView quiz_marks;
    private TextView given_date;
    private Button go_to;
    private Bundle bundle;
    private Quiz quiz;
    private UserQuiz userQuiz;
    private InterstitialAd mInterstitialAd;
    private DelayedProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        toolbar = findViewById(R.id.result_toolbar);
        toolbar.setTitle("RESULT");
        setActionBar(toolbar);
        quiz_name = findViewById(R.id.result_quiz_name);
        quiz_desc = findViewById(R.id.result_quiz_desc);
        quiz_marks = findViewById(R.id.result_quiz_score);
        user_score = findViewById(R.id.result_user_score);
        given_date = findViewById(R.id.result_given_date);
        go_to = findViewById(R.id.result_go_home);

        bundle = getIntent().getExtras();
        quiz = (Quiz) bundle.getSerializable("quiz");
        userQuiz = (UserQuiz) bundle.getSerializable("userquiz");
        dialog = new DelayedProgressDialog();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                finish();
                startActivity(intent);
            }

        });
        quiz_name.setText(quiz.getTitle());
        quiz_desc.setText(quiz.getDescription());
        quiz_marks.setText(quiz.getTotal_marks());
        user_score.setText(userQuiz.getUser_score());

        go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            while(!mInterstitialAd.isLoaded())
            {
             dialog.show(getSupportFragmentManager(),"Please Wait...");
            }
              dialog.cancel();
              mInterstitialAd.show();
            }
        });

    }
}