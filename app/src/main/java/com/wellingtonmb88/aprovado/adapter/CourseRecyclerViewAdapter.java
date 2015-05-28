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

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView mTextViewCourseName;
        private TextView mTextViewCourseProfessor;
        private TextView mTextViewCourseMB1;
        private TextView mTextViewCourseMB2;
        private TextView mTextViewCourseMF;
        private View mButtonCourseEdit;
        private View mButtonCourseDelete;
        private RecyclerViewCallBack mListener;
        private Course mCourse;

        public ViewHolder(View view,  RecyclerViewCallBack listener) {
            super(view); 
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



    }

    public interface RecyclerViewCallBack{
        void Operation(String operation, Course Course);
    }
}
