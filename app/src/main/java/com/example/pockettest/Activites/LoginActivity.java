/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.DataBase.UserDataBaseHandler;
import com.example.pockettest.Fragments.LoginFragment;
import com.example.pockettest.Fragments.SignupFragment;
import com.example.pockettest.Model.Standard;
import com.example.pockettest.Model.User;
import com.example.pockettest.R;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  LoginActivity extends AppCompatActivity {

    private Button loginB;
    private Button signupButton;
    private Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginB = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            args = (Bundle) bundle.get("args");
            ArrayList<Standard> standardList = (ArrayList<Standard>) args.getSerializable("standardList");
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loginB.getLayoutParams();
        layoutParams.width = ((getResources().getDisplayMetrics().widthPixels)/5)*2;
        layoutParams.height = (getResources().getDisplayMetrics().heightPixels)/10;

        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) signupButton.getLayoutParams();
        layoutParams2.width = ((getResources().getDisplayMetrics().widthPixels)/5)*2;
        layoutParams2.height = (getResources().getDisplayMetrics().heightPixels)/10;

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.show(getSupportFragmentManager(), "Login Modal Bottom Sheet");
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupFragment signupFragment = new SignupFragment();
                signupFragment.setArguments(args);
                signupFragment.show(getSupportFragmentManager(), "Signup Modal Bottom Sheet");
            }
        });
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}

