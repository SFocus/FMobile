package WebParser;

/**
 * Created by Andrew on 13.03.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;

public class DataSource {
    
    private static final String userAgent = "Mozilla/5.0 (Linux; <Android Version>; <Build Tag etc.>) AppleWebKit/<WebKit Rev> (KHTML, like Gecko) Chrome/<Chrome Rev> Mobile Safari/<WebKit Rev>";

    public static final HashMap sources;

    static
    {
        sources = new HashMap();
    }

    public static ArrayList getQuickSearchResult(String request)
    {
        return null;
    }
}
