package popups;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;


import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;

/**
 * Created by Andrew on 14.07.2016.
 */
public class FilesPopup extends Activity {

    private String hash, link;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.popup_files);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.8 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        link = getIntent().getStringExtra("link");
        if(link == null) throw new IllegalArgumentException("Link must be provided");
        hash = link.substring(link.lastIndexOf("/")+1, link.indexOf("-"));
        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("entry.getFiles"),
                new String[] { link, hash, "0" }
        );

        new LoadFiles(url).execute();
    }

    private class LoadFiles extends AsyncTask<String, Void, Document>
    {
        private String url;
        public LoadFiles(String url)
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
            new PageParser(document).getFiles();
        }
    }
}
