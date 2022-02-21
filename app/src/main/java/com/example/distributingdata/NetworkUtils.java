//Julien West
package com.example.distributingdata;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {
    private static final String LOG_TAG =
            NetworkUtils.class.getSimpleName();

    // Base URL for Books API
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type
    private static final String PRINT_TYPE = "printType";

    static ArrayList<GoogleBookModel> getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        //Create GoogleBookModel ArrayList to store results
        ArrayList<GoogleBookModel> bookResults = new ArrayList<GoogleBookModel>();

        try {
            // Construct URI with parameters
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the URL connection and make a request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response
            StringBuilder builder = new StringBuilder();

            //Read the input line-by-line into the string while there is still input
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            // Check to see it there is response content, if not return null
            if (builder.length() == 0) {
                // Stream was empty. No point in parsing
                return null;
            }

            // Store StringBuilder results into bookJsonString
            bookJSONString = builder.toString();

            // Parse the JSON string
            try {
                JSONObject jsonObject = new JSONObject(bookJSONString);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                // Initialize variables used for the parsing loop
                int i = 0;
                String title = null;
                String authors = null;
                String description = null;
                String publisher = null;
                String publishedDate = null;

                // Iterate through all the items and add them to the
                while (i < itemsArray.length()) {
                    // Get the current item information
                    JSONObject book = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    // Try to get the title, authors, description, publisher, and published date from the current item,
                    // catch if any field is empty and move on
                    try {
                        title = volumeInfo.getString("title");
                        authors = volumeInfo.getString("authors");
                        try { description = volumeInfo.getString("description"); }
                        catch (Exception e) { description = " "; }
                        try { publisher = volumeInfo.getString("publisher"); }
                        catch (Exception e) { publisher = " "; }
                        try { publishedDate = volumeInfo.getString("publishedDate"); }
                        catch (Exception e) { publishedDate = " "; }

                        //Parse data into GoogleBookModel and add to results
                        GoogleBookModel parsedBook = new GoogleBookModel(title, authors, description, publisher, publishedDate);
                        bookResults.add(parsedBook);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Move to the next item
                    i++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            //Close both the connection and the buffered reader
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Log results
            Log.d(LOG_TAG, bookJSONString);
        }

        return bookResults;
    }
}
