package popups;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import models.CommentItem;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentsPopup extends Activity{

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
        String link = getIntent().getStringExtra("link");
        if(link == null) throw new IllegalArgumentException("Link must be provided");
        hash = link.substring(link.lastIndexOf("/")+1, link.indexOf("-"));
        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("entry.getComments"),
                new Object[] { hash, loadedComments }
        );

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
            comments.addAll(new PageParser(document).getComments());
        }
    }

}
