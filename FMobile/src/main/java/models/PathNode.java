package models;

/**
 * Created by Andrew on 17.07.2016.
 */
public class PathNode {
    public String name;
    public String value;

    public PathNode(String name, String value)
    {
        this.name   = name;
        this.value  = value;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
