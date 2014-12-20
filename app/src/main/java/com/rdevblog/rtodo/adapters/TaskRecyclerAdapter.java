package com.rdevblog.rtodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rdevblog.rtodo.R;
import com.rdevblog.rtodo.objects.Task;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by TomSingleton on 20/12/14.
 */
public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private RealmResults<Task> realmResults;
    private Context context;


    public TaskRecyclerAdapter(RealmResults<Task> realmResults, Context context) {
        this.realmResults = realmResults;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final Task task = realmResults.get(realmResults.size() - 1 - position);

        holder.taskStringTextView.setText(task.getTaskString());
        holder.taskStringTextView.setPaintFlags(holder.taskStringTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.taskStringTextView.setTextColor(Color.parseColor("#212121"));
        holder.taskCompletedCheckBox.setChecked(task.isTaskCompleted());
        holder.taskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
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
            holder.taskStringTextView.setPaintFlags(holder.taskStringTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskStringTextView.setTextColor(Color.parseColor("#727272"));
        }

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new TaskViewHolder(itemHolder);
    }


    public final static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView taskStringTextView;
        CheckBox taskCompletedCheckBox;

        public TaskViewHolder(View itemView) {
            super(itemView);

            taskStringTextView = (TextView) itemView.findViewById(R.id.task_string_textView);
            taskCompletedCheckBox = (CheckBox) itemView.findViewById(R.id.task_completed_checkbox);

        }
    }

}

