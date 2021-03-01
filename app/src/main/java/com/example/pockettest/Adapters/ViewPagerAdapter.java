/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pockettest.Fragments.OnGoingFragment;
import com.example.pockettest.Fragments.UpComingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String slug;
    private static final int NUM_ITEMS = 2;

    public ViewPagerAdapter(@NonNull FragmentManager fm, String slug) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.slug =  slug;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putString("slug", slug);
        switch (position){
            case 0:
                OnGoingFragment onGoingFragment = new OnGoingFragment();
                onGoingFragment.setArguments(args);
                return onGoingFragment;
            case 1:
                UpComingFragment upComingFragment = new UpComingFragment();
                upComingFragment.setArguments(args);
                return upComingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
