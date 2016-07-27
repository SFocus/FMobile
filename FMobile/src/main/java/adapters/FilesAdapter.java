package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.androidbelieve.drawerwithswipetabs.VideoPlayerActivity;

import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.zip.Inflater;

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
    private Context parentCtx;

    public FilesAdapter(ArrayList<FilesItem> content, Context ctx) {
        this.content = content;
        this.context = ctx;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentCtx = parent.getContext();
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
                //FolderViewHolder.. folderHolder... folldHooldr...Hoolrd...Hodoor..HODOOR!!11
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
                        FilesPopup.path.add(new PathNode(item.getTitle(), item.getParam()));
                        FilesPopup.pathAdapter.notifyDataSetChanged();
                        new LoadFiles(url).execute();
                    }
                });
                break;
            case 1:
                //FileViewHolder.. fileHolder... filldeHooldr...Hoolrd...Hodoor..HODOOR!!11
                final FileViewHolder fileHolder = (FileViewHolder) holder;
                final String[] qualities = item.getQuality().split("\\s");
                ArrayAdapter<String> qListAdapter = new ArrayAdapter<>(context, R.layout.spinner_quality_item, R.id.quality_text, qualities) ;
                fileHolder.fileName.setText(item.getFileName());
                fileHolder.seriesNum.setText(item.getSeriesNum());
                fileHolder.size.setText(item.getSize());
                fileHolder.iconPlay.setTypeface(fileHolder.font);
                fileHolder.iconDownload.setTypeface(fileHolder.font);
                fileHolder.qList.setAdapter(qListAdapter);
                fileHolder.qList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        FilesItem.AdditionalQualityInfo quality = item.qualities.get(qualities[position]);
                        fileHolder.fileName.setText(quality.getFileName());
                        fileHolder.size.setText(quality.getSize());
                        item.setDownloadLink(quality.getDownloadLink());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                fileHolder.iconDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: do download here
                    }
                });

                fileHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param = item.getDownloadLink().substring(8,item.getDownloadLink().lastIndexOf("/"));
                        String url = QueryBuilder.buildQuery(
                                DataSource.getUrl("entry.getVideoLink"),
                                param
                        );
                        new LoadLink(url).execute();
                    }
                });
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

        public TextView fileName, seriesNum, size, iconPlay, iconDownload;
        public CardView card;
        public Typeface font;
        public Spinner qList;

        public FileViewHolder(View itemView) {
            super(itemView);
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fontawesome-webfont.ttf");
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            seriesNum = (TextView) itemView.findViewById(R.id.seriesNum);
            size = (TextView) itemView.findViewById(R.id.fileSize);
            iconPlay = (TextView) itemView.findViewById(R.id.icon_play);
            iconDownload = (TextView) itemView.findViewById(R.id.icon_download);
            qList = (Spinner) itemView.findViewById(R.id.qualities_list);




            card = (CardView) itemView;
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

    private class LoadLink extends AsyncTask<String, Void, Document>
    {
        private String url;
        public LoadLink(String url)
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
            try
            {
                String directLink = new JSONObject(document.body().html()).getString("link");
                Log.d("directLink", directLink);
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("link", directLink);
                context.startActivity(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


//
        }
    }
}
