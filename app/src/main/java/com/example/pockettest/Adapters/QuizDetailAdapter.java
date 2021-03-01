/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Model.Questions;
import com.example.pockettest.R;

import java.util.List;

public class QuizDetailAdapter extends RecyclerView.Adapter<QuizDetailAdapter.ViewHolder> {
    private List<Questions> questions_list;

    public QuizDetailAdapter(List<Questions> questionsList) {
        this.questions_list = questionsList;
    }

    @NonNull
    @Override
    public QuizDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_test,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizDetailAdapter.ViewHolder holder, int position) {
        Questions question = questions_list.get(position);
        holder.question.setText(question.getTitle());
        holder.answer_1.setText(question.getAnswer_1().getContent());
        if(question.getAnswer_1().isIs_correct()){
            holder.answer_1.toggle();
        }else{
            holder.answer_1.setEnabled(false);
            holder.answer_1.setTextColor(Color.BLACK);
        }

        holder.answer_2.setText(question.getAnswer_2().getContent());
        if(question.getAnswer_2().isIs_correct()){
            holder.answer_2.toggle();
        }else{
            holder.answer_2.setEnabled(false);
            holder.answer_2.setTextColor(Color.BLACK);
        }

        holder.answer_3.setText(question.getAnswer_3().getContent());
        if(question.getAnswer_3().isIs_correct()){
            holder.answer_3.toggle();
        }else{
            holder.answer_3.setEnabled(false);
            holder.answer_3.setTextColor(Color.BLACK);
        }

        holder.answer_4.setText(question.getAnswer_4().getContent());
        if(question.getAnswer_4().isIs_correct()){
            holder.answer_4.toggle();
        }else{
            holder.answer_4.setEnabled(false);
            holder.answer_4.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return questions_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private RadioGroup radioGroup;
        private RadioButton answer_1;
        private RadioButton answer_2;
        private RadioButton answer_3;
        private RadioButton answer_4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.test_question);
            answer_1 = itemView.findViewById(R.id.test_answer_1);
            answer_2 = itemView.findViewById(R.id.test_answer_2);
            answer_3 = itemView.findViewById(R.id.test_answer_3);
            answer_4 = itemView.findViewById(R.id.test_answer_4);
            radioGroup = itemView.findViewById(R.id.test_radio_group);
        }
    }
}
