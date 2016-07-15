package popups;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;


import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;

/**
 * Created by Andrew on 14.07.2016.
 */
public class FilesPopup extends Activity {

    private String hash, link;
    private static final int FIRST_LEVEL_COUNT = 6;
    private static final int SECOND_LEVEL_COUNT = 4;
    private static final int THIRD_LEVEL_COUNT = 20;
    private ExpandableListView expandableListView;

    private Typeface font;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.popup_files);
        font = Typeface.createFromAsset(this.getAssets(), "fontawesome-webfont.ttf");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.8 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        link = getIntent().getStringExtra("link");
        if(link == null) throw new IllegalArgumentException("Link must be provided");
        hash = link.substring(link.lastIndexOf("/")+1, link.indexOf("-"));
        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("entry.getFiles"),
                new String[] { link, hash, "0" }
        );

        new LoadFiles(url).execute();

        expandableListView = (ExpandableListView) findViewById(R.id.mainList);
        expandableListView.setAdapter(new ParentLevel(this));
    }

    private class LoadFiles extends AsyncTask<String, Void, Document>
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
            new PageParser(document).getFiles();
        }
    }

    public class ParentLevel extends BaseExpandableListAdapter {

        private Context context;

        public ParentLevel(Context context) {
            this.context = context;
        }

        @Override
        public Object getChild(int arg0, int arg1) {
            return arg1;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(FilesPopup.this);
            secondLevelELV.setAdapter(new SecondLevelAdapter(context));
            secondLevelELV.setGroupIndicator(null);
            return secondLevelELV;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return SECOND_LEVEL_COUNT;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return FIRST_LEVEL_COUNT;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.filespopup_folders_fragment, null);
                TextView icon= (TextView) convertView.findViewById(R.id.folder_icon);
                TextView details = (TextView) convertView.findViewById(R.id.folder_details);
                TextView name = (TextView) convertView.findViewById(R.id.folder_name);
                icon.setTypeface(font);
                name.setText("FIRST LEVEL");
                details.setText("34 files");
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class SecondLevelExpandableListView extends ExpandableListView {

        public SecondLevelExpandableListView(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    public class SecondLevelAdapter extends BaseExpandableListAdapter {

        private Context context;

        public SecondLevelAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.entry_content_fragment, null);
                TextView text = (TextView) convertView.findViewById(R.id.folder_name2);
                text.setTypeface(font);
            }
            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.entry_content_fragment, null);
                TextView text = (TextView) convertView.findViewById(R.id.icon);
                text.setText("THIRD LEVEL");
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return THIRD_LEVEL_COUNT;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
