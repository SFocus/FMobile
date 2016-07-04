package com.androidbelieve.drawerwithswipetabs;

/**
 * Created by Focus on 04.07.2016.
 */
public class FilmItem {
    private String poster, filmName, countryName, positiveVote, negativeVote, quality;

    public FilmItem(){

    }

    public FilmItem(String poster, String filmName, String countryName, String positiveVote, String negativeVote, String quality) {
        this.poster = poster;
        this.filmName = filmName;
        this.countryName = countryName;
        this.positiveVote = positiveVote;
        this.negativeVote = negativeVote;
        this.quality = quality;
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
}
