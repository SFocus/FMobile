package providers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidbelieve.drawerwithswipetabs.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;

/**
 * Created by Andrew on 08.07.2016.
 */
public class VideoSuggestionsProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String params = uri.getLastPathSegment();
        String url = QueryBuilder.buildQuery(
                DataSource.getUrl("media.search"),
                params
        );

        JSONArray result = new PageParser(DataSource.executeQuery(url)).getSearch();
        MatrixCursor cursor = new MatrixCursor(new String[] {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                SearchManager.SUGGEST_COLUMN_ICON_1,
        });
        String image = "http://www.megaicons.net/static/img/icons_sizes/99/237/32/game-of-thrones-icon.png";
        try
        {
            for(int i = 0; i < result.length(); i++)
            {
                JSONObject row = result.getJSONObject(i);
                Log.d("ROW", row.toString());
                cursor.addRow(new String[] {
                        i+"",
                        row.getString("title"),
                        row.getJSONArray("genres").join(", ").replace("\"", ""),
                        row.getString("link"),
                        row.getString("link"),
                        R.drawable.ic_video + ""
                });
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
