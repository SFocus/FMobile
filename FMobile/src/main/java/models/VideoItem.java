package models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.androidbelieve.drawerwithswipetabs.EntryScroll;

/**
 * Created by Focus on 04.07.2016.
 */
public class VideoItem implements Parcelable {


    /**
     * Film information selectors
     */
    public static final String VIDEO_LINK_SELECTOR = ".b-poster-tile__link";
    public static final String VIDEO_IMG_SELECTOR = ".b-poster-tile__image img";
    public static final String VIDEO_FULL_NAME_SELECTOR = ".b-poster-tile__title-full";
    public static final String VIDEO_COUNTRY_SELECTOR = ".b-poster-tile__title-info-items";
    public static final String VIDEO_POSITIVE_VOTES_SELECTOR = ".b-poster-tile__title-info-vote-positive";
    public static final String VIDEO_NEGATIVE_VOTES_SELECTOR = ".b-poster-tile__title-info-vote-negative";
    public static final String VIDEO_QUALITY_SELECTOR = ".b-poster-tile__title-info-qualities span";
    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
    public boolean newlyLoaded = false;
    public boolean isFilm;
    private String poster, filmName, countryName, positiveVote, negativeVote, quality, link;

    public VideoItem() {

    }

    public VideoItem(String poster, String filmName, String countryName, String positiveVote, String negativeVote, String quality, String link, boolean isFilm) {
        this.poster = poster;
        this.filmName = filmName;
        this.countryName = countryName;
        this.positiveVote = positiveVote;
        this.negativeVote = negativeVote;
        this.quality = quality;
        this.link = link;
        this.isFilm = isFilm;
    }

    public VideoItem(Parcel in) {
        this.poster = in.readString();
        this.filmName = in.readString();
        this.countryName = in.readString();
        this.positiveVote = in.readString();
        this.negativeVote = in.readString();
        this.quality = in.readString();
        this.link = in.readString();
        this.isFilm =  in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster);
        dest.writeString(filmName);
        dest.writeString(countryName);
        dest.writeString(positiveVote);
        dest.writeString(negativeVote);
        dest.writeString(quality);
        dest.writeString(link);
        dest.writeByte((byte) (isFilm ? 1 : 0));
    }

    public void clicked(View v) {
        Intent intent = new Intent(v.getContext(), EntryScroll.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("link", link);
        v.getContext().startActivity(intent);
    }
}
