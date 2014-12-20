package com.rdevblog.rtodo;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rdevblog.rtodo.adapters.TaskListAdapter;
import com.rdevblog.rtodo.adapters.TaskRecyclerAdapter;
import com.rdevblog.rtodo.objects.Task;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment{


    RecyclerView taskListView;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =
            inflater.inflate(R.layout.fragment_task_list, container, false);

        taskListView = (RecyclerView) view.findViewById(R.id.task_list_view);

        TaskRecyclerAdapter taskRecyclerAdapter = new TaskRecyclerAdapter(Realm.getInstance(getActivity()).where(Task.class).findAll(), getActivity());
        taskListView.setAdapter(taskRecyclerAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        taskListView.setLayoutManager(mLayoutManager);

        return view;
    }


    public void onNewTaskCreated() {

        taskListView.getAdapter().notifyDataSetChanged();



    }


}
