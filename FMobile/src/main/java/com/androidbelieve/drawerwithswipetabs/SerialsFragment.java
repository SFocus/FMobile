package com.androidbelieve.drawerwithswipetabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
public class SerialsFragment extends Fragment {
    private ArrayList<VideoItem> serialList = new ArrayList<>();
    private ArrayList<VideoItem> serialList2 = new ArrayList<>();
    private VideoAdapter mAdapter;
    private int page = 0;
    private static final float GESTURE_THRESHOLD_DP = 170.0f;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serials_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.serialsRecycleView);
        recyclerView.setHasFixedSize(true);
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        int mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        final GridAutofitLayoutManager llm = new GridAutofitLayoutManager(view.getContext(), mGestureThreshold);
        recyclerView.setLayoutManager(llm);
        mAdapter = new VideoAdapter(serialList, getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                page = current_page;
                new LoadSerials().execute();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("recyclerData")) {
            serialList2 = savedInstanceState.getParcelableArrayList("recyclerData");
            serialList.addAll(serialList2);
            mAdapter.notifyDataSetChanged();
        } else {
            new LoadSerials().execute();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("recyclerData", serialList);
    }

    private class LoadSerials extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            String url = QueryBuilder.buildQuery(DataSource.getUrl("media.getSerials"), page);
            return DataSource.executeQuery(url);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            serialList2.clear();
            serialList2 = new PageParser(document).getFilms();
            serialList.addAll(serialList2);
            mAdapter.notifyDataSetChanged();
        }
    }


}
