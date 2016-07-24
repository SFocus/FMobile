package models;


import org.jsoup.helper.StringUtil;

import java.util.List;

/**
 * Created by Andrew on 05.07.2016.
 */
public class VideoEntry {

    /**
     * Entry information selectors
     */
    public static final String ENTRY_NAME_SELECTOR = ".b-tab-item__title-inner span";
    public static final String ENTRY_NAME_ALT_SELECTOR = ".b-tab-item__title-inner div";
    public static final String ENTRY_IMAGES_SET_SELECTOR = "a.item[rel]";
    public static final String ENTRY_DESCRIPTION_SELECTOR = ".item-decription";
    public static final String ENTRY_POSITIVE_VOTES_SELECTOR = ".m-tab-item__vote-value_type_yes";
    public static final String ENTRY_NEGATIVE_VOTES_SELECTOR = ".m-tab-item__vote-value_type_no";


    public static final String ENTRY_INFO_CELL_SELECTOR = ".m-item-info-td_type-short";
    public static final String ENTRY_GENRES_SELECTOR = "span > a";
    public static final String ENTRY_YEAR_SELECTOR = "a span";
    public static final String ENTRY_COUNTRY_SELECTOR = "a";
    public static final String ENTRY_DIRECTOS_SELECTOR = "span > a";
    public static final String ENTRY_CASTS_SELECTOR = "span > a";
    public static final String ENTRY_STATUS_SELECTOR = "*";
    public static final String ENTRY_DURATION_SELECTOR = "td > * > span";


    public static final String ENTRY_INFO_KEYS = ".item-info table tbody tr td:first-child";
    public static final String ENTRY_INFO_VALUES = ".item-info table tbody tr td:last-child";


    public static final String SPACE_SEPARATOR = " ";
    public static final String COMMA_SEPARATOR = ", ";

    private List<String> genres;
    private List<String> year;
    private List<String> duration;
    private List<String> countries;
    private List<String> directors;
    private List<String> casts;
    private List<String> images;

    private String name;
    private String altName;
    private String positiveVotes;
    private String negativeVotes;
    private String description;


    public VideoEntry(List<String> genres, List<String> year, List<String> duration,List<String> countries, List<String> directors, List<String> casts, List<String> images, String name, String altName, String positiveVotes, String negativeVotes, String description) {
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
}
