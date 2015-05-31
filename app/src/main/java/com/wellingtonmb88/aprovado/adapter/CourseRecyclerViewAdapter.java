package com.wellingtonmb88.aprovado.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.Course;

import java.util.List;

public class CourseRecyclerViewAdapter  extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

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
    public void onBindViewHolder(final ViewHolder recyclerView, int position) {

        ViewHolder holder = (ViewHolder)recyclerView;

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

        holder.mTextViewCourseApproved.setVisibility(View.GONE);
        holder.mImageViewCourse.setVisibility(View.GONE);

        String MINUS_ONE = "-1.0";
        if(!MINUS_ONE.equals(mb1)){
            holder.mTextViewCourseMB1.setText(mb1);
        }if(!MINUS_ONE.equals(mb2)){
            holder.mTextViewCourseMB2.setText(mb2);
        }if(!MINUS_ONE.equals(mf)){
            holder.mTextViewCourseMF.setText(mf);
            holder.mTextViewCourseApproved.setVisibility(View.VISIBLE);
            holder.mImageViewCourse.setVisibility(View.VISIBLE);
        }

        holder.mTextViewCourseName.setText(mCourses.get(position).name);
        holder.mTextViewCourseProfessor.setText(mCourses.get(position).professor);

        holder.mTextViewCourseMB1.setTextColor(mContext.getResources().getColor(R.color.ColorPrimary));
        holder.mTextViewCourseMB2.setTextColor(mContext.getResources().getColor(R.color.ColorPrimary));
        holder.mTextViewCourseMF.setTextColor(mContext.getResources().getColor(R.color.ColorPrimary));

        if(mediaB1 < 5){
            holder.mTextViewCourseMB1.setTextColor(mContext.getResources().getColor(R.color.red_dark));
        }
        if(mediaB2 < 5){
            holder.mTextViewCourseMB2.setTextColor(mContext.getResources().getColor(R.color.red_dark));
        }
        if(mediaFinal < 5){
            holder.mTextViewCourseMF.setTextColor(mContext.getResources().getColor(R.color.red_dark));

            holder.mImageViewCourse.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.unapproved));

            holder.mTextViewCourseApproved.setText(mContext.getResources().getString(R.string.card_item_label_unapproved));
            holder.mTextViewCourseApproved.setBackgroundColor(mContext.getResources().getColor(R.color.red_dark));
        }else{

            holder.mImageViewCourse.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.approved));

            holder.mTextViewCourseApproved.setText(mContext.getResources().getString(R.string.card_item_label_approved));
            holder.mTextViewCourseApproved.setBackgroundColor(mContext.getResources().getColor(R.color.ColorPrimary));
        }
    }


    @Override
    public long getHeaderId(int position) {
        return mCourses.get(position).semester;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        String semester = mContext.getResources().getStringArray(R.array.semester_array)[mCourses.get(position).semester];
        textView.setText(semester);
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
        private TextView mTextViewCourseApproved;
        private ImageView mImageViewCourse;

        public ViewHolder(View view) {
            super(view);
            mTextViewCourseName = (TextView) view.findViewById(R.id.txtName);
            mTextViewCourseProfessor = (TextView) view.findViewById(R.id.txtProfessor);
            mTextViewCourseMB1 = (TextView) view.findViewById(R.id.txtMB1);
            mTextViewCourseMB2 = (TextView) view.findViewById(R.id.txtMB2);
            mTextViewCourseMF = (TextView) view.findViewById(R.id.txtMF);
            mTextViewCourseApproved = (TextView) view.findViewById(R.id.textView_card_item_approved);
            mImageViewCourse = (ImageView) view.findViewById(R.id.imageView_card_item);
        }
    }
}
