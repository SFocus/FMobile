package popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import WebParser.DataSource;
import WebParser.PageParser;
import WebParser.QueryBuilder;
import adapters.DetailedSearchAdapter;
import helpers.SimpleAsyncLoader;
import models.SearchItem;
import models.VideoEntry;

/**
 * Created by svyatoslav on 31.07.16.
 */
public class Description extends Fragment {

    private VideoEntry entry;

    private ArrayList<SearchItem> searchResult = new ArrayList<>();

    private DetailedSearchAdapter searchAdapter;

    private String intentAction, link, url;

    private TextView year, names, genre, country, description;

    public Description(String link) {
        this.link = link;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_description, null);

        url = QueryBuilder.buildQuery(
                DataSource.getUrl("media.getEntry"),
                link
        );

        year = (TextView) view.findViewById(R.id.entry_year);
        names = (TextView) view.findViewById(R.id.entry_video_name);

        genre = (TextView) view.findViewById(R.id.entry_genre);
        country = (TextView) view.findViewById(R.id.entry_county);
        description = (TextView) view.findViewById(R.id.entry_description);

        new LoadEntry().execute(url);

        return view;
    }


    private class LoadEntry extends SimpleAsyncLoader {
        @Override
        protected void onPostExecute(Document document) {
            Description.this.entry = new PageParser(document).getEntry();

            String text;
            try {
                Log.d("year", entry.getYear(VideoEntry.COMMA_SEPARATOR));
                text = entry.getYear(VideoEntry.SPACE_SEPARATOR);
                year.setText(text);
            } catch (Exception e) {

            }
            names.setText(entry.getName());

            text = "Жанр: " + entry.getGenres(" ");
            genre.setText(text);

            text = "Страна: " + entry.getCountries(VideoEntry.COMMA_SEPARATOR);
            country.setText(text);

            description.setText(entry.getDescription());
        }
    }
}
