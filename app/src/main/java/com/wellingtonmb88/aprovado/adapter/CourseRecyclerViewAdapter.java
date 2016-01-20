package com.wellingtonmb88.aprovado.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.Course;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> mContext;
    private WeakReference<List<Course>> mCourses;

    public CourseRecyclerViewAdapter(Context context, List<Course> Course) {
        mContext = new WeakReference<>(context);
        mCourses = new WeakReference<>(Course);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_course, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        List<Course> courseListRef = mCourses.get();
        if (courseListRef != null) {
            Course course = courseListRef.get(position);
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
            if (!MINUS_ONE.equals(mb1)) {
                holder.mTextViewCourseMB1.setText(mb1);
            }
            if (!MINUS_ONE.equals(mb2)) {
                holder.mTextViewCourseMB2.setText(mb2);
            }
            if (!MINUS_ONE.equals(mf)) {
                holder.mTextViewCourseMF.setText(mf);
                holder.mTextViewCourseApproved.setVisibility(View.VISIBLE);
                holder.mImageViewCourse.setVisibility(View.VISIBLE);
            }

            holder.mTextViewCourseName.setText(courseListRef.get(position).name);
            holder.mTextViewCourseProfessor.setText(courseListRef.get(position).professor);

            Context context = mContext.get();

            if (context != null) {

                holder.mTextViewCourseMB1.setTextColor(ContextCompat.getColor(context, R.color.ColorPrimary));
                holder.mTextViewCourseMB2.setTextColor(ContextCompat.getColor(context, R.color.ColorPrimary));
                holder.mTextViewCourseMF.setTextColor(ContextCompat.getColor(context, R.color.ColorPrimary));

                if (mediaB1 < 5) {
                    holder.mTextViewCourseMB1.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
                }
                if (mediaB2 < 5) {
                    holder.mTextViewCourseMB2.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
                }
                if (mediaFinal < 5) {
                    holder.mTextViewCourseMF.setTextColor(ContextCompat.getColor(context, R.color.red_dark));

                    holder.mImageViewCourse.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.unapproved, null));

                    holder.mTextViewCourseApproved.setText(context.getResources().getString(R.string.card_item_label_unapproved));
                    holder.mTextViewCourseApproved.setBackgroundColor(ContextCompat.getColor(context, R.color.red_dark));
                } else {

                    holder.mImageViewCourse.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.approved, null));

                    holder.mTextViewCourseApproved.setText(context.getResources().getString(R.string.card_item_label_approved));
                    holder.mTextViewCourseApproved.setBackgroundColor(ContextCompat.getColor(context, R.color.ColorPrimary));
                }
            }
        }
    }


    @Override
    public long getHeaderId(int position) {

        List<Course> courseListRef = mCourses.get();
        if (courseListRef != null) {
            return courseListRef.get(position).semester;
        }
        return -1;
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
        Context context = mContext.get();
        if (context != null) {
            List<Course> courseListRef = mCourses.get();
            if (courseListRef != null) {
                TextView textView = (TextView) holder.itemView;
                String semester = context.getResources()
                        .getStringArray(R.array.semester_array)[courseListRef.get(position).semester];
                textView.setText(semester);
            }
        }
    }

    @Override
    public int getItemCount() {
        List<Course> courseListRef = mCourses.get();
        if (courseListRef != null) {
            return courseListRef.size();
        }
        return -1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txtName)
        TextView mTextViewCourseName;
        @Bind(R.id.txtProfessor)
        TextView mTextViewCourseProfessor;
        @Bind(R.id.txtMB1)
        TextView mTextViewCourseMB1;
        @Bind(R.id.txtMB2)
        TextView mTextViewCourseMB2;
        @Bind(R.id.txtMF)
        TextView mTextViewCourseMF;
        @Bind(R.id.textView_card_item_approved)
        TextView mTextViewCourseApproved;
        @Bind(R.id.imageView_card_item)
        ImageView mImageViewCourse;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
