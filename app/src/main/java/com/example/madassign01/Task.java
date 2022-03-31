/*
 * FILE        : Task.java
 * PROJECT     : PROG3150 - Assignment #1
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-02-09
 * NAME        : Task
 * PURPOSE     : The task class holds information on a task that is assigned to an employee.
 *               It has a name, length, optionally a note, and timestamps of when it
 *               starts and finishes.
*/
package com.example.madassign01;
import java.io.Serializable;
import java.sql.Timestamp;

public class Task implements Serializable {
    String employeeName;
    String task;
    String note;
    long taskLength;
    Timestamp taskStart;
    Timestamp taskEnd;
    int id;

    // --- Methods --- //
    //When the task is created, we set the task's start timestamp to the current time.
    public Task(){
        taskStart = new Timestamp(System.currentTimeMillis());
    }

    /*
    Method:     Task
    Parameters: string for a name of the task, and an integer for the length in minutes
    Returns:    N/A
    Purpose:    Constructor for the Task class
     */
    public Task(String taskName, int length)
    {
        note = "";
        task = taskName;
        taskLength = length;
        taskStart = new Timestamp(System.currentTimeMillis());
        taskEnd = new Timestamp(System.currentTimeMillis());
    }

    /*
    Method:     Task
    Parameters: string for a name of the task, and an integer for the length in minutes
                long for the length of the task
                string for any notes for the task
                long for the start time of the task
                long for the end time of the task
                int for the id of the task
                string for the name of the employee
    Returns:    N/A
    Purpose:    Overloaded constructor for the employee class to be generated from a DB
    */
    public Task(String taskName, long length, String notes,long taskStart, long taskEnd, int id, String name )
    {
        this.note = notes;
        this.taskStart = new Timestamp(taskStart);
        this.taskEnd = new Timestamp(taskEnd);
        this.task = taskName;
        this.taskLength = length;
        this.id = id;
        this.employeeName = name;
    }


    public long getId() {
        return id;
    }
    public void setId(int taskId) { this.id = taskId; }

    public long getTaskLength() {
        return taskLength;
    }
    public void setLength(long length) { this.taskLength = length; }

    public String getEmployeeName() {
        return employeeName;
    }
    public void setName(String name) { this.employeeName = name; }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public long getTaskStart() {
        return taskStart.getTime();
    }
    public void setStartTime(long taskStart) {
        this.taskStart = new Timestamp(taskStart);
    }

    public long getTaskEnd() { return taskEnd.getTime(); }
    public void setEndTime(long taskEnd) { this.taskEnd = new Timestamp(taskEnd) ;}

}
