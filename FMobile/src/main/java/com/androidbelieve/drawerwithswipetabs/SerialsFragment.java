package com.androidbelieve.drawerwithswipetabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.EndlessRecyclerOnScrollListener;
import adapters.VideoAdapter;
import models.VideoItem;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SerialsFragment extends Fragment {
    private List<VideoItem> serialList = new ArrayList<>();
    private List<VideoItem> serialList2 = new ArrayList<>();
    private VideoAdapter mAdapter;
    private int  page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serials_layout,null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.serialsRecycleView);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        mAdapter = new VideoAdapter(serialList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                page = current_page;
                new LoadSerials().execute();
            }
        });
        new LoadSerials().execute();

        return view;
    }

    private class LoadSerials extends AsyncTask<String, Void, Document>
    {
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
            for(int i=0; i<serialList2.size();i++)
            {
                serialList.add(serialList2.get(i));
            }
            mAdapter.notifyDataSetChanged();
        }
    }


}
