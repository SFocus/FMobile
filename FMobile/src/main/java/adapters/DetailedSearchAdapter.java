package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import entryTab.Content;
import models.SearchItem;

/**
 * Created by Andrew on 11.07.2016.
 */
public class DetailedSearchAdapter extends RecyclerView.Adapter<DetailedSearchAdapter.SearchItemHolder> {

    private List<SearchItem> entries;
    private Context context;

    public DetailedSearchAdapter(List<SearchItem> entries, Context context)
    {
        this.entries = entries;
        this.context = context;
    }

    public class SearchItemHolder extends RecyclerView.ViewHolder
    {
        public TextView title, type, genre, positive, negative, pos_count, neg_count, desc;
        public ImageView poster;
        public Typeface font;

        public SearchItemHolder(View itemView) {
            super(itemView);
            font = Typeface.createFromAsset( itemView.getContext().getAssets(), "fontawesome-webfont.ttf" );
            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            genre = (TextView) itemView.findViewById(R.id.genre);
            positive = (TextView) itemView.findViewById(R.id.fa_positive);
            negative = (TextView) itemView.findViewById(R.id.fa_negative);
            pos_count = (TextView) itemView.findViewById(R.id.positive_count);
            neg_count = (TextView) itemView.findViewById(R.id.negative_count);
            desc = (TextView) itemView.findViewById(R.id.description);

        }
    }

    @Override
    public DetailedSearchAdapter.SearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_card, parent, false);
        return new SearchItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailedSearchAdapter.SearchItemHolder holder, int position) {
        final SearchItem entry = entries.get(position);

        holder.title.setText(entry.getTitle());
        holder.type.setText(entry.getType());
        holder.genre.setText(entry.getGenres());

        holder.positive.setTypeface(holder.font);
        holder.negative.setTypeface(holder.font);

        holder.pos_count.setText(entry.getPositiveVotes());
        holder.neg_count.setText(entry.getNegativeVotes());

        holder.desc.setText(entry.getDescription());
        new LoadPic(holder.poster).execute("http:"+entry.getImage());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    class LoadPic extends AsyncTask<String, String, Bitmap>
    {

        private final ImageView imageView;
        public LoadPic(ImageView view)
        {
            this.imageView = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(params[0]).getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if(bitmap != null)
            {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
