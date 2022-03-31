/*
 * FILE        : EmployeeActivity.java
 * PROJECT     : PROG3150 - Assignment #1
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-02-09
 * NAME        : EmployeeActivity
 * PURPOSE     : On this page, an employees current tasks can be viewed. Tasks are
 *              organized from top to bottom in the order they were assigned, and
 *              are accompanied by a progress bar to show the remaining time in
 *              the task.
 */

package com.example.madassign01;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class EmployeeActivity extends AppCompatActivity {

    String currentEmployee = null;
    List <Employee> employeeList = new ArrayList<>();
    TextView displayName = null;
    int i = 0;
    public TextView etaText = null;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        displayName = findViewById(R.id.lblEmpName);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            currentEmployee = extras.getString("name");
            displayName.setText(currentEmployee);
        }

        EmployeeDB dbase = new EmployeeDB(this);
        employeeList = dbase.getEmployees();
        ArrayList<String> names = dbase.getEmployeeNames();

        for (Employee employee : employeeList)
        {

            if(employee.name.toString().equals(currentEmployee.toString())) {
                break;
            }
            i++;
        }

        if(employeeList.get(i).taskList.size() > 0)
        {
            long newTime = employeeList.get(i).taskList.get(employeeList.get(i).taskList.size() - 1).taskEnd.getTime();

        }


        //Check if there was a valid employee selected
        if (employeeList.size() == i)
        {
            Toast.makeText(getBaseContext(), "Invalid Name", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText(getBaseContext(), employeeList.get(i).name+ " selected", Toast.LENGTH_SHORT ).show();
        }

        TextView text = new TextView(this);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(layoutParams);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearParams);

        scrollView.addView(linearLayout);
        List<Task> tempList = dbase.getTasks(employeeList.get(i).name);

        //next section of code populates the list of current tasks
        //the list is pulled from the current employee, and tasks
        //fill up their accompanying progress bar as they get closer to completion
        //for (Task task : employeeList.get(i).taskList) {
        for (Task task : tempList) {

            TextView TextView1 = new TextView(this);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.setMargins(0, 30, 0, 10);
            params1.gravity = Gravity.CENTER;
            TextView1.setText((task.task.toString().toString()));
            TextView1.setLayoutParams(params1);
            TextView1.setTextSize(20);
            linearLayout.addView(TextView1);

            TextView noteTextView = new TextView(this);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params4.setMargins(0, 30, 0, 10);
            params4.gravity = Gravity.CENTER;
            noteTextView.setText((task.note.toString()));
            noteTextView.setLayoutParams(params4);
            noteTextView.setTextSize(12);
            linearLayout.addView(noteTextView);

            ProgressBar progressBar = new ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 10, 75, 30);
            params2.gravity = Gravity.CENTER_HORIZONTAL;


            float barProgress = 0;
            Timestamp tempTime = new Timestamp(System.currentTimeMillis());
            barProgress = tempTime.getTime() - task.taskStart.getTime();
            //math?
            barProgress = (barProgress / 60000) / (task.taskLength) * 100;

            progressBar.setProgress((int) barProgress);


            linearLayout.addView(progressBar,250,50);

            View hr = new View(this);
            hr.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            hr.setBackgroundColor(Color.parseColor("#B3B3B3"));
            linearLayout.addView(hr);
        }
        LinearLayout linearLayout1 = findViewById(R.id.linearLayout);
        if (linearLayout1 != null) {
            linearLayout1.addView(scrollView);
        }

        //on button click, writes current employee info to a serializable file
        Button nextViewEmployee = (Button) findViewById(R.id.btnAddTask);
        nextViewEmployee.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), AssignTasks.class);
                Bundle extras = new Bundle();
                extras.putString("name", currentEmployee);
                myIntent.putExtras(extras);
                startActivity(myIntent);
                finish();
            }

        });

        Button changeEmployee = (Button) findViewById(R.id.btnChangeEmployee);
        changeEmployee.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.getItem(1).setEnabled(false);
        return true;
    }

    // Check for menu item being selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.mymenu_activity1:
                finish();
                break;
            case R.id.mymenu_activity2:
                break;
            case R.id.mymenu_activity3:
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                result = true;
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    private void updateView(final long elapsedMillis) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                etaText.setText(String.valueOf(elapsedMillis));

            }
        });
    }
}