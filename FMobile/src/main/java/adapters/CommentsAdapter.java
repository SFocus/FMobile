package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import helpers.AsyncPhotoLoader;
import models.CommentItem;


/**
 * Created by Focus on 12.07.2016.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    private Context context;
    private List<CommentItem> commentItems;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    private int maxLinesToShow = 3;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, time;
        public TextView text;
        public ImageView image;
        Button show, hide;
        TextView descText;
        Typeface font;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.comment_image);
            id = (TextView) view.findViewById(R.id.comment_id);
            time = (TextView) view.findViewById(R.id.comment_time);
            show = (Button) view.findViewById(R.id.show);
            hide = (Button) view.findViewById(R.id.hide);
            descText = (TextView) view.findViewById(R.id.description_text);
            font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
            cardView = (CardView) view.findViewById(R.id.fa_comments_card);
        }
    }

    public CommentsAdapter(List<CommentItem> commentItems, Context context) {
        this.commentItems = commentItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CommentItem comment = commentItems.get(position);
        new AsyncPhotoLoader(holder.image).execute("http:" + comment.getImage());
        holder.id.setText(comment.getAuthor());
        holder.time.setText(comment.getTime());
        holder.descText.setText(comment.getText());
        holder.hide.setTypeface(holder.font);
        holder.show.setTypeface(holder.font);
        holder.show.setVisibility(View.VISIBLE);
        if (!comment.newlyLoaded) {
            YoYo.with(Techniques.FadeInLeft).playOn(holder.cardView);
            comment.newlyLoaded = true;
        }
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.show.setVisibility(View.INVISIBLE);
                holder.hide.setVisibility(View.VISIBLE);
                holder.descText.setMaxLines(Integer.MAX_VALUE);
            }
        });
        holder.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.hide.setVisibility(View.INVISIBLE);
                holder.show.setVisibility(View.VISIBLE);
                holder.descText.setMaxLines(maxLinesToShow);
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }
}

