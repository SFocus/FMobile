package com.androidbelieve.drawerwithswipetabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import models.FilmItem;

/**
 * Created by Ratan on 7/29/2015.
 */
public class PrimaryFragment extends Fragment {
    private List<FilmItem> filmList = new ArrayList<>();
    private List<FilmItem> filmList2 = new ArrayList<>();
    private FilmsAdapter mAdapter;
    private int  page = 0;
    int totalItemCount, visibleItemCount, firstVisibleItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        mAdapter = new FilmsAdapter(filmList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = llm.getItemCount();
                visibleItemCount = llm.getChildCount();
                firstVisibleItem = llm.findFirstVisibleItemPosition();
                Log.d("totalItemCount",Integer.toString(totalItemCount));
                Log.d("visible",Integer.toString(visibleItemCount));
                Log.d("firstv",Integer.toString(firstVisibleItem));

                    if (totalItemCount-6 == visibleItemCount + firstVisibleItem) {
                        page++;
                        new LoadFilms().execute();
                    }
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
                Log.d("dfddfgg",filmList2.get(i).getLink());
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
