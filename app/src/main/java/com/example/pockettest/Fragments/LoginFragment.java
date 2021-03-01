/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:41 PM
 */

package com.example.pockettest.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Activites.LoginActivity;
import com.example.pockettest.Activites.MainActivity;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.DataBase.UserDataBaseHandler;
import com.example.pockettest.Model.User;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends BottomSheetDialogFragment {

    Context context;
    Button loginButton;
    TextInputEditText emailInput;
    TextInputEditText passwordInput;
    UserDataBaseHandler db;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        loginButton = view.findViewById(R.id.login_submit_button);
        emailInput = view.findViewById(R.id.login_email_field);
        passwordInput = view.findViewById(R.id.login_password_field);
        db = new UserDataBaseHandler(context);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()){
                    login();
                }
            }
        });
    }

    private Boolean validateData(){
        if(emailInput.getText().toString().isEmpty()) {
            emailInput.setError("Enter E-mail");
            return false;
        }
        if(passwordInput.getText().toString().isEmpty()) {
            passwordInput.setError("Enter Password");
            return false;
        }
        return true;
    }

    private void login() {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.BASE_URL + Urls.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    JSONObject userObj = obj.getJSONObject("user");
                    if(userObj.getBoolean("is_student")){
                        User user = new User();
                        user.setEmail(userObj.getString("email"));
                        user.setName(userObj.getString("name"));
                        user.setMobileNo(userObj.getString("mobile_no"));
                        user.setLocation(userObj.getString("location"));
                        user.setClass_no(userObj.getString("class_no"));
                        //adding user to database
                        db.addUser(user);

                        //setting up Auth token
                        String token = obj.getString("token");
                        SharedPrefManager.getInstance(context).generateToken(token);
                        startActivity(new Intent(context, MainActivity.class));
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Verification Required.")
                                .setMessage("Please wait for verification from admin to proceed further")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        context.startActivity(new Intent(context, LoginActivity.class));
                                    }
                                });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }

                }catch(JSONException e){
                    Toast.makeText(context, "Internal Server Error. Please contact the administration",  Toast.LENGTH_SHORT).show();
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
                        JSONArray errorArray = obj.getJSONArray("non_field_errors");
                        Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e1) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        Log.d("error message", "returned data is not JSONObject?");
                        Toast.makeText(context, "Server error. Please contact the administration.",  Toast.LENGTH_SHORT).show();
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
