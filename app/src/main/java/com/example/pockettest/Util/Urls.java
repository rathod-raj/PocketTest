/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Util;

public class Urls {
    public static final String BASE_URL = "{URL}/user/";
    public static final String LOGIN_URL = "login";
    public static final String REGISTER_URL = "register";
    public static final String LOGOUT_URL = "logout";
    public static final String GET_CLASS = "getclass";
    public static final String GET_SUBJECT = "getsubject";
    public static final String UPDATE_USER_URL = "update";
    public static final String GET_QUIZ = "/getquiz";
    public static final String MY_QUIZ = "/myquiz";
    public static final String GET_QUIZ_DETAILS = "quiz/";
    public static final String SUBMIT_QUIZ = "/submit/";
    public static final String QUIZ = "quiz/";

    public static final String YOUTUBE_API_KEY = "YOUTUBE API KEY";
    public static final String YOUTUBE_API_VIDEO_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=";
    public static final String YOUTUBE_API_PLAYLIST_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId={CHANNEL ID}&type=playlist&order=date&maxResults=10&key=";
}
