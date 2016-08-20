package com.androidbelieve.drawerwithswipetabs;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.RecyclerBindingAdapter;
import helpers.EndlessRecyclerOnScrollListener;
import helpers.GridAutofitLayoutManager;
import models.VideoItem;

/**
 * Created by Ratan on 7/29/2015.
 */
public class FilmsFragment extends Fragment{
    // The gesture threshold expressed in dp
    private static final float GESTURE_THRESHOLD_DP = 170.0f;
    private ArrayList<VideoItem> filmList = new ArrayList<>();
    private RecyclerBindingAdapter mAdapter;
    private Integer page = 0;
    private MainActivity.SortOrder sortOrder = MainActivity.SortOrder.TREND;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.films_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        // recyclerView.setHasFixedSize(true);
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        int mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        final GridAutofitLayoutManager llm = new GridAutofitLayoutManager(view.getContext(), mGestureThreshold);
        recyclerView.setLayoutManager(llm);

        mAdapter = new RecyclerBindingAdapter<>(R.layout.film_card, com.androidbelieve.drawerwithswipetabs.BR.video, filmList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore() {
                page++;
                new LoadFilms().execute();
            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        if (savedInstanceState != null && savedInstanceState.containsKey("recyclerData")) {
            ArrayList<VideoItem> temp = savedInstanceState.getParcelableArrayList("recyclerData");
            filmList.addAll(temp);
            mAdapter.notifyDataSetChanged();
        } else {
            new LoadFilms().execute();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("recyclerData", filmList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.menu_sort:
                final String[] items = {
                        MainActivity.SortOrder.NEW.getText(),
                        MainActivity.SortOrder.RATING.getText(),
                        MainActivity.SortOrder.YEAR.getText(),
                        MainActivity.SortOrder.POPULARITY.getText(),
                        MainActivity.SortOrder.TREND.getText()
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select sort order");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FilmsFragment.this.sortOrder = MainActivity.SortOrder.values()[which];
                        FilmsFragment.this.page = 0;
                        endlessRecyclerOnScrollListener.wipe();
                        filmList.clear();
                        mAdapter.notifyDataSetChanged();
                        new LoadFilms().execute();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return true;
    }

    private class LoadFilms extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            String url = QueryBuilder.buildQuery(
                    DataSource.getUrl("media.getFilms"),
                    new Object[] {sortOrder, page}
            );
            return DataSource.executeQuery(url);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            filmList.addAll(new PageParser(document).getFilms());
            mAdapter.notifyDataSetChanged();
        }
    }
}
