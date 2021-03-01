/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Activites.MainActivity;
import com.example.pockettest.Activites.MainTest;
import com.example.pockettest.Adapters.QuizDetailAdapter;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.Model.Answer;
import com.example.pockettest.Model.Questions;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.R;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizDetails extends BottomSheetDialogFragment {
    private Context context;
    private TextView quizTitle;
    private TextView quizDesc;
    private TextView quizTotalMarks;
    private TextView marksScored;
    private TextView quizLoading;
    private List<Questions> questionsList;
    private RecyclerView recyclerView;
    private QuizDetailAdapter quizDetailAdapter;
    private Quiz quiz;
    private Skeleton skeleton;
    public QuizDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        quiz = (Quiz) getArguments().getSerializable("quiz");
        quizTitle = view.findViewById(R.id.quiz_detail_title);
        quizDesc  = view.findViewById(R.id.quiz_detail_desc);
        quizTotalMarks = view.findViewById(R.id.quiz_detail_total_marks);
        quizLoading = view.findViewById(R.id.quiz_detail_loading);
        marksScored = view.findViewById(R.id.quiz_detail_marks_scored);
        quizTitle.setText(quiz.getTitle());
        quizDesc.setText(quiz.getDescription());
        quizTotalMarks.setText("Total Marks: "+ quiz.getTotal_marks());

        recyclerView = view.findViewById(R.id.quiz_detail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        questionsList = new ArrayList<>();
        getQuizDetails();
        quizDetailAdapter = new QuizDetailAdapter(questionsList);
        recyclerView.setAdapter(quizDetailAdapter);
    }

    private void getQuizDetails(){
        questionsList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + Urls.GET_QUIZ_DETAILS + quiz.getPrimary_key() + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                quizLoading.setVisibility(View.GONE);
                try {
                    JSONObject parentObj = new JSONObject(response);
                    JSONObject quiz = parentObj.getJSONObject("quiz");
                    Object userquiz = quiz.get("userquiz_set");
                    if(userquiz.toString() != "false"){
                        JSONObject user_quiz = (JSONObject) userquiz;
                        marksScored.setText("Marks Scored : "+user_quiz.getString("score"));
                        marksScored.setVisibility(View.VISIBLE);
                    }
                    JSONArray questions = quiz.getJSONArray("questions");
                    for (int i = 0; i < questions.length(); i++) {
                        JSONObject questionObj = questions.getJSONObject(i);
                        Questions question = new Questions();
                        String title = (i + 1) + ". " + questionObj.getString("content");
                        question.setTitle(title);
                        question.setQuestion_id(questionObj.getString("id"));
                        question.setMarks(questionObj.getString("marks"));
                        question.setQuiz_id(questionObj.getString("quiz"));
                        JSONArray answerArrayObj = questionObj.getJSONArray("answers");

                        List<Answer> answersList = new ArrayList<>();
                        for (int j = 0; j < answerArrayObj.length(); j++) {
                            JSONObject answerObj = answerArrayObj.getJSONObject(j);
                            Answer answer = new Answer();
                            answer.setAnswer_id(answerObj.getString("id"));
                            answer.setContent(answerObj.getString("content"));
                            answer.setIs_correct(answerObj.getBoolean("is_correct"));
                            answer.setQuestion_id(answerObj.getString("question"));
                            answersList.add(answer);
                        }
                        question.setAnswer_1(answersList.get(0));
                        question.setAnswer_2(answersList.get(1));
                        question.setAnswer_3(answersList.get(2));
                        question.setAnswer_4(answersList.get(3));
                        questionsList.add(question);
                    }
                    quizDetailAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(context).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
