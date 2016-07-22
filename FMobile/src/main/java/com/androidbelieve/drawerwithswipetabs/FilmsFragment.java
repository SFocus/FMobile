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
import adapters.VideoAdapter;
import helpers.EndlessRecyclerOnScrollListener;
import helpers.GridAutofitLayoutManager;
import models.VideoItem;

/**
 * Created by Ratan on 7/29/2015.
 */
public class FilmsFragment extends Fragment{
    private ArrayList<VideoItem> filmList = new ArrayList<>();
    private VideoAdapter mAdapter;
    private int page = 0;
    // The gesture threshold expressed in dp
    private static final float GESTURE_THRESHOLD_DP = 170.0f;

    private MainActivity.SortOrder sortOrder = MainActivity.SortOrder.TREND;

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

        mAdapter = new VideoAdapter(filmList, getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                page = current_page;
                new LoadFilms(false).execute();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("recyclerData")) {
            ArrayList<VideoItem> temp = savedInstanceState.getParcelableArrayList("recyclerData");
            filmList.addAll(temp);
            mAdapter.notifyDataSetChanged();
        } else {
            new LoadFilms(false).execute();
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
                        new LoadFilms(true).execute();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return true;
    }

    private class LoadFilms extends AsyncTask<String, Void, Document> {

        private boolean clear;

        public LoadFilms(boolean clear)
        {
            this.clear = clear;
        }

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
            if(clear)
                filmList.clear();
            filmList.addAll(new PageParser(document).getFilms());
            mAdapter.notifyDataSetChanged();
        }
    }
}
