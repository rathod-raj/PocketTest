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
import android.widget.Toast;

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
import com.example.pockettest.Model.User;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private Button save;
    private TextInputEditText name;
    private TextInputEditText mobile;
    private TextInputEditText loc;
    private Button cancel;
    UserDataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        cancel = findViewById(R.id.edit_profile_cancel);
        save = findViewById(R.id.edit_profile_save);
        name = findViewById(R.id.edit_profile_name_field);
        mobile = findViewById(R.id.edit_profile_mobile_no_field);
        loc = findViewById(R.id.edit_profile_location_field);
        db = new UserDataBaseHandler(this);

        User user = db.getUser();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() == 0 && mobile.getText().toString().length() == 0 && loc.getText().toString().length() == 0){
                    Toast.makeText(EditProfile.this, "Please edit a field", Toast.LENGTH_SHORT).show();
                }else{
                    updateUser();
                }
            }
        });
    }

    private void updateUser() {
        final User user = db.getUser();
        if(name.getText().toString().length() != 0){
            user.setName(name.getText().toString());
        }
        if(loc.getText().toString().length() != 0){
            user.setLocation(loc.getText().toString());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Urls.BASE_URL + Urls.UPDATE_USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    JSONObject userObj = obj.getJSONObject("user");


                    user.setName(userObj.getString("name"));
                    user.setLocation(userObj.getString("location"));
                    db.updateUser(user);
                    Intent intent = new Intent(EditProfile.this, MainActivity.class);
                    intent.putExtra("Edit_AccountF", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }catch(JSONException e){
                    Log.d("Edit Profile", e.toString());
                    Toast.makeText(EditProfile.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        JSONObject obj = new JSONObject(res);
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Oops! Something went wrong", Snackbar.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e1) {

                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Oops! Something went wrong", Snackbar.LENGTH_SHORT).show();
                        e1.printStackTrace();
                    } catch (JSONException e2) {

                        Log.d("error message", "returned data is not JSONObject?");
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Oops! Something went wrong", Snackbar.LENGTH_SHORT).show();
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", user.getName());
                params.put("location", user.getLocation());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String>  headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(EditProfile.this).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(EditProfile.this).addToRequestQueue(stringRequest);
    }

}
