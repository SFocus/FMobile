package com.androidbelieve.drawerwithswipetabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import DateBaseHundler.DBHandler;
import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.RecyclerBindingAdapter;
import helpers.EndlessRecyclerOnScrollListener;
import helpers.GridAutofitLayoutManager;
import helpers.SimpleAsyncLoader;
import models.Favorites;
import models.VideoEntry;
import models.VideoItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private static final float GESTURE_THRESHOLD_DP = 170.0f;
    ArrayList<VideoItem> list = new ArrayList<>();
    private RecyclerBindingAdapter mAdapter;
    private Integer page = 0;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private VideoEntry entry;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        final DBHandler db = new DBHandler(getContext());
        List<Favorites> urlList = db.getAllURL();
        for (int i = 0; i < urlList.size(); i++) {
            String link = urlList.get(i).getUrl().replace("http://fs.to", "");
            String url = QueryBuilder.buildQuery(
                    DataSource.getUrl("media.getEntry"),
                    link
            );
            new LoadEntry(link).execute(url);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        int mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
        final GridAutofitLayoutManager llm = new GridAutofitLayoutManager(view.getContext(), mGestureThreshold);
        recyclerView.setLayoutManager(llm);
        mAdapter = new RecyclerBindingAdapter<>(R.layout.film_card, com.androidbelieve.drawerwithswipetabs.BR.video, list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return view;
    }

    private class LoadEntry extends SimpleAsyncLoader {
        String url;

        public LoadEntry(String url) {
            this.url = url;
        }

        @Override
        protected void onPostExecute(Document document) {
            FavoriteFragment.this.entry = new PageParser(document).getEntry();
            list.add(new VideoItem(entry.getImages().get(0),
                    entry.getName(), entry.getCountries(" "),
                    entry.getPositiveVotes(), entry.getNegativeVotes(),
                    "", url));
            mAdapter.notifyDataSetChanged();
        }
    }

}
