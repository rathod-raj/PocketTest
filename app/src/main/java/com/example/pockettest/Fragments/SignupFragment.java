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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pockettest.Activites.LoginActivity;
import com.example.pockettest.Activites.MainActivity;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.DataBase.UserDataBaseHandler;
import com.example.pockettest.Model.Standard;
import com.example.pockettest.Model.User;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignupFragment extends BottomSheetDialogFragment {

    private Context context;
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText password2;
    private TextInputEditText mobile_no;
    private TextInputEditText location;
    private MaterialButton signupButton;
    private AutoCompleteTextView class_no;
    private UserDataBaseHandler db;
    List<Standard> standards;

    public SignupFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        db = new UserDataBaseHandler(context);

        name = view.findViewById(R.id.name_input_field);
        email = view.findViewById(R.id.email_input_field);
        password = view.findViewById(R.id.password_input_field);
        password2 = view.findViewById(R.id.password_input_field2);
        mobile_no = view.findViewById(R.id.mobile_input_field);
        location = view.findViewById(R.id.location_input_field);
        class_no = view.findViewById(R.id.class_input_dropdown);
        signupButton = view.findViewById(R.id.signup_submit_button);
        standards = new ArrayList<>();
        try{
            ArrayList<Standard> standardList = (ArrayList<Standard>) getArguments().getSerializable("standardList");
            String classes[] = new String[standardList.size()];
            for(int i =0; i< standardList.size(); i++){
                Standard std  = standardList.get(i);
                classes[i] = std.getClass_no();
                standards.add(std);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.signup_dropdown_menu_item, classes);
            class_no.setAdapter(adapter);
        }catch (Exception e){
            Log.d("SignupError",e.toString());
        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateField()){
                    if(!password.getText().toString().equals(password2.getText().toString())){
                        password2.setError("Password does not match.");
                    }else{
                        signup();
                    }
                }
            }
        });
    }

    private Boolean validateField(){
        //Add Validation here for email, password
        if(email.getText().toString().isEmpty()) {
            email.setError("Enter E-mail");
            return false;
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("Enter Password");
            return false;
        }
        if(password2.getText().toString().isEmpty()) {
            password2.setError("Enter Password");
            return false;
        }
        if(name.getText().toString().isEmpty()) {
            name.setError("Enter name");
            return false;
        }
        if(mobile_no.getText().toString().isEmpty()) {
            mobile_no.setError("Enter Mobile Number");
            return false;
        }
        if(class_no.getText().toString().isEmpty()) {
            class_no.setError("Enter Class");
            return false;
        }
        if(location.getText().toString().isEmpty()) {
            location.setError("Enter Address");
            return false;
        }
        return true;
    }

    private void signup(){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password",password.getText().toString());
            jsonObject.put("mobile_no",mobile_no.getText().toString());
            jsonObject.put("location",location.getText().toString());
            jsonObject.put("class_no",getClassID(class_no.getText().toString()));
        }catch(JSONException e){
            Toast.makeText(context, "Server Error. Please restart the app.", Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.REGISTER_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Signup Successful.")
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onerror", error.toString());
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private String getClassID(String class_no){
        for(Standard std: standards){
            if(std.getClass() != null && std.getClass_no().contains(class_no)){
                return std.getPrimary_key();
            }
        }
        return "none";
    }
}
