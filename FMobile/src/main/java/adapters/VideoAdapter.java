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
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import models.VideoItem;

/**
 * Created by Focus on 04.07.2016.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private List<VideoItem> filmList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView filmName, countryName, positiveVote, negativeVote, positiveVoteIcon, negativeVoteIcon, quality;
        public ImageView poster;
        public CardView card;
        Typeface font;

        public MyViewHolder(final View view) {
            super(view);
            font = Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf");
            poster = (ImageView) view.findViewById(R.id.poster);
            filmName = (TextView) view.findViewById(R.id.film_name);
            countryName = (TextView) view.findViewById(R.id.countryName);
            positiveVote = (TextView) view.findViewById(R.id.count_positive);
            negativeVote = (TextView) view.findViewById(R.id.count_negative);
            positiveVoteIcon = (TextView) view.findViewById(R.id.vote_positive);
            negativeVoteIcon = (TextView) view.findViewById(R.id.vote_negative);
            card = (CardView) view.findViewById(R.id.film_card);
        }
    }

    public VideoAdapter(List<VideoItem> filmList, Context context) {
        this.filmList = filmList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final VideoItem movie = filmList.get(position);
        //  new AsyncPhotoLoader(holder.poster).execute();
        Glide.with(holder.poster.getContext()).load("http:" + movie.getPoster()).into(holder.poster);
        holder.positiveVoteIcon.setTypeface(holder.font);
        holder.negativeVoteIcon.setTypeface(holder.font);
        holder.filmName.setText(movie.getFilmName());
        holder.countryName.setText(movie.getCountryName());
        holder.positiveVote.setText(movie.getPositiveVote());
        holder.negativeVote.setText(movie.getNegativeVote());
        if (!movie.newlyLoaded) {
            YoYo.with(Techniques.BounceIn).playOn(holder.card);
            movie.newlyLoaded = true;
        }

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VideoAdapter.this.context, EntryScroll.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("link", movie.getLink());
                VideoAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }
}
