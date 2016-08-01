package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.EntryScroll;
import com.androidbelieve.drawerwithswipetabs.R;
import com.bumptech.glide.Glide;

import java.util.List;

import models.VideoEntry;

/**
 * Created by Andrew on 01.08.2016.
 */
public class SimilarItemsAdapter extends RecyclerView.Adapter<SimilarItemsAdapter.ViewHolder> {


    private List<VideoEntry.SimilarItem> items;
    private Context context;

    public SimilarItemsAdapter(List<VideoEntry.SimilarItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoEntry.SimilarItem item = items.get(position);
        holder.title.setText(item.getTitle());
        Glide.with(holder.poster.getContext()).load("http:" + item.getImage()).into(holder.poster);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SimilarItemsAdapter.this.context, EntryScroll.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("link",item.getLink());
                SimilarItemsAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView poster;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            card = (CardView) itemView;
        }
    }
}
