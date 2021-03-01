/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:38 PM
 */

package com.example.pockettest.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Adapters.AllTestRecyclerViewAdapter;
import com.example.pockettest.Adapters.MainFAdapter;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.Model.Subjects;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllTestsActivity extends AppCompatActivity {
    private RecyclerView rvall;
    private List<Subjects> subjectList;
    private AllTestRecyclerViewAdapter mainFAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tests);

        rvall = findViewById(R.id.rv_all);
        subjectList = new ArrayList<>();
        getSubjects();
        mainFAdapter=new AllTestRecyclerViewAdapter(AllTestsActivity.this,subjectList);
        rvall.setLayoutManager(new GridLayoutManager(AllTestsActivity.this,2));
        rvall.setAdapter(mainFAdapter);

    }

    public void getSubjects(){
        subjectList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + Urls.GET_SUBJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray parentObj = new JSONArray(response);
                    for(int i =0; i< parentObj.length(); i++){
                        JSONObject subjectObj = parentObj.getJSONObject(i);
                        Subjects subject = new Subjects();
                        subject.setName(subjectObj.getString("name"));
                        subject.setSlug(subjectObj.getString("slug"));
                        subjectList.add(subject);
                    }
                }catch (JSONException e){
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
                mainFAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String>  headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(AllTestsActivity.this).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(AllTestsActivity.this).addToRequestQueue(stringRequest);
    }

}