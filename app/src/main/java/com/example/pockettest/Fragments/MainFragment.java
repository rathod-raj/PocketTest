/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Activites.EditProfile;
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

public class MainFragment extends Fragment {
    List<Subjects> subjectsList;
    Context context;
    MainFAdapter mainFAdapter;
    CardView maincardview;

    public MainFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        maincardview=container.findViewById(R.id.mainf_cardview);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=view.getContext();
        subjectsList = new ArrayList<>();
        getSubjects();

        RecyclerView mainfrv=(RecyclerView) view.findViewById(R.id.mainf_rv);

        mainFAdapter=new MainFAdapter(context,subjectsList);
        mainfrv.setLayoutManager(new GridLayoutManager(context,2));
        mainfrv.setAdapter(mainFAdapter);
    }

    public void getSubjects(){
        subjectsList.clear();
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
                        subjectsList.add(subject);
                    }
                }catch (JSONException e){
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
                mainFAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String>  headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(context).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
