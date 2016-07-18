package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 11.07.2016.
 */
public class SearchItem implements Parcelable {

    /**
     * Search information selectors
     */
    public static final String SEARCH_ITEM_SELECTOR                 = ".b-search-page__results a";
    public static final String SEARCH_ITEM_IMAGE_SELECTOR           = ".b-search-page__results-item-image img";
    public static final String SEARCH_ITEM_TITLE_SELECTOR           = ".b-search-page__results-item-title";
    public static final String SEARCH_ITEM_TYPE_SELECTOR            = ".b-search-page__results-item-subsection";
    public static final String SEARCH_ITEM_GENRES_SELECTOR          = ".b-search-page__results-item-genres";
    public static final String SEARCH_ITEM_POSITIVE_VOTES_SELECTOR  = ".b-search-page__results-item-rating-positive";
    public static final String SEARCH_ITEM_NEGATIVE_VOTES_SELECTOR  = ".b-search-page__results-item-rating-negative";
    public static final String SEARCH_ITEM_DESCRIPTION_SELECTOR     = ".b-search-page__results-item-description";

    private String image, title, type, genres, positiveVotes, negativeVotes, description, link;

    public SearchItem(String image, String title, String type, String genres, String positiveVotes, String negativeVotes, String description, String link) {
        this.image          = image;
        this.title          = title;
        this.type           = type;
        this.genres         = genres;
        this.positiveVotes  = positiveVotes;
        this.negativeVotes  = negativeVotes;
        this.description    = description;
        this.link           = link;
    }

    protected SearchItem(Parcel in) {
        image           = in.readString();
        title           = in.readString();
        type            = in.readString();
        genres          = in.readString();
        positiveVotes   = in.readString();
        negativeVotes   = in.readString();
        description     = in.readString();
        link            = in.readString();
    }

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getGenres() {
        return genres;
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

    public String getLink() {
        return link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(genres);
        dest.writeString(positiveVotes);
        dest.writeString(negativeVotes);
        dest.writeString(description);
        dest.writeString(link);
    }
}
