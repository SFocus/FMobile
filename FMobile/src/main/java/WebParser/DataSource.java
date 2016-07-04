package WebParser;

/**
 * Created by Andrew on 13.03.2016.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class DataSource {

    public static final String userAgent = "Mozilla/5.0 (Linux; <Android Version>; <Build Tag etc.>) AppleWebKit/<WebKit Rev> (KHTML, like Gecko) Chrome/<Chrome Rev> Mobile Safari/<WebKit Rev>";

    public static final HashMap<String, String> sources;

    static
    {
        sources = new HashMap<String, String>();
        sources.put("media.getFilms", "http://fs.to/video/films/");
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
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
