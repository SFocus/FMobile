package adapters;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Andrew on 08.07.2016.
 */
public class FSSuggestionAdapter extends CursorAdapter {

    private static MatrixCursor cursor = new MatrixCursor( new String[] {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA,
            SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
    });




    public FSSuggestionAdapter(Context context) {
        super(context, cursor, false);
    }

    public FSSuggestionAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("newVIEW", cursor.getString(0));
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("bindView", "view");

    }
}
