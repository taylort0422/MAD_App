/*
 * FILE        : MainActivity.java
 * PROJECT     : PROG3150 - Assignment #1
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-02-09
 * NAME        : MainActivity
 * PURPOSE     : Main page for the Plakata Landscaping app. Here, you can cycle through
 *              employees and choose to either assign them new tasks, or view their
 *              currently assigned tasks
 */

package com.example.madassign01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import androidx.appcompat.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.annotation.SuppressLint;

public class MainActivity extends AppCompatActivity {

    private Spinner employeeSpinner = null;
    public String selectedEmployee = null;
    public List <Employee> employees = new ArrayList<Employee>();
    public Task job = null;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmployeeDB dbase = new EmployeeDB(this);
        employees = dbase.getEmployees();
        ArrayList<String> names = dbase.getEmployeeNames();


        // Set the spinner
        employeeSpinner = findViewById(R.id.spinEmployeeList);
        // Set the spinner

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,names);
        employeeSpinner.setAdapter(adapter);

        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEmployee = getSelectedEmployee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button nextAssign = (Button) findViewById(R.id.btnAssignNewTask);
        nextAssign.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), AssignTasks.class);
                Bundle extras = new Bundle();
                extras.putString("name", selectedEmployee);
                myIntent.putExtras(extras);
                startActivity(myIntent);
            }
        });

        Button nextViewEmployee = (Button) findViewById(R.id.btnViewCurrentTask);
        nextViewEmployee.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), EmployeeActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", selectedEmployee);
                myIntent.putExtras(extras);
                startActivity(myIntent);

            }

        });
    }

    String getSelectedEmployee()
    {
        return employeeSpinner.getSelectedItem().toString();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuBuilder m = (MenuBuilder)menu;
        m.setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.getItem(0).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.mymenu_activity2:
                intent = new Intent(this.getApplicationContext(), EmployeeActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", selectedEmployee);
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