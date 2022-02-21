package com.example.distributingdata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private Bundle extras;
    private TextView title;
    private TextView authors;
    private TextView description;
    private TextView publisher;
    private TextView datePublished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.detailed_title);
        authors = findViewById(R.id.detailed_author);
        description = findViewById(R.id.detailed_description);
        publisher = findViewById(R.id.detailed_publisher);
        datePublished = findViewById(R.id.detailed_date);

        // Fill in all data fields with information from bundle
        extras = getIntent().getExtras();
        if (extras != null)
        {
            title.setText(extras.getString("title"));
            authors.setText("By " + extras.getString("authors"));
            description.setText(extras.getString("description"));
            publisher.setText("Published by " + extras.getString("publisher"));
            datePublished.setText("Date published: " + extras.getString("publishedDate"));
        }

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}