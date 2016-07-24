package WebParser;

import java.net.URLEncoder;

/**
 * Created by Andrew on 04.07.2016.
 */
public class QueryBuilder {

    /*
    *  Default charset
    */
    private static final String defaultCharset = "UTF-8";

    /**
     * @param in    url data
     * @return      Encoded data
     */
    private static Object[] encodeParams(Object[] in)
    {
        try {
            Object[] out = new Object[in.length];
            for(int i = 0; i < in.length; i++)
                out[i] = URLEncoder.encode(in[i].toString(), defaultCharset);

            return out;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return in;
        }

    }
    /**
     * @param in   input url which will be replaced with given data
     * @param data url data
     * @return replaced url with given data
     */
    private static String replace(String in, Object[] data) {
        return String.format(
                in,
                encodeParams(data)
        );
    }

    /**
     * @param url  input url
     * @param data Array of url data
     * @return Return url ready for execution
     */
    public static String buildQuery(String url, Object[] data) {
        return replace(url, data);
    }

    /**
     * @param url  input url
     * @param data Single data part
     * @return Return url ready for execution
     */
    public static String buildQuery(String url, Object data) {
        return replace(url, new Object[]{data});
    }
}
