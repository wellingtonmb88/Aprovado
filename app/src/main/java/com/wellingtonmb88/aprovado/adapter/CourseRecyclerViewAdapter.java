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
        String teacher = mContext.getString(R.string.recyclrview_label_teacher);
        String mb1 = mContext.getString(R.string.recyclrview_label_mb1);
        String mb2 = mContext.getString(R.string.recyclrview_label_mb2);
        String mf = mContext.getString(R.string.recyclrview_label_mf);

        holder.mTextViewCourseName.setText(mCourses.get(position).name);
        holder.mTextViewCourseProfessor.setText(teacher+" "+mCourses.get(position).professor);
        holder.mTextViewCourseMB1.setText(mb1+" "+String.valueOf(mCourses.get(position).mediaB1));
        holder.mTextViewCourseMB2.setText(mb2+" "+String.valueOf(mCourses.get(position).mediaB2));
        holder.mTextViewCourseMF.setText(mf+" "+String.valueOf(mCourses.get(position).mediaFinal));
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
