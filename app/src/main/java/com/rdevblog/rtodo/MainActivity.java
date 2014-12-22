package com.rdevblog.rtodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.melnykov.fab.FloatingActionButton;
import com.rdevblog.rtodo.adapters.SpinnerTaskListAdapter;
import com.rdevblog.rtodo.objects.Task;
import com.rdevblog.rtodo.objects.TaskList;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends Activity{

    private EditText newTaskEditText;
    private ImageButton addNewTaskButton;
    private Boolean runTextAnimation = true;

    private ImageButton newTaskListButton;

    private ArrayAdapter<TaskList> itemAdapter;

    private boolean newTaskOpen = false;

    public final static String TASKLISTNAMETAG = "taskListNameForFragment";

    TaskList taskList;

    FrameLayout taskListFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        if (realm.where(TaskList.class).findAll().size() == 0){
            TaskList defaultTaskList = realm.createObject(TaskList.class);
            defaultTaskList.setListName("default");
            final RealmResults<Task> realmResults = Realm.getInstance(this).where(Task.class).findAll();
            for (int i = 0; i < realmResults.size(); i++) {
                Task task = realmResults.get(i);
                task.setTaskList(Realm.getInstance(this).where(TaskList.class).findAll().first());
            }
        }
        realm.commitTransaction();
        realm.close();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayShowTitleEnabled(false);

        taskListFrameLayout = (FrameLayout)findViewById(R.id.task_list_container);

        final RealmResults<TaskList> taskLists = Realm.getInstance(this).where(TaskList.class).findAll();

        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        itemAdapter = new SpinnerTaskListAdapter(this, R.layout.spinner_list_item, taskLists);
        itemAdapter.setDropDownViewResource(R.layout.black_simple_spinner_dropdown_item);
        spinner.setAdapter(itemAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                taskList = taskLists.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(TASKLISTNAMETAG,taskList.getListName());

                TaskListFragment taskListFragment = new TaskListFragment();
                taskListFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.task_list_container, taskListFragment)
                        .commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        Realm realm = Realm.getInstance(getApplicationContext());
//        realm.beginTransaction();
//        taskLists.get(position).removeFromRealm();
//        realm.commitTransaction();
//        realm.close();
//        itemAdapter.notifyDataSetChanged();


        toolbar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.toolbar_minimized_height);

        FloatingActionButton newTaskFab = (FloatingActionButton)findViewById(R.id.new_task_fab);
        newTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!newTaskOpen){


                    int listFrameLayoutBefore = taskListFrameLayout.getTop();

                    toolbar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.toolbar_maximised_height);
                    newTaskEditText.setVisibility(View.VISIBLE);
                    newTaskOpen = true;

                    newTaskEditText.requestFocus();

                    float beforeScale = getResources().getDimension(R.dimen.toolbar_minimized_height)/getResources().getDimension(R.dimen.toolbar_maximised_height);
                    Animation toolbarScaleAnimation = new ScaleAnimation(1,1,beforeScale, 1);
                    toolbarScaleAnimation.setDuration(200);
                    toolbar.startAnimation(toolbarScaleAnimation);

                    Animation editTextScaleAnimation = new ScaleAnimation(0,1,1,1);
                    editTextScaleAnimation.setDuration(200);
                    editTextScaleAnimation.setStartOffset(200);
                    newTaskEditText.startAnimation(editTextScaleAnimation);
                    newTaskEditText.setFocusable(true);

                    Animation frameLayoutTranslateAnimation = new TranslateAnimation(0,0,-listFrameLayoutBefore,0);
                    frameLayoutTranslateAnimation.setDuration(200);

                    taskListFrameLayout.startAnimation(frameLayoutTranslateAnimation);

                }
                else {

                    toolbar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.toolbar_minimized_height);
                    newTaskEditText.setVisibility(View.GONE);
                    newTaskOpen = false;

                    float beforeScale = getResources().getDimension(R.dimen.toolbar_maximised_height)/getResources().getDimension(R.dimen.toolbar_minimized_height);
                    Animation animation = new ScaleAnimation(1,1,beforeScale, 1);
                    animation.setDuration(200);
                    toolbar.startAnimation(animation);

                    Animation frameLayoutTranslateAnimation = new TranslateAnimation(0,0,getResources().getDimensionPixelSize(R.dimen.toolbar_difference_height),0);
                    frameLayoutTranslateAnimation.setDuration(200);

                    taskListFrameLayout.startAnimation(frameLayoutTranslateAnimation);

                }

            }
        });

        newTaskEditText = (MaterialEditText)findViewById(R.id.new_task_edit_text);

        newTaskEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    newTaskAdd();
                }
                return false;
            }
        });

        newTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (newTaskEditText.getText().toString().equals("")){
                    ScaleAnimation tickShrink = new ScaleAnimation(1,0,1,0, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
                    tickShrink.setDuration(200);
                    addNewTaskButton.startAnimation(tickShrink);
                    addNewTaskButton.setVisibility(View.GONE);

                    ScaleAnimation editTextExpand = new ScaleAnimation((float) .8, 1, 1, 1);
                    editTextExpand.setDuration(200);
                    newTaskEditText.startAnimation(editTextExpand);

                    runTextAnimation = true;
                }
                else {
                    if (runTextAnimation) {
                        addNewTaskButton.setVisibility(View.VISIBLE);
                        ScaleAnimation tickExpand = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
                        tickExpand.setDuration(200);
                        addNewTaskButton.startAnimation(tickExpand);

                        ScaleAnimation editTextShrink = new ScaleAnimation((float) 1.2, 1, 1, 1);
                        editTextShrink.setDuration(200);
                        newTaskEditText.startAnimation(editTextShrink);

                        runTextAnimation = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addNewTaskButton = (ImageButton)findViewById(R.id.add_new_task_button);
        addNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskAdd();
            }
        });

        newTaskListButton = (ImageButton)findViewById(R.id.new_task_list_button);
        newTaskListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskListDialog();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        //I have made a change

        return super.onOptionsItemSelected(item);
    }


    /*Custom mehtods*/
    private void newTaskAdd(){

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        Task newTask = realm.createObject(Task.class);
        newTask.setTaskCompleted(false);
        newTask.setTaskString(newTaskEditText.getText().toString());
        newTask.setTaskList(taskList);
        Log.d("new Task object in database", newTask.toString());
        realm.commitTransaction();
        realm.close();

        newTaskEditText.setText("");

        TaskListFragment taskListFragment = (TaskListFragment)getFragmentManager().findFragmentById(R.id.task_list_container);
        taskListFragment.onNewTaskCreated();

    }


    private void showTaskListDialog(){

        LayoutInflater inflater = LayoutInflater.from(this);
        final View newTaskListDialog = inflater.inflate(R.layout.new_task_list_dialog, null);

        final MaterialEditText taskListNameEdit = (MaterialEditText) newTaskListDialog.findViewById(R.id.task_list_name_editText);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(newTaskListDialog);
        builder
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Realm realm = Realm.getInstance(getApplicationContext());
                        realm.beginTransaction();
                        TaskList newTaskList = realm.createObject(TaskList.class);
                        newTaskList.setListName(taskListNameEdit.getText().toString());
                        realm.commitTransaction();
                        realm.close();

                        itemAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}























