/*
 * FILE        : EmployeeDB.java
 * PROJECT     : PROG3150 - Assignment #2
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-04-14
 * NAME        : EmployeeDB
 * PURPOSE     : The EmployeeDB class allows for connections and manipulation of the Employee Database
 */

package com.example.madassign01;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EmployeeDB {

    // database constants
    public static final String DB_NAME = "employee.db";
    public static final int    DB_VERSION = 1;

    // list table constants
    public static final String TASK_TABLE = "task";

    public static final String TASK_ID = "id";
    public static final int    TASK_ID_COL = 0;

    public static final String TASK_NAME = "taskname";
    public static final int    TASK_NAME_COL = 2;

    public static final String TASK_LENGTH = "length";
    public static final int    TASK_LENGTH_COL = 1;

    public static final String TASK_NOTE = "note";
    public static final int    TASK_NOTE_COL = 3;

    public static final String TASK_START = "taskStart";
    public static final int    TASK_START_COL = 4;

    public static final String TASK_END = "taskEnd";
    public static final int    TASK_END_COL = 5;

    public static final String TASK_EMPLOYEE_NAME = "employeeName";
    public static final int    TASK_EMPLOYEE_NAME_COL = 6;

    public static final String EMPLOYEE_TABLE = "employee";

    public static final String EMPLOYEE_ID = "id";
    public static final int EMPLOYEE_ID_COL = 0;

    public static final String EMPLOYEE_NAME = "name";
    public static final int EMPLOYEE_NAME_COL = 1;

    // Table definition for Employee
    public static final String CREATE_EMPLOYEE_TABLE =
            "CREATE TABLE " + EMPLOYEE_TABLE + " (" +
                    EMPLOYEE_ID     + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    EMPLOYEE_NAME + " TEXT NOT NULL); ";

    //Table definition for TASK
    public static final String CREATE_TASK_TABLE =
            "CREATE TABLE " + TASK_TABLE + " (" +
                    TASK_ID     + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    TASK_LENGTH + " TEXT NOT NULL, " +
                    TASK_NAME   + " TEXT NOT NULL, " +
                    TASK_NOTE   + " TEXT, " +
                    TASK_START  + " TEXT NOT NULL , " +
                    TASK_END    + " TEXT NOT NULL, "+
                    TASK_EMPLOYEE_NAME +" TEXT NOT NULL);";


    public static final String DROP_TASK_TABLE =
            "DROP TABLE IF EXISTS " + TASK_TABLE;

    public static final String DROP_EMPLOYEE_TABLE =
            "DROP TABLE IF EXISTS " + EMPLOYEE_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_TASK_TABLE);
            db.execSQL(CREATE_EMPLOYEE_TABLE);

            // Initialize with the employee information
            db.execSQL("INSERT INTO employee VALUES(1, 'Ashley')");
            db.execSQL("INSERT INTO employee VALUES(2, 'Josh')");
            db.execSQL("INSERT INTO employee VALUES(3, 'Taylor')");
            db.execSQL("INSERT INTO employee VALUES(4, 'Will')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(EmployeeDB.DROP_TASK_TABLE);
            db.execSQL(EmployeeDB.DROP_EMPLOYEE_TABLE);
            onCreate(db);
        }
    }
    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public EmployeeDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    /*
    Method:     openReadableDB
    Parameters: N/A
    Returns:    N/A
    Purpose:    Opens the database for reading
    */
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    /*
    Method:     openWriteableDB
    Parameters: N/A
    Returns:    N/A
    Purpose:    Opens the database for writing
    */
    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    /*
    Method:     closeDB
    Parameters: N/A
    Returns:    N/A
    Purpose:    Closes the database
    */
    public void closeDB() {
        if (db != null)
            db.close();
    }

    /*
    Method:     closeCursor
    Parameters: A cursor for the desired table
    Returns:    N/A
    Purpose:    Close the cursor
    */
    private void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

    /*
    Method:     getEmployees
    Parameters: N/A
    Returns:    A list of employees
    Purpose:    Get a list of employees from the database
    */
    public ArrayList<Employee> getEmployees(){
        // Create a new list
        ArrayList<Employee> employees = new ArrayList<>();
        // open the db
        try {
            openReadableDB();
            Cursor cursor = db.query(EMPLOYEE_TABLE,
                    null, null, null, null, null, null);
            // Iterate through employee table
            while (cursor.moveToNext()) {
                Employee employee = new Employee();
                employee.setId(cursor.getInt((EMPLOYEE_ID_COL)));
                employee.setName(cursor.getString(EMPLOYEE_NAME_COL));
                employees.add(employee);
            }
            closeCursor(cursor);
            closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }
        return employees;
    }

    /*
    Method:     getEmployeeNames
    Parameters: N/A
    Returns:    A list of employees names
    Purpose:    Get a list of employee's names from the database
    */
    public ArrayList<String> getEmployeeNames(){
        ArrayList<String> names = new ArrayList<>();
        try {
            openReadableDB();
            Cursor cursor = db.query(EMPLOYEE_TABLE,
                    null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(EMPLOYEE_NAME_COL);
                names.add(name);
            }
            closeCursor(cursor);
            closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }
        return names;
    }

    /*
    Method:     getTasks
    Parameters: String the employees name
    Returns:    A list of tasks
    Purpose:    Get a list of tasks for a specific employee
    */
    public ArrayList<Task> getTasks(String employeeName) {
        ArrayList<Task> tasks = new ArrayList<>();
        String where =
                TASK_EMPLOYEE_NAME + "= ?";
        String[] whereArgs = { employeeName};
        try {
            this.openReadableDB();
            Cursor cursor = db.query(TASK_TABLE, null,
                    where, whereArgs,
                    null, null, null);

            while (cursor.moveToNext()) {
                tasks.add(getTaskFromCursor(cursor));
            }
            this.closeCursor(cursor);
            this.closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }
        return tasks;
    }

    /*
    Method:     getTask
    Parameters: int representing a task id
    Returns:    A task
    Purpose:    Returns a specific task based on an id
    */
    public Task getTask(int id) {
        Task task = new Task();
        String where = TASK_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };
        try {
            this.openReadableDB();
            Cursor cursor = db.query(TASK_TABLE,
                    null, where, whereArgs, null, null, null);
            cursor.moveToFirst();
            task = getTaskFromCursor(cursor);
            this.closeCursor(cursor);
            this.closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }

        return task;
    }

    /*
    Method:     getTaskFromCursor
    Parameters: A cursor
    Returns:    A task
    Purpose:    Gets a task from the database
    */
    private static Task getTaskFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Task task = new Task(
                        cursor.getString(TASK_NAME_COL),
                        cursor.getInt(TASK_LENGTH_COL),
                        cursor.getString(TASK_NOTE_COL),
                        Long.parseLong(cursor.getString(TASK_START_COL)),
                        Long.parseLong(cursor.getString(TASK_END_COL)),
                        cursor.getInt(TASK_ID_COL),
                        cursor.getString(TASK_EMPLOYEE_NAME_COL));
                return task;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    /*
    Method:     insertTask
    Parameters: Task object
    Returns:    The row ID
    Purpose:    Inserts a task into the task table
    */
    public long insertTask(Task task) {
        // Build the insert
        ContentValues cv = new ContentValues();
        cv.put(TASK_NAME, task.getTask());
        cv.put(TASK_NOTE, task.getNote());
        cv.put(TASK_START, task.getTaskStart());
        cv.put(TASK_END, task.getTaskEnd());
        cv.put(TASK_LENGTH, task.getTaskLength());
        cv.put(TASK_EMPLOYEE_NAME, task.getEmployeeName());

        long rowID = -1;
        try {
            // Open and execute the insert
            this.openWriteableDB();
            rowID = db.insert(TASK_TABLE, null, cv);
            this.closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }

        return rowID;
    }

    /*
    Method:     updateTask
    Parameters: Task object
    Returns:    The row count
    Purpose:    This allows for a task, possible future implementation
    */
    public int updateTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_ID, task.getId());
        cv.put(TASK_NAME, task.getTask());
        cv.put(TASK_NOTE, task.getNote());
        cv.put(TASK_START, task.getTaskStart());
        cv.put(TASK_END, task.getTaskEnd());
        cv.put(TASK_LENGTH, task.getTaskLength());
        cv.put(TASK_EMPLOYEE_NAME, task.getEmployeeName());

        String where = TASK_ID + "= ?";
        String[] whereArgs = { String.valueOf(task.getId()) };
        int rowCount = -1;
        try {
            this.openWriteableDB();
            rowCount = db.update(TASK_TABLE, cv, where, whereArgs);
            this.closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }

        return rowCount;
    }

    /*
    Method:     deleteTask
    Parameters: int representing task id
    Returns:    The row count
    Purpose:    Deletes a task from the task table
    */
    public int deleteTask(long id) {
        String where = TASK_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };
        int rowCount = -1;
        try {
            this.openWriteableDB();
            rowCount = db.delete(TASK_TABLE, where, whereArgs);
            this.closeDB();
        }
        catch (Exception e)
        {
            Log.d("Error", "Error accessing database");
        }

        return rowCount;
    }
}