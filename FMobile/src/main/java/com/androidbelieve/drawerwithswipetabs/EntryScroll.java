package com.androidbelieve.drawerwithswipetabs;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import DateBaseHundler.DBHandler;
import TabFragment.EntryTabFragment;
import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.RecyclerBindingAdapter;
import helpers.SimpleAsyncLoader;
import models.Favorites;
import models.SearchItem;
import models.VideoEntry;

public class EntryScroll extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    Toolbar myToolbar;
    Typeface font;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private SliderLayout mDemoSlider;
    private VideoEntry entry;
    private ArrayList<SearchItem> searchResult = new ArrayList<>();
    private RecyclerBindingAdapter searchAdapter;
    private String intentAction, link;
    private boolean isClickedFavoriteBtn = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String url;
        this.intentAction = intent.getAction();
        switch (this.intentAction) {
            //On suggestion select
            case Intent.ACTION_VIEW:
                setContentView(R.layout.activity_entry_scroll);

                //    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                link = intent.getDataString() == null ? intent.getStringExtra("link") : intent.getDataString();
                url = QueryBuilder.buildQuery(
                        DataSource.getUrl("media.getEntry"),
                        link
                );
                new LoadEntry().execute(url);

                // set custom toolbar
                myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                myToolbar.setTitle("");
                setSupportActionBar(myToolbar);

                // run container with fragments
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();


                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                // custom style text
                collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
                font = Typeface.createFromAsset(getBaseContext().getAssets(), "fontawesome-webfont.ttf");

                mDemoSlider = (SliderLayout) findViewById(R.id.slider);
                mDemoSlider.addOnPageChangeListener(this);

                final FloatingActionButton favoriteButton = (FloatingActionButton) findViewById(R.id.favorite);
                final DBHandler db = new DBHandler(getApplicationContext());
                if (db.checkItem(url)) {
                    isClickedFavoriteBtn = true;
                    favoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                }

                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isClickedFavoriteBtn) {
                            db.deleteItem(new Favorites(url));
                            isClickedFavoriteBtn = false;
                            Log.d("Item Deleted", "deleted");
                            favoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                        } else {
                            db.addNewURL(new Favorites(url));
                            Log.d("Item Add", "add");
                            favoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                            favoriteButton.setRippleColor(Color.RED);
                            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(Color
                                    .parseColor("#33691E")));
                            isClickedFavoriteBtn = true;
                        }
                    }
                });

                break;

            //On query submit
            case Intent.ACTION_SEARCH:
                setContentView(R.layout.activity_search);

                myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);

                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.SearchRecycleView);
                recyclerView.setLayoutManager(llm);

                searchAdapter = new RecyclerBindingAdapter<>(R.layout.search_item_card, BR.model, searchResult);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(searchAdapter);

                String query = intent.getStringExtra(SearchManager.QUERY);

                if (savedInstanceState != null && savedInstanceState.containsKey("recyclerData")) {
                    ArrayList<SearchItem> temp = savedInstanceState.getParcelableArrayList("recyclerData");
                    searchResult.addAll(temp);
                    searchAdapter.notifyDataSetChanged();
                } else {
                    url = QueryBuilder.buildQuery(
                            DataSource.getUrl("media.detailedSearch"),
                            query
                    );

                    new DetailedSearch().execute(url);
                }
                getSupportActionBar().setTitle(String.format("Пошук %s", query));
                break;
            default:
                Log.d("TEST", "notfound");
                break;
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
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
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    /**
     * Loads information about certain entry by given url
     */
    private class LoadEntry extends SimpleAsyncLoader {
        @Override
        protected void onPostExecute(Document document) {
            EntryScroll.this.entry = new PageParser(document).getEntry();
            EntryScroll.this.entry.setType(
                    VideoEntry.Type.get(link)
            );
            List<String> URLImage = entry.getImages();
            new LoadImages(URLImage).execute();
            Bundle bundle = new Bundle();
            bundle.clear();
            bundle.putString("link", link);
            bundle.putSerializable("entry", entry);
            EntryTabFragment entryTabFragment = new EntryTabFragment();
            entryTabFragment.setArguments(bundle);
            mFragmentTransaction.replace(R.id.entryContainerView, entryTabFragment).commit();
        }
    }

    /**
     * Detailed search for certain query
     */
    private class DetailedSearch extends SimpleAsyncLoader {
        @Override
        protected void onPostExecute(Document document) {
            searchResult.addAll(new PageParser(document).getDetailedSearch());
            searchAdapter.notifyDataSetChanged();
        }
    }

    private class LoadImages extends AsyncTask<String, Void, List<String>> {
        private List<String> list;

        public LoadImages(List<String> list) {
            this.list = list;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            return list;
        }

        @Override
        protected void onPostExecute(final List<String> doc) {
            super.onPostExecute(doc);

            for (int i = 0; i < doc.size(); i++) {
                TextSliderView textSliderView = new TextSliderView(getBaseContext());
                // initialize a SliderLayout

                textSliderView
                        .image("http:" + doc.get(i))
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                int position = mDemoSlider.getCurrentPosition();
                                Intent intent = new Intent(getApplicationContext(), FullScreanImage.class);
                                intent.putExtra("position", position);
                                ArrayList<String> list = (ArrayList<String>) doc;
                                intent.putExtra("images", list);
                                startActivity(intent);
                            }
                        })
                        .setScaleType(BaseSliderView.ScaleType.CenterInside);
                //add your extra information
                textSliderView.bundle(new Bundle());

                mDemoSlider.addSlider(textSliderView);
            }
            if (doc.size() == 1) {
                mDemoSlider.setCurrentPosition(0, false);
                mDemoSlider.stopAutoCycle();
            }

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(6000);
            myToolbar.setTitle(entry.getName());
        }
    }
}