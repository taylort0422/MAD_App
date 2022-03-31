/*
 * FILE        : RSSHandler.java
 * PROJECT     : PROG3150 - Assignment #2
 * PROGRAMMERS : Taylor Trainor, Will Schwetz, Josh Evans, Ashley Ingle
 * LAST EDIT   : 2022-04-14
 * NAME        : RSSFeed
 * PURPOSE     : Based on a class from Igor Pustylnick, this class handles an RSS feed
 */

package com.example.madassign01;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class RSSHandler extends DefaultHandler{
    private RSSFeed feed;
    private RSSItem item;

    private boolean feedTitleHasBeenRead = false;
    private boolean feedUpDateHasBeenRead = false;

    private boolean isTitle = false;
    private boolean isSummary = false;
    private boolean isUpDateDate = false;

    public RSSFeed getFeed() {return feed;}

    /*
    Method:     startDocument
    Parameters: N/A
    Returns:    N/A
    Purpose:    This initiates feed and item objects
    */
    public void startDocument() throws SAXException{
        feed = new RSSFeed();
        item = new RSSItem();
    }

    /*
    Method:     startElement
    Parameters: the URL
                the local name
                the element name
                attributes
    Returns:    The size of the items list
    Purpose:    This RSS item to the list of RSS items
    */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException{

        // Check for the beginning of an item as "entry"
        // If we find it, build the RSS Item object
        if (qName.equals("entry")){
            item = new RSSItem();
        }
        else if(qName.equals("title"))
        {
            isTitle = true;
        }
        else if(qName.equals("summary"))
        {
            isSummary = true;
        }
        else if(qName.equals("updated"))
        {
            isUpDateDate = true;
        }
    }

    /*
    Method:     endElement
    Parameters: the URL
                the local name
                the element name
                attributes
    Returns:    N/A
    Purpose:    This finds the end of an entry "item"
    */
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("entry")) {
            feed.addItem(item);
            return;
        }
    }

    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
        if (isTitle) {
            if (feedTitleHasBeenRead == false) {
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            }
            else {
                item.setTitle(s);
            }
            isTitle = false;
        }
        else if (isSummary) {
            if (s.startsWith("<")) {
                item.setSummary("No summary available.");
            }
            else{
                item.setSummary(s);
            }
            isSummary = false;
        }
        else if (isUpDateDate) {
            if (feedUpDateHasBeenRead == false) {
                feed.setUpdateDate(s);
                feedUpDateHasBeenRead = true;
            }
            else {
                item.setUpDate(s);
            }
            isUpDateDate = false;
        }
    }
}
