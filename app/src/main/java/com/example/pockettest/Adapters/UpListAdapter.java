/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Activites.MainActivity;
import com.example.pockettest.Activites.StartTest;
import com.example.pockettest.Model.Quiz;
import com.example.pockettest.R;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class UpListAdapter extends RecyclerView.Adapter<UpListAdapter.UpViewHolder> {
    private List<Quiz> quiz_list;
    private Context ctx;
    public UpListAdapter(List<Quiz> quiz_list, Context ctx)
    {
        this.ctx = ctx;
        this.quiz_list=quiz_list;
    }

    @NonNull
    @Override
    public UpListAdapter.UpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list, parent, false);
        return new UpViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UpListAdapter.UpViewHolder holder, int position) {
        Quiz quiz = quiz_list.get(position);
        holder.title.setText(quiz.getTitle());
        holder.desc.setText(quiz.getDescription());
        holder.startTime.setText(quiz.getPublish_date().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        holder.endTime.setText(quiz.getEnd_time().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
    }

    @Override
    public int getItemCount()
    {
        return quiz_list.size();
    }

    public class UpViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private TextView title;
        private TextView desc;
        private TextView startTime;
        private TextView endTime;
        UpViewHolder(@NonNull View itemView) {
            super(itemView);
            context = ctx;
            title=itemView.findViewById(R.id.quizTitle);
            desc=itemView.findViewById(R.id.quizDescription);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
        }

    }
}
