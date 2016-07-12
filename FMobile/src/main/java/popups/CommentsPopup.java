package popups;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import models.CommentItem;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentsPopup extends Activity{
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    private int page = 0;
    private CommentItem commentItem;

    private int loadedComments = 0;
    private String hash;
    private ArrayList<CommentItem> comments = new ArrayList<>();

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        setContentView(R.layout.popup_comments);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.8 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        Intent intent = getIntent();

        String link = intent.getStringExtra("link");
        if(link == null) throw new IllegalArgumentException("Link must be provided");
        hash = link.substring(link.lastIndexOf("/")+1, link.indexOf("-"));
        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("entry.getComments"),
                new Object[] { hash, loadedComments }
        );


        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.6f; // уровень затемнения от 1.0 до 0.0
        getWindow().setAttributes(lp);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.popup_recycle_view);
        mAdapter = new CommentsAdapter(comments, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        new LoadComments(url).execute();
    }

    private class LoadComments extends AsyncTask<String, Void, Document>
    {
        private String url;
        public LoadComments(String url)
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
            List<CommentItem> temp = new PageParser(document).getComments();
            comments.addAll(temp);
            mAdapter.notifyDataSetChanged();
        }
    }


}
