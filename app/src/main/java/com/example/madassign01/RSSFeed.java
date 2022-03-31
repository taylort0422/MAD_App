/*
 * FILE        : RSSFeed.java
 * PROJECT     : PROG3150 - Assignment #2
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-04-14
 * NAME        : RSSFeed
 * PURPOSE     : Based on a class from Igor Pustylnick, this class represents and RSS Feed
 */

package com.example.madassign01;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class RSSFeed {
    private String title = null;
    private String updateDate = null;
    private ArrayList<RSSItem> items;

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss Z");
    public RSSFeed(){ items = new ArrayList<RSSItem>();}

    public void setTitle(String title) { this.title = title; }

    public String getTitle() {
        return title;
    }

    public void setUpdateDate(String updated) {
        this.updateDate = updated;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    /*
    Method:     addItem
    Parameters: An RSS Item object
    Returns:    The size of the items list
    Purpose:    This RSS item to the list of RSS items
    */
    public int addItem(RSSItem item) {
        items.add(item);
        return items.size();
    }

    public RSSItem getItem(int index) {
        return items.get(index);
    }

    public ArrayList<RSSItem> getAllItems() {
        return items;
    }
}
