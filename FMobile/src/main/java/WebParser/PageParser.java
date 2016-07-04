package WebParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

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
    public List<VideoItem> getFilms()
    {
        List<VideoItem> films = new ArrayList<VideoItem>();
        Elements elements = document.body().select(FILM_ITEM);
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
        return films;
    }
}
