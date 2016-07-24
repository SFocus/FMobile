package popups;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.androidbelieve.drawerwithswipetabs.R;
import org.jsoup.nodes.Document;


import java.util.ArrayList;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.FilesAdapter;
import adapters.PathAdapter;
import models.FilesItem;
import models.PathNode;

/**
 * Created by Andrew on 14.07.2016.
 */
public class FilesPopup extends Activity {

    public static String hash, link;
    public static ArrayList<FilesItem> filesList = new ArrayList<>();
    public static ArrayList<PathNode> path = new ArrayList<>();

    public static FilesAdapter filesAdapter;
    public static PathAdapter pathAdapter;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.popup_files);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.9 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        link = getIntent().getStringExtra("link");
        if(link == null) throw new IllegalArgumentException("Link must be provided");

        hash = link.substring(link.lastIndexOf("/")+1, link.indexOf("-"));

        filesList.clear();
        path.clear();
        path.add(new PathNode("Файлы и папки", "0"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.files_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView pathRecycler = (RecyclerView) findViewById(R.id.path_recycler);
        pathRecycler.setLayoutManager(layoutManager);


        filesAdapter = new FilesAdapter(filesList, this);
        pathAdapter = new PathAdapter(path);

        recyclerView.setAdapter(filesAdapter);
        pathRecycler.setAdapter(pathAdapter);

        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("entry.getFiles"),
                new String[] { link, hash, "0" }
        );

        new LoadFiles(url).execute();

    }

    public class LoadFiles extends AsyncTask<String, Void, Document>
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
            filesList.clear();
            filesList.addAll(new PageParser(document).getFiles());
            filesAdapter.notifyDataSetChanged();
        }
    }
}
