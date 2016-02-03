package com.wellingtonmb88.aprovado.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.Course;
import com.wellingtonmb88.aprovado.listener.interfaces.ItemTouchHelperAdapter;
import com.wellingtonmb88.aprovado.presenter.interfaces.CourseListFragmentPresenter;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, ItemTouchHelperAdapter {


    private final CourseListFragmentPresenter mCourseListFragmentPresenter;
    private final WeakReference<Context> mContext;
    private final List<Course> mCourses;
    private boolean mShouldAnimateView = true;

    public CourseRecyclerViewAdapter(CourseListFragmentPresenter courseListFragmentPresenter,
                                     Context context, List<Course> courseList) {
        mCourseListFragmentPresenter = courseListFragmentPresenter;
        mContext = new WeakReference<>(context);
        mCourses = courseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_course, parent, false);
        return new ViewHolder(mCourseListFragmentPresenter, v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Course course = mCourses.get(position);
        float mediaB1 = course.getMediaB1();
        float mediaB2 = course.getMediaB2();
        float mediaFinal = course.getMediaFinal();

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

        holder.mTextViewCourseName.setText(mCourses.get(position).getName());
        holder.mTextViewCourseProfessor.setText(mCourses.get(position).getProfessor());

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
                holder.mTextViewCourseApproved.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
//                holder.mTextViewCourseApproved.setBackgroundColor(ContextCompat.getColor(context, R.color.red_dark));
            } else {

                holder.mImageViewCourse.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.approved, null));

                holder.mTextViewCourseApproved.setText(context.getResources().getString(R.string.card_item_label_approved));
//                holder.mTextViewCourseApproved.setBackgroundColor(ContextCompat.getColor(context, R.color.ColorPrimary));
                holder.mTextViewCourseApproved.setTextColor(ContextCompat.getColor(context, R.color.ColorPrimary));
            }
        }

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView);

        if (position == mCourses.size() - 1 && !mShouldAnimateView) {
            mShouldAnimateView = true;
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate) {
        Context context = mContext.get();
        if (context != null && mShouldAnimateView) {
            // If the bound view wasn't previously displayed on screen, it's animated
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom);
            viewToAnimate.startAnimation(animation);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.itemView.clearAnimation();
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public long getHeaderId(int position) {
        if (position < 0) {
            position = 0;
        }
        return mCourses.get(position).getSemester();
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
            TextView textView = (TextView) holder.itemView;
            String semester = context.getResources()
                    .getStringArray(R.array.semester_array)[mCourses.get(position).getSemester()];
            textView.setText(semester);
        }
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mCourses, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mCourses, i, i - 1);
//            }
//        }
//        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Context context = mContext.get();
        if (context != null) {
            mShouldAnimateView = false;
            mCourseListFragmentPresenter.onDismissRecyclerViewItem(context, position);
            notifyItemRemoved(position);

//            mShouldAnimateView = true;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        private final CourseListFragmentPresenter mCourseListFragmentPresenter;

        public ViewHolder(CourseListFragmentPresenter courseListFragmentPresenter, View view) {
            super(view);
            ButterKnife.bind(this, view);
            mCourseListFragmentPresenter = courseListFragmentPresenter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCourseListFragmentPresenter.onOpenCourseDetails(getAdapterPosition());
        }
    }
}
