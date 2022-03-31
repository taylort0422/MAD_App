/*
 * FILE        : WeatherActivity.java
 * PROJECT     : PROG3150 - Assignment #1
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-03-14
 * PURPOSE     : This holds the weather activity used to display current conditions and warnings
 */

package com.example.madassign01;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WeatherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    final String FILENAME = "weather_feed.xml";

    TextView header = null;
    ListView feedListView = null;
    RSSFeed feed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        new RetrieveFileTask().execute(new String[] {"https://weather.gc.ca/rss/city/on-82_e.xml"});
        header = (TextView)findViewById(R.id.lblWeatherPage);
        feedListView = (ListView)findViewById(R.id.feedListView);
    }

    /*
    Class:     RetrieveFileTask
    Purpose:   This class is used to retrieve the XML from an RSS feed
     */
    class RetrieveFileTask extends AsyncTask<String, Void, Void> {

        /*
        Method:     doInBackground
        Parameters: strings representing the URLs to fetch the stream fromm
        Returns:    N/A
        Purpose:    This loads a file from a URL
         */
        @Override
        protected Void doInBackground (String... params)
        {
            try
            {
                // Get the URL
                URL url = new URL((String)params[0]);
                InputStream in = url.openStream();
                FileOutputStream out = openFileOutput(FILENAME, Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                StringBuffer sb = new StringBuffer();
                // Write the file locally to XML file
                while (bytesRead != -1)
                {
                    sb.append(new String(buffer, "UTF-8"));
                    out.write(buffer, 0, bytesRead);
                    bytesRead = in.read(buffer);
                }
                Log.i("MyApp", sb.toString());
                out.close();
                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("Weather feed", "Feed downloaded: " + new Date());
            new ReadFile().execute();
        }
    }

    /*
    Class:     ReadFile
    Purpose:   This class is used to read the created XML file and parse it for output
     */
    class ReadFile extends AsyncTask<Void, Void, RSSFeed>
    {
        /*
        Method:     doInBackground
        Parameters: N/A
        Returns:    N/A
        Purpose:    This loads a file from the local machine
         */
        @Override
        protected RSSFeed doInBackground(Void... params)
        {
            Log.i("Debug:","Reading File");
            try{
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlReader = parser.getXMLReader();

                RSSHandler theRssHandler = new RSSHandler();
                xmlReader.setContentHandler(theRssHandler);

                FileInputStream in = openFileInput(FILENAME);

                InputSource is = new InputSource(in);
                xmlReader.parse(is);

                RSSFeed feed = theRssHandler.getFeed();
                return feed;
            }
            catch(Exception e)
            {
                Log.e("Weather Reader", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(RSSFeed result)
        {

            WeatherActivity.this.feed = result;
            WeatherActivity.this.updateDisplay();
        }
    }

    /*
    Method:     updateDisplay
    Parameters: N/A
    Returns:    N/A
    Purpose:    This updates the GUI with the apropriate weather RSS feed information
     */
    public void updateDisplay()
    {
        if(feed == null){

            // change title for error here
            Toast.makeText(this, "FEED IS NULL", Toast.LENGTH_SHORT).show();
            header.setText("Error Getting Weather Data");
            Log.d("Error", "Could not get feed!");
        }

        ArrayList<RSSItem> items = feed.getAllItems();

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for(RSSItem item:items)
        {
            HashMap<String, String> map = new HashMap<String, String >();
            map.put("updated", item.getUpDateFormatted());
            map.put("title", item.getTitle());
            data.add(map);
        }

        int resource = R.layout.listview_item;
        String[] from = {"updated", "title"};
        int[] to = {R.id.upDateTextView, R.id.titleTextView};

        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        feedListView.setOnItemClickListener(this);
        feedListView.setAdapter(adapter);
    }

    /*
    Method:     onItemClick
    Parameters: parent adapter
                The current view
                position
                ID
    Returns:    N/A
    Purpose:    This builds an intent that shows specific weather details
                depending on the weather that is current
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){

        RSSItem item = feed.getItem(position);
        Intent intent = new Intent(this, WeatherDetail.class);
        intent.putExtra("update", item.getUpDateFormatted());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("summary", item.getSummary());
        this.startActivity(intent);
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
        menu.getItem(2).setEnabled(false);
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
                finish();
                break;
            case R.id.mymenu_activity3:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
}