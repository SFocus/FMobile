package WebParser;

/**
 * Created by Andrew on 13.03.2016.
 */

public class Sanitizer {
    public static String replace(String source, String[] data)
    {
        String[] parts = source.split("&");
        String out = "";
        try
        {
            for(int i = 0; i< parts.length; i++)
            {
                out += parts[i].replaceFirst("\\{\\{(\\w*)\\}\\}", data[i] )+ "&" ;
            }
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return out;
        }
        return out;
    }

    public static String prepareData(String source, String data)
    {
        return replace(source, new String[] {"",data});
    }

    public static String prepareData(String source, String[] data)
    {
        return replace(source, data);
    }
}
