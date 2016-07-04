package adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.ImageLoader;
import com.androidbelieve.drawerwithswipetabs.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import models.VideoItem;

/**
 * Created by Focus on 04.07.2016.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private List<VideoItem> filmList;
    private ImageLoader loader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  filmName, countryName, positiveVote, negativeVote, positiveVoteIcon, negativeVoteIcon, quality;
        public ImageView poster;
        Typeface font;
        public MyViewHolder(View view) {
            super(view);
            font = Typeface.createFromAsset( view.getContext().getAssets(), "fontawesome-webfont.ttf" );
            poster = (ImageView) view.findViewById(R.id.poster);
            filmName = (TextView) view.findViewById(R.id.film_name);
            countryName = (TextView) view.findViewById(R.id.countryName);
            positiveVote = (TextView) view.findViewById(R.id.count_positive);
            negativeVote = (TextView) view.findViewById(R.id.count_negative);
            positiveVoteIcon = (TextView) view.findViewById(R.id.vote_positive);
            negativeVoteIcon = (TextView) view.findViewById(R.id.vote_negative);
        }
    }

    public VideoAdapter(List<VideoItem> filmList) {
        this.filmList = filmList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VideoItem movie = filmList.get(position);
        new LoadCarPic(holder.poster).execute("http:"+movie.getPoster());
        holder.positiveVoteIcon.setTypeface(holder.font);
        holder.negativeVoteIcon.setTypeface(holder.font);
        holder.filmName.setText(movie.getFilmName());
        holder.countryName.setText(movie.getCountryName());
        holder.positiveVote.setText(movie.getPositiveVote());
        holder.negativeVote.setText(movie.getNegativeVote());
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }
    class LoadCarPic extends AsyncTask<String, String, Bitmap>
    {

        private final ImageView imageView;
        public LoadCarPic(ImageView view)
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
