package WebParser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.VideoEntry;
import models.VideoItem;

/**
 * Created by Andrew on 04.07.2016.
 */
public class PageParser {

    /**
     * Holds entire page
     */
    private Document document;

    private static final String FILM_ITEM = ".b-poster-tile";

    public PageParser(Document document) {
        this.document = document;
    }

    /**
     * @return Returns array of films
     */
    public ArrayList<VideoItem> getFilms()
    {
        ArrayList<VideoItem> films = new ArrayList<VideoItem>();
        Elements elements = document.body().select(FILM_ITEM);
        try
        {
            for(Element element : elements)
            {
                String quality = "";
                String name = element.select(VideoItem.VIDEO_FULL_NAME_SELECTOR).text();
                String image = element.select(VideoItem.VIDEO_IMG_SELECTOR).attr("src");
                String link = element.select(VideoItem.VIDEO_LINK_SELECTOR).attr("href");
                String country = element.select(VideoItem.VIDEO_COUNTRY_SELECTOR).text();
                String positive = element.select(VideoItem.VIDEO_POSITIVE_VOTES_SELECTOR).text();
                String negative = element.select(VideoItem.VIDEO_NEGATIVE_VOTES_SELECTOR).text();
                Elements qualityEl = element.select(VideoItem.VIDEO_QUALITY_SELECTOR);
                if(!qualityEl.isEmpty())
                {
                    quality = qualityEl.first().className();
                }
                films.add(new VideoItem(image, name, country, positive, negative, quality, link));
            }
        }catch (Exception e)
        {
            return null;
        }

        return films;
    }

    public VideoEntry getEntry()
    {
        VideoEntry entry;
        String[] selectors = {
                VideoEntry.ENTRY_GENRES_SELECTOR,
                VideoEntry.ENTRY_YEAR_SELECTOR,
                VideoEntry.ENTRY_COUNTRY_SELECTOR,
                VideoEntry.ENTRY_DIRECTOS_SELECTOR,
                VideoEntry.ENTRY_CASTS_SELECTOR,
        };
        String[] infoKeys = {
                "genre",
                "year",
                "country",
                "director",
                "cast"
        };
        int iterator = 0;
        Map<String, ArrayList<String>> info = new HashMap<>();

        try
        {
            Elements cells = document.body().select(VideoEntry.ENTRY_INFO_CELL_SELECTOR);
            try {
                for(Element cell : cells)
                {
                    ArrayList<String> list = new ArrayList<String>();
                    for(Element t : cell.select(selectors[iterator]))
                    {
                        list.add(t.text());
                    }
                    info.put(infoKeys[iterator], list);
                    iterator++;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            Elements imgWrappers = document.body().select(VideoEntry.ENTRY_IMAGES_SET_SELECTOR);
            ArrayList<String> gallery = new ArrayList<>();
            for(Element wrapper : imgWrappers )
            {
                gallery.add(wrapper.attr("rel"));
            }
            info.put("gallery", gallery);

            String name = document.body().select(VideoEntry.ENTRY_NAME_SELECTOR).text();
            String altName = document.body().select(VideoEntry.ENTRY_NAME_ALT_SELECTOR).text();
            String description = document.body().select(VideoEntry.ENTRY_DESCRIPTION_SELECTOR).text();
            String positiveVotes = document.body().select(VideoEntry.ENTRY_POSITIVE_VOTES_SELECTOR).text();
            String negativeVotes = document.body().select(VideoEntry.ENTRY_NEGATIVE_VOTES_SELECTOR).text();

            entry = new VideoEntry(
                    info.get("genre"),
                    info.get("year"),
                    info.get("country"),
                    info.get("director"),
                    info.get("cast"),
                    info.get("gallery"),
                    name,
                    altName,
                    positiveVotes,
                    negativeVotes,
                    description
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return entry;
    }

    public JSONArray getSearch()
    {
        try {
            String text = this.document.body().text();
            return new JSONArray(text);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
