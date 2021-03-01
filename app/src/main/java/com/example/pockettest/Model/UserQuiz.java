/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserQuiz implements Serializable {
    private String userquiz_id;
    private  String user_score;
    private LocalDateTime date_attempted;

    public UserQuiz() {
    }

    public String getUserquiz_id() {
        return userquiz_id;
    }

    public void setUserquiz_id(String userquiz_id) {
        this.userquiz_id = userquiz_id;
    }

    public String getUser_score() {
        return user_score;
    }

    public void setUser_score(String user_score) {
        this.user_score = user_score;
    }

    public LocalDateTime getDate_attempted() {
        return date_attempted;
    }

    public void setDate_attempted(LocalDateTime date_attempted) {
        this.date_attempted = date_attempted;
    }
}
