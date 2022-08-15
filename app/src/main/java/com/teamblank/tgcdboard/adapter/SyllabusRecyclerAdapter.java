package com.teamblank.tgcdboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.activity.WebViewActivity;
import com.teamblank.tgcdboard.model.Syllabus;

import java.util.List;

public class SyllabusRecyclerAdapter extends RecyclerView.Adapter<SyllabusRecyclerAdapter.ViewHolder> {

    List<Syllabus> syllabuss;
    Context context;

    public static String URL = "URL";
    public static String TITLE = "Title";

    public SyllabusRecyclerAdapter(List<Syllabus> syllabuss, Context context) {
        this.syllabuss = syllabuss;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_notice,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Syllabus syllabus = syllabuss.get(position);

        holder.title.setText(syllabus.getsTitle());
        holder.syllabusTitle.setText(syllabus.getsDepartment());
        holder.syllabusDate.setText(syllabus.getsDate());
        holder.syllabusTime.setText(syllabus.getsTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(URL,syllabus.getSPDFLink());
                intent.putExtra(TITLE,syllabus.getsTitle());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return syllabuss.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,syllabusTitle,syllabusDate,syllabusTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            syllabusTitle = itemView.findViewById(R.id.noticeTitleTV);
            syllabusDate = itemView.findViewById(R.id.noticeDateTV);
            syllabusTime = itemView.findViewById(R.id.noticeTimeTV);
        }
    }
}
