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
import com.teamblank.tgcdboard.activity.DetailsNoticeActivity;
import com.teamblank.tgcdboard.model.Notice;

import java.util.List;

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder> {

    Context context;
    List<Notice> notices;
    public static String NOTICE_ID = "Notice ID";


    public NoticeRecyclerAdapter(Context context, List<Notice> notices) {
        this.context = context;
        this.notices = notices;
    }

    @NonNull
    @Override
    public NoticeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_notice,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerAdapter.ViewHolder holder, int position) {

        final Notice notice = notices.get(position);

        holder.noticeTitleTV.setText(notice.getTitle());
        holder.noticeTimeTV.setText("Time: "+notice.getTime());
        holder.noticeDateTv.setText("Date: "+notice.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailsNoticeActivity.class);
                intent.putExtra(NOTICE_ID,notice.getnID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noticeTitleTV,noticeDateTv,noticeTimeTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitleTV = itemView.findViewById(R.id.noticeTitleTV);
            noticeDateTv = itemView.findViewById(R.id.noticeDateTV);
            noticeTimeTV = itemView.findViewById(R.id.noticeTimeTV);
        }
    }
}
