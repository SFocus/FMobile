package WebParser;

import android.util.Log;

import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.CommentItem;
import models.FilesItem;
import models.SearchItem;
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
                //**IGNORE**
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

    public List<SearchItem> getDetailedSearch()
    {
        List<SearchItem> out = new ArrayList<>();

        Elements elements = document.body().select(SearchItem.SEARCH_ITEM_SELECTOR);

        try {
            for(Element row : elements)
            {
                String image = row.select(SearchItem.SEARCH_ITEM_IMAGE_SELECTOR).attr("src");
                String title = row.select(SearchItem.SEARCH_ITEM_TITLE_SELECTOR).text();
                String type = row.select(SearchItem.SEARCH_ITEM_TYPE_SELECTOR).text();
                String genres = row.select(SearchItem.SEARCH_ITEM_GENRES_SELECTOR).text();
                String posVotes = row.select(SearchItem.SEARCH_ITEM_POSITIVE_VOTES_SELECTOR).text();
                String negVotes = row.select(SearchItem.SEARCH_ITEM_NEGATIVE_VOTES_SELECTOR).text();
                String desc = row.select(SearchItem.SEARCH_ITEM_DESCRIPTION_SELECTOR).text();
                String link = row.attr("href");
                out.add(new SearchItem(image, title, type, genres, posVotes, negVotes, desc, link));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return out;
        }
        return out;
    }

    public List<CommentItem> getComments()
    {
        List<CommentItem> out = new ArrayList<>();

        Elements elements = document.body().select(CommentItem.COMMENT_ITEM_SELECTOR);

        try {
            for(Element row : elements)
            {
                String image = row.select(CommentItem.COMMENT_AUTHOR_IMAGE_SELECTOR).attr("style");
                image = image.substring(23, image.length()-3);
                String name = row.select(CommentItem.COMMENT_AUTHOR_NAME_SELECTOR).text();
                String time = row.select(CommentItem.COMMENT_TIME_SELECTOR).text();
                String vote_yes = row.select(CommentItem.COMMENT_VOTE_YES_SELECTOR).text();
                String vote_no = row.select(CommentItem.COMMENT_VOTE_NO_SELECTOR).text();
                String text = row.select(CommentItem.COMMENT_TEXT_SELECTOR).text();
                out.add(new CommentItem(name, image, time, vote_yes, vote_no, text));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return out;
        }
        return out;
    }

    public List<FilesItem> getFiles()
    {
        List<FilesItem> out = new ArrayList<>();

        Elements folders    = document.body().select(FilesItem.FOLDER_SELECTOR);
        Elements files      = document.body().select(FilesItem.FILE_SELECTOR);

        try {
            if(folders.size() > 0)
            {
                for(Element row : folders)
                {
                    String title = row.select(FilesItem.FOLDER_TITLE_SELECTOR).text();
                    String parent = row.select(FilesItem.FOLDER_TITLE_SELECTOR).attr("rel");
                    String details = row.select(FilesItem.FOLDER_DETAILS_SELECTOR).text();
                    String date = row.select(FilesItem.FOLDER_DATE_SELECTOR).text();

                    parent = parent.substring(parent.indexOf('\'') + 1);
                    parent = parent.substring(0, parent.indexOf('\''));
                    out.add(new FilesItem(title, details, date, parent));
                }
            }
            else
            {
                for(Element row : files)
                {
                    Log.d("ROW", row.html());
                    String quality = row.select(FilesItem.FILE_QUALITY_SELECTOR).text();
                    String fileName = row.select(FilesItem.FILE_NAME_SELECTOR).text();
                    String link = row.select(FilesItem.FILE_NAME_SELECTOR).attr("href");
                    String size = row.select(FilesItem.FILE_SIZE_SELECTOR).text();
                    String download = row.select(FilesItem.FILE_SIZE_SELECTOR).attr("href");
                    out.add(new FilesItem(quality, fileName, size, link, download));
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            return out;
        }

        return out;
    }

    public String getVideoLink()
    {
        return document.body().html();
    }
}
