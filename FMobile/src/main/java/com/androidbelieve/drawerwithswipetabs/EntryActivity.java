package com.androidbelieve.drawerwithswipetabs;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.DetailedSearchAdapter;
import models.SearchItem;
import models.VideoEntry;

public class EntryActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private VideoEntry entry;

    private ArrayList<SearchItem> searchResult = new ArrayList<>();

    private DetailedSearchAdapter searchAdapter;

    private String intentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url;
        Log.d("INTENT",intent.getAction() + "");
        this.intentAction = intent.getAction();
        switch (this.intentAction)
        {
            //On suggestion select
            case Intent.ACTION_VIEW :
                setContentView(R.layout.activity_entry);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                String link = intent.getDataString() == null ? intent.getStringExtra("link") : intent.getDataString();
                url = QueryBuilder.buildQuery(
                        DataSource.getUrl("media.getEntry"),
                        link
                );
                new LoadEntry(url).execute();
                break;

            //On query submit
            case Intent.ACTION_SEARCH :
                setContentView(R.layout.activity_search);

                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.SearchRecycleView);
                recyclerView.setLayoutManager(llm);

                searchAdapter = new DetailedSearchAdapter(this.searchResult, this);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(searchAdapter);

                String query = intent.getStringExtra(SearchManager.QUERY);

                if(savedInstanceState != null && savedInstanceState.containsKey("recyclerData"))
                {
                    ArrayList<SearchItem> temp = savedInstanceState.getParcelableArrayList("recyclerData");
                    searchResult.addAll(temp);
                    searchAdapter.notifyDataSetChanged();
                }
                else
                {
                    url = QueryBuilder.buildQuery(
                            DataSource.getUrl("media.detailedSearch"),
                            query
                    );

                    new DetailedSearch(url).execute();
                }
                getSupportActionBar().setTitle(String.format("Пошук %s", query));
                break;
            default:
                Log.d("TEST","notfound");
                break;
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("recyclerData", searchResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
        return true;
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

    /**
     * Detailed search for certain query
     */
    private class DetailedSearch extends AsyncTask<String, Void, Document>
    {
        private String url;

        public DetailedSearch(String url)
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
            searchResult.addAll(new PageParser(document).getDetailedSearch());
            searchAdapter.notifyDataSetChanged();
        }
    }
}
