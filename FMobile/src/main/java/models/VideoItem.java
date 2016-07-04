package models;

/**
 * Created by Focus on 04.07.2016.
 */
public class VideoItem {


    /**
     * Film information selectors
     */
    public static final String VIDEO_LINK_SELECTOR            = ".b-poster-tile__link";
    public static final String VIDEO_IMG_SELECTOR             = ".b-poster-tile__image img";
    public static final String VIDEO_FULL_NAME_SELECTOR       = ".b-poster-tile__title-full";
    public static final String VIDEO_COUNTRY_SELECTOR         = ".b-poster-tile__title-info-items";
    public static final String VIDEO_POSITIVE_VOTES_SELECTOR  = ".b-poster-tile__title-info-vote-positive";
    public static final String VIDEO_NEGATIVE_VOTES_SELECTOR  = ".b-poster-tile__title-info-vote-negative";
    public static final String VIDEO_QUALITY_SELECTOR         = ".b-poster-tile__title-info-qualities span";


    private String poster, filmName, countryName, positiveVote, negativeVote, quality, link;

    public VideoItem(){

    }

    public VideoItem(String poster, String filmName, String countryName, String positiveVote, String negativeVote, String quality, String link) {
        this.poster = poster;
        this.filmName = filmName;
        this.countryName = countryName;
        this.positiveVote = positiveVote;
        this.negativeVote = negativeVote;
        this.quality = quality;
        this.link = link;
    }

    public String getPoster() {
        return poster;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getPositiveVote() {
        return positiveVote;
    }

    public String getNegativeVote() {
        return negativeVote;
    }

    public String getLink() {
        return link;
    }
}
