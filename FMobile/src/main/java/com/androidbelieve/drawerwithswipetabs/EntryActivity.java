package com.androidbelieve.drawerwithswipetabs;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.DetailedSearchAdapter;
import models.SearchItem;
import models.VideoEntry;

public class EntryActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private SliderLayout mDemoSlider;

    private VideoEntry entry;

    private ArrayList<SearchItem> searchResult = new ArrayList<>();

    private DetailedSearchAdapter searchAdapter;

    private String intentAction;
    Toolbar myToolbar;
    Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url;
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
                 myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);
                font = Typeface.createFromAsset(getBaseContext().getAssets(), "fontawesome-webfont.ttf" );

                mDemoSlider = (SliderLayout)findViewById(R.id.slider);

                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(6000);
                mDemoSlider.addOnPageChangeListener(this);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

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
            List<String> URLImage = entry.getImages();
            Log.d("size", URLImage.size() + "");
            new LoadImages(URLImage).execute();
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

    private class LoadImages extends AsyncTask<String, Void, List<String>>
    {
        private List<String> list;

        public LoadImages(List<String> list)
        {
            this.list = list;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            return list;
        }

        @Override
        protected void onPostExecute(List<String> doc) {
            super.onPostExecute(doc);
            myToolbar.setTitle(entry.getName());
            for(int i=0; i<doc.size(); i++)
            {
                TextSliderView textSliderView = new TextSliderView(getBaseContext());
                // initialize a SliderLayout
                textSliderView
                        .image("http:"+doc.get(i))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                //add your extra information
                textSliderView.bundle(new Bundle());
                mDemoSlider.addSlider(textSliderView);
            }

            TextView likes = (TextView) findViewById(R.id.fa_likes);
            TextView comments = (TextView) findViewById(R.id.fa_comments);
            TextView files = (TextView) findViewById(R.id.fa_folder);

            likes.setTypeface(font);
            comments.setTypeface(font);
            files.setTypeface(font);

            String text = likes.getText() + "  " + entry.getPositiveVotes();
            likes.setText(text);
           // text = R.string.fa_comments + "  " + entry.get
          //  text = R.string.fa_folder_open ;
            files.setText(R.string.fa_folder_open);

        }
    }
}
