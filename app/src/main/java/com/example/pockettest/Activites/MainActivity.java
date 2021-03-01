/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.pockettest.Fragments.AccountFragment;
import com.example.pockettest.Fragments.LecturesFragment;
import com.example.pockettest.Fragments.MainFragment;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout main_frame;
    private MainFragment mainFragment;
    private LecturesFragment lecturesFragment;
    private AccountFragment accountFragment;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Quiz> catlist;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.nav_home:
                    setFragment(new MainFragment());
                    return true;

                case R.id.nav_lecture:
                    setFragment(new LecturesFragment());
                    return true;

                case R.id.nav_account:
                    setFragment(new AccountFragment());
                    return true;
            }

            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        main_frame = findViewById(R.id.main_frame);
        lecturesFragment = new LecturesFragment();
        accountFragment = new AccountFragment();
        mainFragment=new MainFragment();
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        boolean flag = false;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("Edit_AccountF")) {
            flag = extras.getBoolean("Edit_AccountF");


        }

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        if (flag) {
            setFragment(accountFragment);
        } else {
            setFragment(mainFragment);
        }


        boolean start_test_flag = false;
        Bundle start_test_extras = getIntent().getExtras();
        if (extras != null && start_test_extras.containsKey("StartTest_HomeF")) {
            start_test_flag = extras.getBoolean("StartTest_HomeF");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        if (start_test_flag) {
            setFragment(mainFragment);
        } else {
            setFragment(mainFragment);
        }


        boolean result_home_flag = false;
        Bundle result_home_extras = getIntent().getExtras();
        if (extras != null && result_home_extras.containsKey("Result_HomeF")) {
            result_home_flag = result_home_extras.getBoolean("Result_HomeF");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        if (result_home_flag) {
            setFragment(mainFragment);
        } else {
            setFragment(mainFragment);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setFragment(new MainFragment());
                return true;

            case R.id.nav_lecture:
                setFragment(lecturesFragment);
                return true;

            case R.id.nav_account:
                setFragment(accountFragment);
                return true;
        }

        return false;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), fragment);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.nav_home != selectedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}

