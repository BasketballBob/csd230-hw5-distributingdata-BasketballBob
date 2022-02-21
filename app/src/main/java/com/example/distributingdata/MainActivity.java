//Julien West
package com.example.distributingdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity
    implements ItemAdapter.ListItemClickListener {
    private ArrayList<GoogleBookModel> bookData;
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;
    private ItemAdapter itemAdapter;
    private RecyclerView bookList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookInput = (EditText)findViewById(R.id.bookInput);
        mTitleText = (TextView)findViewById(R.id.titleText);
        mAuthorText = (TextView)findViewById(R.id.authorText);
        bookList = (RecyclerView)findViewById(R.id.bookRecyclerView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Create layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookList.setLayoutManager(layoutManager);
        bookList.setHasFixedSize(true);

        //Initialize adapter
        itemAdapter = new ItemAdapter(bookData, this);

        bookList.setAdapter(itemAdapter);
    }

    public void searchBooks(View view) {
        //Get the search string from input
        String queryString = mBookInput.getText().toString();

        //Make progressBar visible while query is running
        progressBar.setVisibility(View.VISIBLE);

        //Use an Async Function to call the API request and store the results
        CompletableFuture<Void> bookResults = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {

                //Run query to for search data
                bookData = NetworkUtils.getBookInfo(queryString);
                Log.d("TEST", bookData.toString());

                //Update UI variables on main thread (as they won't properly update otherwise)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Make progress bar invisible once finished
                        progressBar.setVisibility(View.INVISIBLE);

                        //Update recycler view with new data
                        itemAdapter.setBookList(bookData);
                        itemAdapter.notifyDataSetChanged();

                        //Display an error toast if bookData is empty
                        if (bookData.size() == 0)
                        {
                            Toast error = Toast.makeText(getApplicationContext(), "Error: search found no books!", Toast.LENGTH_LONG);
                            error.show();
                        }
                    }
                });
            }
        });

        //Hide text window
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onListItemClick(int index, View itemView) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("title", bookData.get(index).getTitle());
        i.putExtra("authors", bookData.get(index).getAuthors());
        i.putExtra("description", bookData.get(index).getDescription());
        i.putExtra("publisher", bookData.get(index).getPublisher());
        i.putExtra("publishDate", bookData.get(index).getPublishedDate());
        startActivity(i);
    }

}