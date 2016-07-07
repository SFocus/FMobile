package com.androidbelieve.drawerwithswipetabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import models.VideoEntry;

public class EntryActivity extends AppCompatActivity {

    private VideoEntry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Bundle data = getIntent().getExtras();
        if(data.containsKey("link"))
        {
            String url = QueryBuilder.buildQuery(
                    DataSource.getUrl("media.getEntry"),
                    data.getString("link")
            );
            new LoadEntry(url).execute();
        }
    }


    /**
     * Loads information about certain entry by given url
     */
    private class LoadEntry extends AsyncTask<String, Void, Document>
    {
        private String url;

        public LoadEntry(String url)
        {
            this.url = url;
        }

        @Override
        protected Document doInBackground(String... strings) {
            return DataSource.executeQuery(this.url);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            EntryActivity.this.entry = new PageParser(document).getEntry();
        }
    }
}
