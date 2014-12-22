package com.rdevblog.rtodo.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rdevblog.rtodo.R;
import com.rdevblog.rtodo.objects.TaskList;

import io.realm.RealmResults;

/**
 * Created by TomSingleton on 22/12/14.
 */
public class SpinnerTaskListAdapter extends ArrayAdapter<TaskList> {

    RealmResults<TaskList> taskListRealmResults;

    public SpinnerTaskListAdapter(Context context, int resource, RealmResults<TaskList> taskListRealmResults) {
        super(context, resource, taskListRealmResults);

        this.taskListRealmResults = taskListRealmResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskList taskList = taskListRealmResults.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.text1);
        textView.setText(taskList.getListName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TaskList taskList = taskListRealmResults.get(position);

        TextView taskStringTextView;

        if (position == 0){
            Log.d("this", "margin top");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.black_simple_spinner_dropdown_item_top_margin, parent, false);
        }
        else if (position == taskListRealmResults.size()-1){
            Log.d("this", "margin bottom");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.black_simple_spinner_dropdown_item_botton_margin, parent, false);
        }
        else {
            Log.d("this", "nope");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.black_simple_spinner_dropdown_item, parent, false);
        }

        taskStringTextView = (TextView) convertView.findViewById(R.id.text2);

        if (taskList == null){
            Log.d("for fucks", "Sake");
        }

        taskStringTextView.setText(
                taskList.getListName());

        return convertView;
    }

}