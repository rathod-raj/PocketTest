/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Standard implements Serializable {
    private String primary_key;
    private String class_no;

    public Standard() {
    }

    public String getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }

    public String getClass_no() {
        return class_no;
    }

    public void setClass_no(String class_no) {
        this.class_no = "Class "+class_no;
    }

}
