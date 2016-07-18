package models;

/**
 * Created by Andrew on 14.07.2016.
 */
public class FilesItem {

    public static final String FILE_FOLDER_SELECTOR = ".folder";
    public static final String FILE_TITLE_SELECTOR = ".title";
    public static final String FILE_DETAILS_SELECTOR = ".material-details";
    public static final String FILE_DATE_SELECTOR = ".material-date";

    private String title, details, date, param;

    public FilesItem(String title, String details, String date, String param) {
        this.title = title;
        this.details = details;
        this.date = date;
        this.param = param;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getDate() {
        return date;
    }

    public String getParam() {
        return param;
    }
}
