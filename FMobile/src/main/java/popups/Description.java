package popups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;

import java.util.ArrayList;

import models.SearchItem;
import models.VideoEntry;

/**
 * Created by svyatoslav on 31.07.16.
 */
public class Description extends Fragment {

    private VideoEntry entry;

    private ArrayList<SearchItem> searchResult = new ArrayList<>();

    private String intentAction, link, url;

    private TextView year, names, genre, country, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        VideoEntry entry = (VideoEntry) bundle.getSerializable("entry");
        year = (TextView) view.findViewById(R.id.entry_year);
        names = (TextView) view.findViewById(R.id.entry_video_name);

        genre = (TextView) view.findViewById(R.id.entry_genre);
        country = (TextView) view.findViewById(R.id.entry_county);
        description = (TextView) view.findViewById(R.id.entry_description);


        assert entry != null;
        names.setText(entry.getName());

        String text;
        text = "Жанр: " + entry.getGenres(VideoEntry.SPACE_SEPARATOR);
        genre.setText(text);

        text = "Страна: " + entry.getCountries(VideoEntry.COMMA_SEPARATOR);
        country.setText(text);

        description.setText(entry.getDescription());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_description, null);

        return view;
    }
}
