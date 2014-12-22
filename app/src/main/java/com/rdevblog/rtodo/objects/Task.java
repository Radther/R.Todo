package com.rdevblog.rtodo.objects;

import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by TomSingleton on 18/12/14.
 */
public class Task extends RealmObject {
    private String taskString;

    private boolean taskCompleted;

    private TaskList taskList;

    public String getTaskString() {
        return taskString;
    }

    public void setTaskString(String taskString) {
        this.taskString = taskString;
    }

    public boolean isTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(boolean taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
}
