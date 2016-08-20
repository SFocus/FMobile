package models;

/**
 * Created by focus on 19.08.16.
 */
public class Favorites {
    private int id;
    private String url;

    public Favorites() {

    }

    public Favorites(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public Favorites(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
