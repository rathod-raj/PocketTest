/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Fragments.QuizDetails;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.R;


import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class AllTestListRecyclerViewAdapter extends RecyclerView.Adapter<AllTestListRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Quiz> quizList;

    public AllTestListRecyclerViewAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public AllTestListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTestListRecyclerViewAdapter.ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.title.setText(quiz.getTitle());
        holder.desc.setText(quiz.getDescription());
        holder.startTime.setText(quiz.getPublish_date().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        holder.endTime.setText(quiz.getEnd_time().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context ctx;
        private TextView title;
        private TextView desc;
        private TextView startTime;
        private TextView endTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ctx = context;
            title = itemView.findViewById(R.id.quizTitle);
            desc = itemView.findViewById(R.id.quizDescription);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Quiz quiz = quizList.get(getAdapterPosition());
            Bundle args = new Bundle();
            args.putSerializable("quiz",quiz);
            QuizDetails quizDetails = new QuizDetails();
            quizDetails.setArguments(args);
            quizDetails.show(((AppCompatActivity) ctx).getSupportFragmentManager(), "Login Modal Bottom Sheet");
        }
    }
}
