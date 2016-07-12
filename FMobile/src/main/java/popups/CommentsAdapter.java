package popups;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import models.CommentItem;


/**
 * Created by Focus on 12.07.2016.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    private Context context;
    private List<CommentItem> commentItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, time;
        public ExpandableTextView text;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rootView = inflater.inflate(R.layout.comments_card, null);

            image = (ImageView) view.findViewById(R.id.comment_image);
            id = (TextView) view.findViewById(R.id.comment_id);
            time = (TextView) view.findViewById(R.id.comment_time);
            text = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentItem comment = commentItems.get(position);
        new LoadIcon(holder.image).execute("http:" + comment.getImage());
        holder.id.setText(comment.getAuthor());
        holder.time.setText(comment.getTime());
        holder.text.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class LoadIcon extends AsyncTask<String, String, Bitmap> {

        private final ImageView imageView;

        public LoadIcon(ImageView view) {
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
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}

