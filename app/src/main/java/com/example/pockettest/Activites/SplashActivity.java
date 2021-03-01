/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Model.Standard;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {
    private TextView appname;
    private List<Standard> standardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appname=findViewById(R.id.app_name);
        standardList = new ArrayList<Standard>();
        getStandard();


    }

    private void getStandard(){
        standardList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + Urls.GET_CLASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray parentObj = new JSONArray(response);
                    for(int i =0; i< parentObj.length(); i++){
                        JSONObject standardObj = parentObj.getJSONObject(i);
                        Standard standard = new Standard();
                        standard.setClass_no(standardObj.getString("class_no"));
                        standard.setPrimary_key(standardObj.getString("id"));
                        standardList.add(standard);
                    }
                    Log.d("parentObj", parentObj.toString());
                    Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("standardList", (Serializable) standardList);
                    intent.putExtra("args", args);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }catch (JSONException e){
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("on Response Error",error.toString());
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Sever Error. Please restart the app.", Snackbar.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(SplashActivity.this).addToRequestQueue(stringRequest);
    }
}
