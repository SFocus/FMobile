package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.EntryScroll;
import com.androidbelieve.drawerwithswipetabs.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import helpers.AsyncPhotoLoader;
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

        holder.descText.setText(entry.getDescription());
        new AsyncPhotoLoader(holder.poster).execute("http:"+entry.getImage());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedSearchAdapter.this.context, EntryScroll.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("link",entry.getLink());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                DetailedSearchAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class SearchItemHolder extends RecyclerView.ViewHolder {
        public TextView title, type, genre, positive, negative, pos_count, neg_count;
        public ExpandableTextView descText;
        public CardView card;
        public ImageView poster;
        public Typeface font;

        public SearchItemHolder(View itemView) {
            super(itemView);
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fontawesome-webfont.ttf");
            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            genre = (TextView) itemView.findViewById(R.id.genre);
            positive = (TextView) itemView.findViewById(R.id.fa_positive);
            negative = (TextView) itemView.findViewById(R.id.fa_negative);
            pos_count = (TextView) itemView.findViewById(R.id.positive_count);
            neg_count = (TextView) itemView.findViewById(R.id.negative_count);
            descText = (ExpandableTextView) itemView.findViewById(R.id.description);
            card = (CardView) itemView;

        }
    }
}