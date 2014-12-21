package com.rdevblog.rtodo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.rdevblog.rtodo.MainActivity;
import com.rdevblog.rtodo.R;
import com.rdevblog.rtodo.objects.Task;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by TomSingleton on 20/12/14.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private RealmResults<Task> realmResults;
    private Activity context;


    public TaskRecyclerAdapter(RealmResults<Task> realmResults, Activity context) {
        this.realmResults = realmResults;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final Task task = realmResults.get(realmResults.size() - 1 - position);

        holder.bindTask(task);

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new TaskViewHolder(itemHolder, context, new TaskViewHolder.OnTaskViewHolder() {
            @Override
            public void onDeleteTask() {
                notifyDataSetChanged();
            }
        });
    }


    public final static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{


        TextView taskStringTextView;
        CheckBox taskCompletedCheckBox;

        Task task;

        OnTaskViewHolder mListener;

        Activity mContext;

        public TaskViewHolder(View itemView, Activity context, OnTaskViewHolder listener) {
            super(itemView);

            mListener = listener;
            mContext = context;

            taskStringTextView = (TextView) itemView.findViewById(R.id.task_string_textView);
            taskCompletedCheckBox = (CheckBox) itemView.findViewById(R.id.task_completed_checkbox);

            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);


        }

        public void bindTask(Task task){
            this.task = task;
            taskStringTextView.setText(task.getTaskString());
            taskStringTextView.setPaintFlags(taskStringTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            taskStringTextView.setTextColor(Color.parseColor("#212121"));
            taskCompletedCheckBox.setChecked(task.isTaskCompleted());

            if (task.isTaskCompleted()){
                taskStringTextView.setPaintFlags(taskStringTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                taskStringTextView.setTextColor(Color.parseColor("#727272"));

            }
        }

        @Override
        public void onClick(View v) {
//            Realm realm = Realm.getInstance(v.getContext());
//            realm.beginTransaction();
//            task.setTaskCompleted(!task.isTaskCompleted());
//            realm.commitTransaction();
//            realm.close();


        }

        private long touchTime = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    long touchDuration = System.currentTimeMillis() - touchTime;
                    Log.d("touch duration", String.valueOf(touchDuration));
                    if (touchDuration>1500){

                        Realm realm = Realm.getInstance(v.getContext());
                        realm.beginTransaction();
                        final String taskText = task.getTaskString();
                        final boolean taskCompleted = task.isTaskCompleted();
                        task.removeFromRealm();
                        realm.commitTransaction();
                        realm.close();

                        mListener.onDeleteTask();

                        SnackbarManager.show(Snackbar.with(mContext)
                                .text("Task Deleted!")
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .actionLabel("Undo")
                                .actionColor(Color.YELLOW)
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        Realm realm = Realm.getInstance(mContext);
                                        realm.beginTransaction();
                                        Task oldTask = realm.createObject(Task.class);
                                        oldTask.setTaskString(taskText);
                                        oldTask.setTaskCompleted(taskCompleted);
                                        realm.commitTransaction();
                                        mListener.onDeleteTask();
                                    }
                                }), mContext);



                    }
                    else {
                        Realm realm = Realm.getInstance(v.getContext());
                        realm.beginTransaction();
                        task.setTaskCompleted(!task.isTaskCompleted());
                        realm.commitTransaction();
                        realm.close();
                        this.bindTask(task);
                    }
                    break;
            }
            return false;
        }

        public static interface OnTaskViewHolder{
            public void onDeleteTask();
        }

    }



}

