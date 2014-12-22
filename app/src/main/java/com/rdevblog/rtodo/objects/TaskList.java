package com.rdevblog.rtodo.objects;

import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by TomSingleton on 22/12/14.
 */
public class TaskList extends RealmObject {

    private String ListName;

    public String getListName() {
        return ListName;
    }

    public void setListName(String listName) {
        ListName = listName;
    }

}
