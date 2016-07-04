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
public class FilmsFragment extends Fragment {
    private List<VideoItem> filmList = new ArrayList<>();
    private List<VideoItem> filmList2 = new ArrayList<>();
    private VideoAdapter mAdapter;
    private int  page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.films_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        mAdapter = new VideoAdapter(filmList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                page = current_page;
                new LoadFilms().execute();
            }
        });

        new LoadFilms().execute();
        return view;
    }

    private class LoadFilms extends AsyncTask<String, Void, Document>
    {
        @Override
        protected Document doInBackground(String... strings) {
            String url = QueryBuilder.buildQuery(DataSource.getUrl("media.getFilms"), page);
            return DataSource.executeQuery(url);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            filmList2.clear();
            filmList2 = new PageParser(document).getFilms();
            for(int i=0; i<filmList2.size();i++)
            {
                filmList.add(filmList2.get(i));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
