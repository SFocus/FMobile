package popups;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
public class FilesPopup extends Fragment {

    public static String hash, link;
    public static ArrayList<FilesItem> filesList = new ArrayList<>();
    public static ArrayList<PathNode> path = new ArrayList<>();

    public static FilesAdapter filesAdapter;
    public static PathAdapter pathAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_files, null);

        filesList.clear();
        path.clear();
        path.add(new PathNode("Файлы и папки", "0"));


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        link = bundle.getString("link");

        if (link == null) throw new IllegalArgumentException("Link must be provided");

        hash = link.substring(link.lastIndexOf("/") + 1, link.indexOf("-"));
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.files_content);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView pathRecycler = (RecyclerView) getActivity().findViewById(R.id.path_recycler);
        pathRecycler.setLayoutManager(layoutManager);


        filesAdapter = new FilesAdapter(filesList, getContext());
        pathAdapter = new PathAdapter(path);

        recyclerView.setAdapter(filesAdapter);
        pathRecycler.setAdapter(pathAdapter);
        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("entry.getFiles"),
                new String[] { link, hash, "0" }
        );
        new LoadFiles(url).execute();
        Log.d("dfdf", "do it");
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
