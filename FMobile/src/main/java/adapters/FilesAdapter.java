package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.util.ArrayList;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import models.FilesItem;
import models.PathNode;
import popups.FilesPopup;

/**
 * Created by Andrew on 17.07.2016.
 */
public class FilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FilesItem> content;
    private Context context;

    public FilesAdapter(ArrayList<FilesItem> content, Context ctx) {
        this.content = content;
        this.context = ctx;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case 0: // IN CASE OF FOLDER RETURNS FOLDER LAYOUT
                return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folders_card, parent, false));


            case 1: // IN CASE OF FILE RETURNS FILE LAYOUT
                return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_file_card, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FilesItem item = content.get(position);
        switch ( ( item.isFile ? 1 : 0 ) )
        {
            case 0:
                FolderViewHolder folderHolder = (FolderViewHolder) holder;

                folderHolder.title.setText(item.getTitle());
                folderHolder.details.setText(item.getDetails());
                folderHolder.date.setText(item.getDate());
                folderHolder.icon_folder.setTypeface(folderHolder.font);

                folderHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = QueryBuilder.buildQuery(
                                DataSource.getUrl("entry.getFiles"),
                                new String[] { FilesPopup.link, FilesPopup.hash, item.getParam() }
                        );
                        Log.d("SIEZPATH", FilesPopup.path.size() + "");
                        FilesPopup.path.add(new PathNode(item.getTitle(), item.getParam()));
                        Log.d("SIEZPATH", FilesPopup.path.size() + "");
                        FilesPopup.pathAdapter.notifyDataSetChanged();
                        new LoadFiles(url).execute();
                    }
                });
                break;
            case 1:
                FileViewHolder fileHolder = (FileViewHolder) holder;

                fileHolder.fileName.setText(item.getFileName());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return content.size();
    }

    @Override
    public int getItemViewType(int position) {
        FilesItem item = content.get(position);
        return ( item.isFile ? 1 : 0 );
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView title, icon_folder, details, date;
        public Typeface font;
        public FolderViewHolder(View itemView) {
            super(itemView);
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fontawesome-webfont.ttf");
            title = (TextView) itemView.findViewById(R.id.title);
            details = (TextView) itemView.findViewById(R.id.folder_details);
            icon_folder = (TextView) itemView.findViewById(R.id.fa_folder);
            date = (TextView) itemView.findViewById(R.id.folder_date);

            card = (CardView) itemView;
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {

        public TextView fileName;

        public FileViewHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.fileName);
        }
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
            content.clear();
            content.addAll(new PageParser(document).getFiles());
            FilesPopup.filesAdapter.notifyDataSetChanged();
        }
    }
}
