/*
 * FILE        : WeatherDetail.java
 * PROJECT     : PROG3150 - Assignment #1
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-03-14
 * PURPOSE     : This holds the weather detail activity for view specifics
 *              about weather items from the RSS feed
 */

package com.example.madassign01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WeatherDetail extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        TextView titleTextView = (TextView) findViewById(R.id.lblWeatherTxt);
        TextView upDateTextView = (TextView) findViewById(R.id.lblWeatherDate);
        TextView summaryTextView = (TextView) findViewById(R.id.lblWeatherSummary);
        TextView linkTextView = (TextView) findViewById(R.id.textLink);
        Intent intent = getIntent();

        String update = intent.getStringExtra("update");
        String title = intent.getStringExtra("title");
        String summary = intent.getStringExtra("summary");

        titleTextView.setText(title);
        upDateTextView.setText(update);
        summaryTextView.setText(summary);
        linkTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        String link = "https://www.weather.gc.ca/city/pages/on-82_metric_e.html";
        Uri viewUri = Uri.parse(link);

        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);
    }
}