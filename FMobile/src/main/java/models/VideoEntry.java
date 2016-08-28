package models;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.androidbelieve.drawerwithswipetabs.EntryScroll;

import org.jsoup.helper.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew on 05.07.2016.
 */
public class VideoEntry implements Serializable {

    /**
     * Entry information selectors
     */
    public static final String ENTRY_NAME_SELECTOR = ".b-tab-item__title-inner span";
    public static final String ENTRY_NAME_ALT_SELECTOR = ".b-tab-item__title-inner div";
    public static final String ENTRY_IMAGES_SET_SELECTOR = "a.item[rel]";
    public static final String ENTRY_DESCRIPTION_SELECTOR = ".item-decription";
    public static final String ENTRY_POSITIVE_VOTES_SELECTOR = ".m-tab-item__vote-value_type_yes";
    public static final String ENTRY_NEGATIVE_VOTES_SELECTOR = ".m-tab-item__vote-value_type_no";

    public static final String ENTRY_GENRES_SELECTOR = "span > a";
    public static final String ENTRY_YEAR_SELECTOR = "a span";
    public static final String ENTRY_COUNTRY_SELECTOR = "a";
    public static final String ENTRY_DIRECTOS_SELECTOR = "span > a";
    public static final String ENTRY_CASTS_SELECTOR = "span > a";
    public static final String ENTRY_STATUS_SELECTOR = "*";
    public static final String ENTRY_DURATION_SELECTOR = "td > * > span";


    public static final String ENTRY_INFO_KEYS = ".item-info table tbody tr td:first-child";
    public static final String ENTRY_INFO_VALUES = ".item-info table tbody tr td:last-child";

    public static final String ENTRY_SIMILAR        = ".b-poster-new";
    public static final String ENTRY_SIMILAR_LINK   = ".b-poster-new__link";
    public static final String ENTRY_SIMILAR_IMAGE  = ".b-poster-new__image-poster";
    public static final String ENTRY_SIMILAR_TITLE  = ".m-poster-new__short_title";


    public static final String SPACE_SEPARATOR = " ";
    public static final String COMMA_SEPARATOR = ", ";

    private List<String> genres;
    private List<String> year;
    private List<String> duration;
    private List<String> countries;
    private List<String> directors;
    private List<String> casts;
    private List<String> images;
    private ArrayList<SimilarItem> similarItems;

    private String name;
    private String altName;
    private String positiveVotes;
    private String negativeVotes;
    private String description;
    private Type type;

    public VideoEntry(List<String> genres, List<String> year, List<String> duration,List<String> countries, List<String> directors, List<String> casts, List<String> images, String name, String altName, String positiveVotes, String negativeVotes, String description, ArrayList<SimilarItem> similarItems) {
        this.genres = genres;
        this.year = year;
        this.duration = duration;
        this.countries = countries;
        this.directors = directors;
        this.casts = casts;
        this.images = images;
        this.name = name;
        this.altName = altName;
        this.positiveVotes = positiveVotes;
        this.negativeVotes = negativeVotes;
        this.description = description;
        this.similarItems = similarItems;
    }

    public String getGenres(String separator) {
        return StringUtil.join(genres, separator);
    }

    public String getYear(String separator) {
        return StringUtil.join(year, separator);
    }

    public String getCountries(String separator) {
        return StringUtil.join(countries, separator);
    }

    public String getDirectors(String separator) {
        return StringUtil.join(directors, separator);
    }

    public String getCasts(String separator) {
        return StringUtil.join(casts, separator);
    }

    public List<String> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getAltName() {
        return altName;
    }

    public String getPositiveVotes() {
        return positiveVotes;
    }

    public String getNegativeVotes() {
        return negativeVotes;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration(String separator) {
        return StringUtil.join(duration, separator);
    }

    public ArrayList<SimilarItem> getSimilarItems() {
        return similarItems;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public static class SimilarItem implements Serializable
    {
        private String title, link, image;

        public SimilarItem(String title, String link, String image) {
            this.title = title;
            this.link = link;
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getImage() {
            return image;
        }

        public void clicked(View v)
        {
            Intent intent = new Intent(v.getContext(), EntryScroll.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.putExtra("link",this.getLink());
            v.getContext().startActivity(intent);
        }
    }

    public enum Type {
        FILMS("films"),
        SERIALS("serials"),
        CARTOONS("cartoons"),
        CARTOONSRIALS("cartoonserials"),
        TVSHOW("tvshow");

        private String value;

        Type(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return value;
        }

        public static Type get(String in)
        {
            Matcher matcher = Pattern
                    .compile("\\/\\w+\\/(\\w+)\\/")
                    .matcher(in);
            if(matcher.find())
            {
                return Type.valueOf(
                        matcher.group(1).toUpperCase()
                );
            }
            throw  new IllegalArgumentException();
        }

    }

}
