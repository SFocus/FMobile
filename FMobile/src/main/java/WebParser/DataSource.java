package WebParser;

/**
 * Created by Andrew on 13.03.2016.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class DataSource {

    private static final String userAgent = "Mozilla/5.0 (Linux; <Android Version>; <Build Tag etc.>) AppleWebKit/<WebKit Rev> (KHTML, like Gecko) Chrome/<Chrome Rev> Mobile Safari/<WebKit Rev>";

    public static final HashMap<String, String> sources;

    static
    {
        sources = new HashMap<String, String>();
        sources.put("media.getFilms", "http://fs.to/video/films/?page=%s");
        sources.put("media.getSerials", "http://fs.to/video/serials/?page=%s");
        sources.put("media.getEntry", "http://fs.to%s");
        sources.put("media.search", "http://fs.to/search.aspx?f=quick_search&search=%s&limit=10");
        sources.put("media.detailedSearch", "http://fs.to/search.aspx?search=%s");

        sources.put("entry.getComments", "http://fs.to/review/list/%s?loadedcount=%d");
    }

    /**
     * @param source Url key
     * @return Return url by given key
     */
    public static String getUrl(String source)
    {
        if(sources.containsKey(source))
            return sources.get(source);
        return null;
    }

    /**
     * Executes query with given url
     * @param url input url
     * @return Return Jsoup Document
     */
    public static Document executeQuery(String url)
    {
        try {
            return Jsoup.connect(url)
                    .userAgent(userAgent)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
