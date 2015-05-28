package com.wellingtonmb88.aprovado.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.entity.Course;

import java.util.List;



public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder> {

    private static final String DELETAR = "Deletar";
    private final String EDITAR = "Editar";
    private Context mContext;
    private RecyclerViewCallBack mListener;
    private List<Course> mCourses;

    public CourseRecyclerViewAdapter(Context context, RecyclerViewCallBack listener, List<Course> Course) {
        mContext = context;
        mListener = listener;
        mCourses = Course;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_course, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mCourse = mCourses.get(position);
        holder.mTextViewCourseName.setText(mCourses.get(position).name);
        holder.mTextViewCourseProfessor.setText("Professor: "+mCourses.get(position).professor);
        holder.mTextViewCourseMB1.setText("MB1: "+String.valueOf(mCourses.get(position).mediaB1));
        holder.mTextViewCourseMB2.setText("MB2: "+String.valueOf(mCourses.get(position).mediaB2));
        holder.mTextViewCourseMF.setText("MF: "+String.valueOf(mCourses.get(position).mediaFinal));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewCourseName;
        private TextView mTextViewCourseProfessor;
        private TextView mTextViewCourseMB1;
        private TextView mTextViewCourseMB2;
        private TextView mTextViewCourseMF;
        private View mButtonCourseEdit;
        private View mButtonCourseDelete;
        private RecyclerViewCallBack mListener;
        private Course mCourse;

        private View yourCustomView;


        private int originalHeight = 0;
        private boolean mIsViewExpanded = false;

        public ViewHolder(View view,  RecyclerViewCallBack listener) {
            super(view);
            view.setOnClickListener(this);
            mListener = listener;
            mTextViewCourseName = (TextView) view.findViewById(R.id.txtName);
            mTextViewCourseProfessor = (TextView) view.findViewById(R.id.txtProfessor);
            mTextViewCourseMB1 = (TextView) view.findViewById(R.id.txtMB1);
            mTextViewCourseMB2 = (TextView) view.findViewById(R.id.txtMB2);
            mTextViewCourseMF = (TextView) view.findViewById(R.id.txtMF);
            mButtonCourseDelete = (View) view.findViewById(R.id.btnDelete);
            mButtonCourseEdit = (View) view.findViewById(R.id.btnEdit);
            mButtonCourseDelete.setOnClickListener(deleleListner);
            mButtonCourseEdit.setOnClickListener(editListner);

            // If isViewExpanded == false then set the visibility
            // of whatever will be in the expanded to GONE

            if (mIsViewExpanded == false) {
                // Set Views to View.GONE and .setEnabled(false)
               // yourCustomView.setVisibility(View.GONE);
               // yourCustomView.setEnabled(false);
            }
        }

        private View.OnClickListener deleleListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        private View.OnClickListener editListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.Operation("", mCourse);
            }
        };

        @Override
        public void onClick(final View view) {
            // If the originalHeight is 0 then find the height of the View being used
            // This would be the height of the cardview
            if (originalHeight == 0) {
                originalHeight = view.getHeight();
            }

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;
            if (!mIsViewExpanded) {
               // yourCustomView.setVisibility(View.VISIBLE);
               // yourCustomView.setEnabled(true);

                mIsViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 2.0)); // These values in this method can be changed to expand however much you like
            } else {
                mIsViewExpanded = false;
                valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 2.0), originalHeight);

                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                a.setDuration(200);
                // Set a listener to the animation and configure onAnimationEnd
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                       // yourCustomView.setVisibility(View.INVISIBLE);
                       // yourCustomView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                // Set the animation on the custom view
                //yourCustomView.startAnimation(a);
            }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });


            valueAnimator.start();

        }

    }

    public interface RecyclerViewCallBack{
        void Operation(String operation, Course Course);
    }
}
