/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:41 PM
 */

package com.example.pockettest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Activites.AllTestsActivity;
import com.example.pockettest.Activites.EditProfile;
import com.example.pockettest.Activites.GivenTestActivity;
import com.example.pockettest.Activites.LoginActivity;
import com.example.pockettest.Activites.SplashActivity;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.DataBase.UserDataBaseHandler;
import com.example.pockettest.Model.User;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment implements View.OnClickListener{

    private Button editProfile;
    private Button starredQuizes;
    private Button givenQuizes;
    private Button allquizes;
    private Button logOut;
    private TextView name;
    private TextView email;
    private TextView mobileNo;
    private UserDataBaseHandler db;
    private AdView mAdView;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        name = view.findViewById(R.id.account_name);
        email = view.findViewById(R.id.account_email);
        mobileNo = view.findViewById(R.id.account_mobile);
        editProfile = view.findViewById(R.id.account_editProfile);
        givenQuizes = view.findViewById(R.id.account_quiz_taken);
        allquizes=view.findViewById(R.id.account_quiz_history);
        logOut = view.findViewById(R.id.account_logout);
        db = new UserDataBaseHandler(getActivity());
        User user = db.getUser();
        MobileAds.initialize(view.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        email.setText(user.getEmail());
        name.setText(user.getName());
        mobileNo.setText("+91-"+user.getMobileNo());

        editProfile.setOnClickListener(this);
        givenQuizes.setOnClickListener(this);
        allquizes.setOnClickListener(this);
        logOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.account_editProfile:
                Log.d("pressed", "Button pressed");
                startActivity(new Intent(getActivity(), EditProfile.class));
                break;
            case R.id.account_quiz_taken:
                startActivity(new Intent(getActivity(), GivenTestActivity.class));
                break;
            case R.id.account_quiz_history:
                startActivity(new Intent(getActivity(), AllTestsActivity.class));
                break;

            case R.id.account_logout:
                logout();
                break;
        }
    }

    public  void logout(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.BASE_URL + Urls.LOGOUT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                db.deleteUser();
                SharedPrefManager.getInstance(getActivity()).deleteToken();
                startActivity(new Intent(getContext(), SplashActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("status", "error occured");
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String>  headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getActivity()).getToken());
                return headers;
            }
        };
        Log.d("error", "Bearer " +SharedPrefManager.getInstance(getActivity()).getToken());
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
