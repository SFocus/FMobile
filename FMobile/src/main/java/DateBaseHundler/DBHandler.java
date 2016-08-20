package DateBaseHundler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.Favorites;

/**
 * Created by Focus on 19.08.16.
 */

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATEBASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FmobileBase";
    // Table name
    private static final String TABLE_FAVORITES = "Favorites";

    // Favorites table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATEBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_URL + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(sqLiteDatabase);
    }

    // Put new favorite to database
    public void addNewURL(Favorites favorites) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_URL, favorites.getUrl());
        database.insert(TABLE_FAVORITES, null, values);
        database.close();
    }

    // Get all favorite url
    public List<Favorites> getAllURL() {
        List<Favorites> favoritesList = new ArrayList<>();

        // Select all data
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Favorites favorites = new Favorites();
                favorites.setId(Integer.parseInt(cursor.getString(0)));
                favorites.setUrl(cursor.getString(1));
                // Adding url to list
                favoritesList.add(favorites);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return favoritesList;
    }

    // Check is favorite item in base
    public boolean checkItem(String url) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_FAVORITES, new String[]{KEY_ID, KEY_URL},
                KEY_URL + "=?", new String[]{String.valueOf(url)}, null, null, null, null);
        Log.d("cursor", cursor.getCount() + "");
        if (cursor.getCount() != 0)
            return true;
        cursor.close();
        database.close();
        return false;
    }

    // Delete favorite item
    public void deleteItem(Favorites favorites) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_FAVORITES, KEY_URL + " = ?",
                new String[]{String.valueOf(favorites.getUrl())});
        database.close();
    }
}
