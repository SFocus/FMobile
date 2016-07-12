package popups;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.androidbelieve.drawerwithswipetabs.R;

import java.util.ArrayList;
import java.util.List;

import models.CommentItem;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentsPopup extends Activity{
    private List<CommentItem> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    private int page = 0;
    private CommentItem commentItem;
    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        setContentView(R.layout.popup_comments);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.8 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.6f; // уровень затемнения от 1.0 до 0.0
        getWindow().setAttributes(lp);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.popup_recycle_view);
        mAdapter = new CommentsAdapter(commentList, getBaseContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }

 /*   private class LoadComments extends AsyncTask<String, Void, Document>
    {
        @Override
        protected Document doInBackground(String... strings) {
        //    String url = QueryBuilder.buildQuery(DataSource.getUrl("entry.getComments"));
            return DataSource.executeQuery(url);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            commentList.clear();
          //  filmList2 = new PageParser(document).getFilms();
         //   filmList.addAll(filmList2);
            mAdapter.notifyDataSetChanged();
        }
    }  */

}
