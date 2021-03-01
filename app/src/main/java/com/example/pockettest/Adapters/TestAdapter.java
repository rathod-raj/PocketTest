/*
 * Copyright (c) 2020. Made by Vivek Surya and Raj Rathod. All Rights Reserved.
 * Last Modified 15/11/20 12:39 PM
 */

package com.example.pockettest.Adapters;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<Questions> questions_list;
    private Map<String, String> answers = new HashMap<>();

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> ansers) {
        this.answers = answers;
    }

    public TestAdapter(List<Questions> questions_list){
        this.questions_list = questions_list;

    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_test,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        Questions question = questions_list.get(position);
        holder.question.setText(question.getTitle());
        holder.answer_1.setText(question.getAnswer_1().getContent());
        holder.answer_1.setTag(question.getAnswer_1().getAnswer_id());

        holder.answer_2.setText(question.getAnswer_2().getContent());
        holder.answer_2.setTag(question.getAnswer_2().getAnswer_id());

        holder.answer_3.setText(question.getAnswer_3().getContent());
        holder.answer_3.setTag(question.getAnswer_3().getAnswer_id());

        holder.answer_4.setText(question.getAnswer_4().getContent());
        holder.answer_4.setTag(question.getAnswer_4().getAnswer_id());

        holder.radioGroup.setTag(question.getQuestion_id());
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                answers.put(group.getTag().toString(), radioButton.getTag().toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return questions_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView question;
        private RadioGroup radioGroup;
        private RadioButton answer_1;
        private RadioButton answer_2;
        private RadioButton answer_3;
        private RadioButton answer_4;

        public ViewHolder(@NonNull final View itemView) {
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
