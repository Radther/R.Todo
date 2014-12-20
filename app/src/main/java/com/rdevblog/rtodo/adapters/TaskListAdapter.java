package com.rdevblog.rtodo.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.rdevblog.rtodo.R;
import com.rdevblog.rtodo.objects.Task;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by TomSingleton on 18/12/14.
 */
public class TaskListAdapter extends RealmBaseAdapter<Task> {

    private long touchTime = 0;
    private long touchDuration;

    private static class TaskViewHolder {
        TextView taskStringTextView;
        CheckBox taskCompletedCheckBox;
    }

    public TaskListAdapter(Context context, RealmResults<Task> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TaskViewHolder taskViewHolder;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.task_list_item, parent, false);

            taskViewHolder = new TaskViewHolder();

            taskViewHolder.taskStringTextView = (TextView) convertView.findViewById(R.id.task_string_textView);
            taskViewHolder.taskCompletedCheckBox = (CheckBox) convertView.findViewById(R.id.task_completed_checkbox);

            convertView.setTag(taskViewHolder);
        } else {
            taskViewHolder = (TaskViewHolder) convertView.getTag();
        }

        final Task task = realmResults.get(realmResults.size()-1-position);

        taskViewHolder.taskStringTextView.setText(task.getTaskString());
        taskViewHolder.taskStringTextView.setPaintFlags(taskViewHolder.taskStringTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        taskViewHolder.taskStringTextView.setTextColor(Color.parseColor("#212121"));
        taskViewHolder.taskCompletedCheckBox.setChecked(task.isTaskCompleted());
        taskViewHolder.taskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();
                task.setTaskCompleted(!task.isTaskCompleted());
                realm.commitTransaction();
                realm.close();
            }
        });
        if (task.isTaskCompleted()){
            taskViewHolder.taskStringTextView.setPaintFlags(taskViewHolder.taskStringTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskViewHolder.taskStringTextView.setTextColor(Color.parseColor("#727272"));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        touchDuration = System.currentTimeMillis() - touchTime;
                        Log.d("touch duration", String.valueOf(touchDuration));
                        if (touchDuration>1500){

                            Realm realm = Realm.getInstance(context);
                            realm.beginTransaction();
                            task.removeFromRealm();
                            realm.commitTransaction();
                            realm.close();
                        }
                        else {
                            Realm realm = Realm.getInstance(context);
                            realm.beginTransaction();
                            task.setTaskCompleted(!task.isTaskCompleted());
                            realm.commitTransaction();
                            realm.close();
                        }
                        break;
                }
                return false;
            }
        });

        return convertView;

    }

    public RealmResults<Task> getRealmResults(){
        return realmResults;
    }



}
