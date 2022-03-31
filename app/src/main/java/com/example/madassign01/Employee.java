/*
 * FILE        : Employee.java
 * PROJECT     : PROG3150 - Assignment #1
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-02-09
 * NAME        : Employee
 * PURPOSE     : The employee class holds very basic information about an employee.
 *               It has a name and a list of tasks that are assigned or were previously assigned to it.
 */

package com.example.madassign01;

import android.content.Context;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Employee implements Serializable {

    public int id;
    public String name;
    public List<Task> taskList = new ArrayList<Task>();

    public long getId() {
        return id;
    }
    public void setId(int taskId) { this.id = taskId; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*
Method      : Employee
Parameters  : String for an employee name
Returns     : N/A
Purpose     : Constructor for the Employee class
*/
    public Employee(String newName)
    {
        name = newName;
    }

    public Employee(){
        name = null;
        id = 0;
    }

    /*
    Method      : setNextTask
    Parameters  : a task object
    Returns     : N/A
    Purpose     : To organize an employees tasks. The method sets the passed task's starting time
                to the previous task's end time
    */
    public Task setNextTask(Task curTask)
    {
        int numTasks = taskList.size();
        if(numTasks > 0)
        {
            curTask.taskStart = taskList.get(numTasks -1).taskEnd;
            curTask.taskEnd.setTime(curTask.taskStart.getTime() + TimeUnit.MINUTES.toMillis((long)curTask.taskLength));
        }
        else
        {
            curTask.taskEnd.setTime(curTask.taskStart.getTime() + TimeUnit.MINUTES.toMillis((long)curTask.taskLength));
        }
        return curTask;

    }

}
