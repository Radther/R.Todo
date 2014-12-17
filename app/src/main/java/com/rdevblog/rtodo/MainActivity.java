package com.rdevblog.rtodo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toolbar;

import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends Activity {

    EditText newTaskEditText;
    ImageButton addNewTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayShowTitleEnabled(false);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> itemAdapter   = ArrayAdapter.createFromResource(getActionBar().getThemedContext(),
                R.array.some_items, R.layout.spinner_list_item);
        itemAdapter.setDropDownViewResource(R.layout.black_simple_spinner_dropdown_item);
        spinner.setAdapter(itemAdapter);

        newTaskEditText = (MaterialEditText)findViewById(R.id.new_task_edit_text);
        addNewTaskButton = (ImageButton)findViewById(R.id.add_new_task_button);
        addNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskEditText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newTaskEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
}
