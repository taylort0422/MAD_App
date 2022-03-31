/*
 * FILE        : AssignTasks.java
 * PROJECT     : PROG3150 - Assignment #2
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-03-15
 * NAME        : AssignTasks
 * PURPOSE     : This screen is where the user can assign tasks to employees.
 *              When assigning tasks, a user can add notes, and select the duration
 *              of the task.
 */

package com.example.madassign01;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

public class AssignTasks extends AppCompatActivity {
    int selectedTime = 0;
    private Spinner taskSpinner = null;
    public TextView displayName = null;
    public EditText editNote = null;
    private SeekBar timeBar = null;
    private TextView timeLength = null;
    public String selectedEmployee = null;
    String currentEmployee = null;
    List <Employee> employeeList = new ArrayList<>();
    String currentJob = null;
    int i = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_tasks);
        // File variables for finding the employee data

        EmployeeDB dbase = new EmployeeDB(this);
        employeeList = dbase.getEmployees();

        // The employee name label
        displayName = findViewById(R.id.lblEmployeeName);
        timeLength = findViewById(R.id.lblTaskLength);
        timeBar = findViewById(R.id.seekBarTime);

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                selectedTime = (int)(Integer.valueOf(i) * 0.6);
                String timeOut = selectedTime  + " mins";
                timeLength.setText(timeOut);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // The spinner task
        taskSpinner = findViewById(R.id.spinTasks);
        editNote = findViewById(R.id.editNotes);

        editNote.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_UNSPECIFIED)
                {
                    InputMethodManager imm = (InputMethodManager)textView.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                }
                return true;
            }
        });


        // Build spinner from task_list string array
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.task_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSpinner.setAdapter(adapter);

        // Get the employee which was selected from last page
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            currentEmployee = extras.getString("name");
            displayName.setText(currentEmployee);
        }

        // If an item on the spinner is selected
        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentJob = getSelectedJob();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // From the list of employees, find the current employee
        for (Employee employee : employeeList)
        {
            if(employee.name.equals(currentEmployee)) {
                break;
            }
            i++;
        }

        //Check if there was a valid employee selected
        if (employeeList.size() == i) //display error message
        {
            Toast.makeText(getBaseContext(), "Invalid Name", Toast.LENGTH_SHORT ).show();
        }
        else //display confirmation message
        {
            Toast.makeText(getBaseContext(), employeeList.get(i).name+ " selected", Toast.LENGTH_SHORT ).show();
        }


        selectedEmployee = employeeList.get(i).name;
        // If the button was pressed
        Button assignJob = (Button) findViewById(R.id.btnAssignTask);
        assignJob.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Task temp = new Task(currentJob,selectedTime);
                temp.note= editNote.getText().toString();
                temp.employeeName = currentEmployee;
                temp = employeeList.get(i).setNextTask(temp);

               if(dbase.insertTask(temp) < 0)
               {
                   Toast.makeText(getBaseContext(), "Could not insert into the database", Toast.LENGTH_SHORT ).show();
               }

                // Start intent for next activity
                Intent myIntent = new Intent(view.getContext(), EmployeeActivity.class);
                Bundle extras = new Bundle();
                // Save the name of the current employee
                extras.putString("name", currentEmployee);
                myIntent.putExtras(extras);
                startActivity(myIntent);
                // End this activity
                finish();
            }
        });

    }
    String getSelectedJob()
    {
        return taskSpinner.getSelectedItem().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.mymenu_activity1:
                finish();
                break;
            case R.id.mymenu_activity2:
                intent = new Intent(this.getApplicationContext(), EmployeeActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", currentEmployee);
                intent.putExtras(extras);
                startActivity(intent);
                result = true;
                break;
            case R.id.mymenu_activity3:
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                result = true;
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

}