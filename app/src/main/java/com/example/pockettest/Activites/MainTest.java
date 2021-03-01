/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pockettest.Adapters.TestAdapter;
import com.example.pockettest.DataBase.SharedPrefManager;
import com.example.pockettest.Model.Answer;
import com.example.pockettest.Model.Questions;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.Model.UserQuiz;
import com.example.pockettest.Util.Urls;
import com.example.pockettest.Util.VolleySingleton;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pockettest.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainTest extends AppCompatActivity {

    private Long backPressedTime;
    private Skeleton skeleton;
    private Bundle bundle;
    private TextView marks;
    private RecyclerView recyclerView;
    private Button submitButton;
    private Quiz quiz;
    private List<Questions> questions_list;
    private TestAdapter testAdapter;
    private Map<String, String> userAnswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        submitButton = findViewById(R.id.main_test_submit);
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        bundle = getIntent().getExtras();
        quiz = (Quiz) bundle.getSerializable("quiz");
        toolbar.setTitle(quiz.getTitle());
        questions_list = new ArrayList<>();

        submitButton = findViewById(R.id.main_test_submit);
        marks = findViewById(R.id.main_test_marks);
        marks.setText("Marks: "+ quiz.getTotal_marks());

        recyclerView = findViewById(R.id.main_test_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        skeleton = SkeletonLayoutUtils.applySkeleton(recyclerView, R.layout.skeleton_layout_main_test, 3);
        skeleton.showSkeleton();
        skeleton.setShimmerDurationInMillis(1000);
        questions_list = getQuestions();
        testAdapter = new TestAdapter(questions_list);
        userAnswers = testAdapter.getAnswers();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswers();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to Submit and Exit ?").
        setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitAnswers();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();


    }

    private List<Questions> getQuestions() {
        questions_list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.BASE_URL + Urls.GET_QUIZ_DETAILS + quiz.getPrimary_key() + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                skeleton.showOriginal();
                try {
                    JSONObject parentObj = new JSONObject(response);
                    JSONObject quiz = parentObj.getJSONObject("quiz");
                    Object userquiz = quiz.get("userquiz_set");
                    Log.d("maintest", userquiz.toString());
                    if(userquiz.toString() != "false"){
                        AlertDialog alertDialog = new AlertDialog.Builder(MainTest.this).create();
                        alertDialog.setTitle("Oops!");
                        alertDialog.setMessage("You already gave this Quiz!");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainTest.this, MainActivity.class));
                                finish();
                            }
                        });
                        alertDialog.show();
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
                        questions_list.add(question);
                    }
                    recyclerView.setAdapter(testAdapter);
                    testAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("error :  ", e.getMessage());
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please contact Staff member.", Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", error.toString());
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please contact staff member.", Snackbar.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(MainTest.this).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(MainTest.this).addToRequestQueue(stringRequest);
        return questions_list;
    }

    private void submitAnswers(){

        final JSONObject jsonObject = new JSONObject();
        try{
            JSONArray obj = new JSONArray();
            Iterator iterator = userAnswers.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry pair = (Map.Entry) iterator.next();
                JSONObject answers = new JSONObject();
                try{
                    answers.put("question", pair.getKey().toString());
                    answers.put("answer", pair.getValue().toString());
                    obj.put(answers);
                }catch (JSONException e){
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please try again later", Snackbar.LENGTH_SHORT).show();
                }
            }
            Log.d("answers", obj.toString());
            jsonObject.put("answers", obj);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, Urls.BASE_URL + Urls.QUIZ + quiz.getPrimary_key() + Urls.SUBMIT_QUIZ, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                Log.d("response", obj.toString());
                try {
                    Quiz quiz = new Quiz();
                    quiz.setTitle(obj.getString("title"));
                    quiz.setDescription(obj.getString("description"));
                    quiz.setTotal_marks(obj.getString("total_marks"));
                    JSONObject userquiz =  obj.getJSONObject("userquiz_set");
                    UserQuiz userQuiz = new UserQuiz();
                    userQuiz.setUserquiz_id(userquiz.getString("id"));
                    userQuiz.setUser_score(userquiz.getString("score"));
                    userQuiz.setDate_attempted(LocalDateTime.parse((userquiz.getString("given_date"))));

                    Intent intent = new Intent(MainTest.this, ResultsActivity.class);
                    intent.putExtra("quiz", quiz);
                    intent.putExtra("userquiz", userQuiz);
                    startActivity(intent);
                    finish();
                }catch (JSONException e){
                    Log.d("JSONException", e.toString());
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please contact staff member.", Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", error.toString());
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Something went wrong. Please contact staff member", Snackbar.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String>  headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(MainTest.this).getToken());
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(MainTest.this).addToRequestQueue(jsonObjectRequest);
    }

}
