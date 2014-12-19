package com.rdevblog.rtodo;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rdevblog.rtodo.adapters.TaskListAdapter;
import com.rdevblog.rtodo.objects.Task;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {


    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =
            inflater.inflate(R.layout.fragment_task_list, container, false);

        ListView taskListView = (ListView) view.findViewById(R.id.task_list_view);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.list_layout_controller);

        taskListView.setLayoutAnimation(animationController);

        final TaskListAdapter taskListAdapter = new TaskListAdapter(getActivity(), Realm.getInstance(getActivity()).where(Task.class).findAll(), true);
        taskListView.setAdapter(taskListAdapter);

        return view;
    }


}
