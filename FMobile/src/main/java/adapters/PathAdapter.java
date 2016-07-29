package adapters;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import helpers.SimpleAsyncLoader;
import models.PathNode;
import popups.FilesPopup;

/**
 * Created by Andrew on 17.07.2016.
 */
public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {

    private List<PathNode> nodes;

    public PathAdapter(List<PathNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.path_node_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PathNode node = nodes.get(position);
        holder.nodeName.setText(node.name);
        holder.icon.setTypeface(holder.font);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = QueryBuilder.buildQuery(
                        DataSource.getUrl("entry.getFiles"),
                        new String[] { FilesPopup.link, FilesPopup.hash, node.value }
                );
                int size = nodes.size();
                if(position != size - 1)
                {
                    List<PathNode> temp = new ArrayList<PathNode>(nodes.subList(0, position + 1));
                    nodes.clear();
                    nodes.addAll(temp);
                }
                PathAdapter.this.notifyDataSetChanged();
                new LoadFiles().execute(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nodeName, icon;
        public Typeface font;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fontawesome-webfont.ttf");
            nodeName = (TextView) itemView.findViewById(R.id.node_name);
            icon = (TextView) itemView.findViewById(R.id.fa_chevron_right);

            card = (CardView) itemView;
        }
    }

    public class LoadFiles extends SimpleAsyncLoader
    {
        @Override
        protected void onPostExecute(Document document) {
            FilesPopup.filesList.clear();
            FilesPopup.filesList.addAll(new PageParser(document).getFiles());
            FilesPopup.filesAdapter.notifyDataSetChanged();
        }
    }
}
