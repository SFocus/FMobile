package WebParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import models.FilmItem;

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

    public List<FilmItem> getFilms()
    {
        List<FilmItem> films = new ArrayList<FilmItem>();
        Elements elements = document.body().select(FILM_ITEM);
        for(Element element : elements)
        {
            String quality = "";
            String name = element.select(FilmItem.FILM_FULL_NAME_SELECTOR).text();
            String image = element.select(FilmItem.FILM_IMG_SELECTOR).attr("src");
            String link = element.select(FilmItem.FILM_LINK_SELECTOR).attr("href");
            String country = element.select(FilmItem.FILM_COUNTRY_SELECTOR).text();
            String positive = element.select(FilmItem.FILM_POSITIVE_VOTES_SELECTOR).text();
            String negative = element.select(FilmItem.FILM_NEGATIVE_VOTES_SELECTOR).text();
            Elements qualityEl = element.select(FilmItem.FILM_QUALITY_SELECTOR);
            if(!qualityEl.isEmpty())
            {
                quality = qualityEl.first().className();
            }
            films.add(new FilmItem(image, name, country, positive, negative, quality, link));
        }
        return films;
    }
}
