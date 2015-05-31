package com.wellingtonmb88.aprovado.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.Course;

import java.util.List;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Course> mCourses;

    public CourseRecyclerViewAdapter(Context context, List<Course> Course) {
        mContext = context;
        mCourses = Course;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_course, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Course course = mCourses.get(position);
        float mediaB1 = course.mediaB1;
        float mediaB2 = course.mediaB2;
        float mediaFinal = course.mediaFinal;

        String mb1 = String.valueOf(mediaB1);
        String mb2 = String.valueOf(mediaB2);
        String mf = String.valueOf(mediaFinal);

        holder.mTextViewCourseMB1.setText("");
        holder.mTextViewCourseMB2.setText("");
        holder.mTextViewCourseMF.setText("");

        String ZERO = "0.0";
        if(!ZERO.equals(mb1)){
            holder.mTextViewCourseMB1.setText(mb1);
        }if(!ZERO.equals(mb2)){
            holder.mTextViewCourseMB2.setText(mb2);
        }if(!ZERO.equals(mf)){
            holder.mTextViewCourseMF.setText(mf);
        }

        holder.mTextViewCourseName.setText(mCourses.get(position).name);
        holder.mTextViewCourseProfessor.setText(mCourses.get(position).professor);

        holder.mTextViewCourseMB1.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));
        holder.mTextViewCourseMB2.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));
        holder.mTextViewCourseMF.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));

        if(mediaB1 < 5){
            holder.mTextViewCourseMB1.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        }
        if(mediaB2 < 5){
            holder.mTextViewCourseMB2.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        }
        if(mediaFinal < 5){
            holder.mTextViewCourseMF.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        }
    }


    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView mTextViewCourseName;
        private TextView mTextViewCourseProfessor;
        private TextView mTextViewCourseMB1;
        private TextView mTextViewCourseMB2;
        private TextView mTextViewCourseMF;

        public ViewHolder(View view) {
            super(view);
            mTextViewCourseName = (TextView) view.findViewById(R.id.txtName);
            mTextViewCourseProfessor = (TextView) view.findViewById(R.id.txtProfessor);
            mTextViewCourseMB1 = (TextView) view.findViewById(R.id.txtMB1);
            mTextViewCourseMB2 = (TextView) view.findViewById(R.id.txtMB2);
            mTextViewCourseMF = (TextView) view.findViewById(R.id.txtMF);
        }
    }
}
