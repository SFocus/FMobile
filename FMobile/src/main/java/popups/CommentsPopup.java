package popups;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.RecyclerBindingAdapter;
import helpers.EndlessRecyclerOnScrollListener;
import models.CommentItem;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentsPopup extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerBindingAdapter mAdapter;
    private Integer page = 0;
    private CommentItem commentItem;
    private int loadedComments = 0;
    private String hash, link;
    private ArrayList<CommentItem> comments = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        Bundle bundle = getArguments();
        link = bundle.getString("link");
        if (link == null) throw new IllegalArgumentException("Link must be provided");
        hash = link.substring(link.lastIndexOf("/") + 1, link.indexOf("-"));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.popup_recycle_view);
        mAdapter = new RecyclerBindingAdapter<>(R.layout.comments_card, com.androidbelieve.drawerwithswipetabs.BR.model, comments);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore() {
                String url = QueryBuilder.buildQuery(
                        DataSource.getUrl("entry.getComments"),
                        new Object[]{hash, loadedComments}
                );
                new LoadComments(url).execute();
            }
        });

        if (state != null && state.containsKey("recyclerData")) {
            ArrayList<CommentItem> temp = state.getParcelableArrayList("recyclerData");
            comments.addAll(temp);
            mAdapter.notifyDataSetChanged();
        } else {
            String url = QueryBuilder.buildQuery(
                    DataSource.getUrl("entry.getComments"),
                    new Object[]{hash, loadedComments}
            );

            new LoadComments(url).execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.popup_comments, null);

        return view;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("recyclerData", comments);
    }

    private class LoadComments extends AsyncTask<String, Void, Document> {
        private String url;

        public LoadComments(String url) {
            this.url = url;
        }

        @Override
        protected Document doInBackground(String... strings) {
            return DataSource.executeQuery(this.url);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            List<CommentItem> temp = new PageParser(document).getComments();
            loadedComments += temp.size();
            comments.addAll(temp);
            mAdapter.notifyDataSetChanged();
        }
    }
}
