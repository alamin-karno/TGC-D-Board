package com.teamblank.tgcdboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamblank.tgcdboard.R;
import com.teamblank.tgcdboard.activity.DetailsNoticeActivity;
import com.teamblank.tgcdboard.model.Teacher;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.ViewHolder> {

    Context context;
    List<Teacher> teachers;

    public TeacherListAdapter(Context context, List<Teacher> teachers) {
        this.context = context;
        this.teachers = teachers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_teacher_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Teacher teacher = teachers.get(position);

        holder.teacherNameTV.setText(teacher.gettName());
        holder.teacherDesignationTV.setText(teacher.gettDesignation());
        Glide.with(context).load(teacher.gettImage()).into(holder.teacherImage);

        holder.teacherMailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = teacher.gettEmail().toString();

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Type your Email Subject here");
                intent.putExtra(Intent.EXTRA_TEXT, "Type your Email here");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        holder.teacherCallIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+teacher.gettPhone()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView teacherImage;
        private TextView teacherNameTV,teacherDesignationTV;
        private ImageView teacherMailIV,teacherCallIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherImage = itemView.findViewById(R.id.teacher_profile_image);
            teacherNameTV = itemView.findViewById(R.id.teacherNameTV);
            teacherDesignationTV = itemView.findViewById(R.id.teacherDesignationTV);
            teacherMailIV = itemView.findViewById(R.id.teacherMailTV);
            teacherCallIV = itemView.findViewById(R.id.teacherCallIV);
        }
    }
}
