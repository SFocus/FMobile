package WebParser;

/**
 * Created by Andrew on 04.07.2016.
 */
public class QueryBuilder {

    /**
     * @param in input url which will be replaced with given data
     * @param data url data
     * @return replaced url with given data
     */
    private static String replace(String in, Object[] data)
    {
        String out = "";
        String[] parts = in.split("&");
        for(int i = 0; i < data.length; i++)
        {
            out+= parts[i].replaceFirst("\\{\\{(\\w*)\\}\\}", data[i].toString()) + "&";
        }
        return out;
    }

    /**
     * @param url input url
     * @param data Array of url data
     * @return Return url ready for execution
     */
    public static String buildQuery(String url, Object[] data)
    {
        return replace(url, data);
    }

    /**
     * @param url input url
     * @param data Single data part
     * @return Return url ready for execution
     */
    public static String buildQuery(String url, Object data)
    {
        return replace(url, new Object[] { data });
    }
}
