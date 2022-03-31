/*
 * FILE        : RSSItem.java
 * PROJECT     : PROG3150 - Assignment #2
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-04-14
 * NAME        : RSSFeed
 * PURPOSE     : Based on a class from Igor Pustylnick, this class represents a single RSS entry
 */

package com.example.madassign01;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class RSSItem {
    private String title = null;
    private String summary = null;
    private String upDate = null;

    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    public void setTitle(String title)     {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSummary(String description)     {
        this.summary = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setUpDate(String pubDate) {
        this.upDate = pubDate;
    }

    public String getUpDate() {
        return upDate;
    }

    public String getUpDateFormatted() {
        try {
            if (upDate == null) {
                upDate = dateInFormat.format(new Date());
                //Log.i("Debug Fromatted dte:","update is null");
            }
            //Date date = dateInFormat.parse(upDate.trim());
            //String upDateFormatted = dateOutFormat.format(date);

            return upDate;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
