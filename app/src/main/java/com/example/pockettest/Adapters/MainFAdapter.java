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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pockettest.Activites.HomeActivity;
import com.example.pockettest.Model.Subjects;
import com.example.pockettest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainFAdapter extends RecyclerView.Adapter<MainFAdapter.MainviewHolder> {
   private Context ctx;
   private List<Subjects> subjectsList;

    public MainFAdapter(Context ctx, List<Subjects> subjectsList) {
        this.ctx = ctx;
        this.subjectsList = subjectsList;
    }

    @NonNull
    @Override
    public MainviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater= LayoutInflater.from(ctx);
        view=layoutInflater.inflate(R.layout.fragment_main_items,parent,false);
        return new MainviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainviewHolder holder, int position) {
       Subjects subject = subjectsList.get(position);
       holder.subject_name.setText(subject.getName());
        Picasso.get()
                .load(R.drawable.exam_img)
                .into(holder.subject_thumbnail);
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public class MainviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subject_name;
        ImageView subject_thumbnail;
        CardView mainf_cardview;
        Context context;
        public MainviewHolder(@NonNull View itemView) {
            super(itemView);
            context = ctx;
            subject_name=(TextView) itemView.findViewById(R.id.subject_title);
            subject_thumbnail=(ImageView) itemView.findViewById(R.id.subject_image);
            mainf_cardview=(CardView) itemView.findViewById(R.id.mainf_cardview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, HomeActivity.class);
            Subjects subject = subjectsList.get(getAdapterPosition());
            intent.putExtra("slug",  subject.getSlug());
            intent.putExtra("name",  subject.getName());
            context.startActivity(intent);
        }
    }
}
