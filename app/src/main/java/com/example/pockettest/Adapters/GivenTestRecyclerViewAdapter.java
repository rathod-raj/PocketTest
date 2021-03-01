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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Activites.AllTestListActivity;
import com.example.pockettest.Model.Subjects;
import com.example.pockettest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GivenTestRecyclerViewAdapter extends RecyclerView.Adapter<GivenTestRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Subjects> subjectList;

    public GivenTestRecyclerViewAdapter(Context context, List<Subjects> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public GivenTestRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        view=layoutInflater.inflate(R.layout.fragment_main_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GivenTestRecyclerViewAdapter.ViewHolder holder, int position) {
        Subjects subject = subjectList.get(position);
        holder.subject_name.setText(subject.getName());
        Picasso.get()
                .load(R.drawable.exam_img)
                .into(holder.subject_thumbnail);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Context ctx;
        TextView subject_name;
        ImageView subject_thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ctx = context;
            subject_name=(TextView) itemView.findViewById(R.id.subject_title);
            subject_thumbnail=(ImageView) itemView.findViewById(R.id.subject_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Subjects subject = subjectList.get(getAdapterPosition());
            Intent intent = new Intent(ctx, AllTestListActivity.class);
            intent.putExtra("name", subject.getName());
            intent.putExtra("slug",subject.getSlug());
            intent.putExtra("isGiven", true);
            ctx.startActivity(intent);
        }
    }
}
