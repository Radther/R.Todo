package com.rdevblog.rtodo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
public class TaskListAdapter extends RealmBaseAdapter<Task> implements ListAdapter {

    private static class TaskViewHolder {
        TextView taskStringTextView;
        CheckBox taskCompletedCheckBox;
    }

    public TaskListAdapter(Context context, RealmResults<Task> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewHolder taskViewHolder;

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
        taskViewHolder.taskCompletedCheckBox.setChecked(task.isTaskCompleted());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empty for now
            }
        });

        return convertView;

    }


    public RealmResults<Task> getRealmResults(){
        return realmResults;
    }

}
