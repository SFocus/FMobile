package models;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 14.07.2016.
 */
public class FilesItem {

    public static final String FOLDER_SELECTOR          = ".folder";
    public static final String FOLDER_TITLE_SELECTOR    = ".title";
    public static final String FOLDER_DETAILS_SELECTOR  = ".material-details";
    public static final String FOLDER_DATE_SELECTOR     = ".material-date";

    public static final String FILE_SELECTOR            = ".b-file-new";
    public static final String FILE_QUALITY_SELECTOR    = ".video-qulaity";
    public static final String FILE_NAME_SELECTOR       = ".b-file-new__link-material-filename-text";
    public static final String FILE_SERIES_NUM_SELECTOR = ".b-file-new__link-material-filename-series-num";
    public static final String FILE_SIZE_SELECTOR       = ".b-file-new__link-material-download";


    public boolean isFolder = false;
    public boolean isFile = false;

    //Folder info
    private String title, details, date, param;

    //File info
    private String quality, fileName, size, link, downloadLink, seriesNum;
    public HashMap<String, AdditionalQualityInfo> qualities = new HashMap<>();

    public FilesItem(String title, String details, String date, String param) {
        this.title = title;
        this.details = details;
        this.date = date;
        this.param = param;
        this.isFolder = true;
    }

    public FilesItem(String quality, String fileName, String size, String link, String downloadLink, String seriesNum)
    {
        this.quality        = quality;
        this.fileName       = fileName;
        this.size           = size;
        this.link           = link;
        this.downloadLink   = downloadLink;
        this.seriesNum      = seriesNum;
        this.qualities.put(quality, new AdditionalQualityInfo(fileName, size, link, downloadLink));

        this.isFile         = true;
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

    public String getQuality() {
        return StringUtil.join(qualities.keySet()," ");
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public String getLink() {
        return link;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink)
    {
        this.downloadLink = downloadLink;
    }

    public String getSeriesNum() {
        return seriesNum;
    }

    public static class AdditionalQualityInfo
    {
        private String fileName, size, link, downloadLink;

        public AdditionalQualityInfo(String fileName, String size, String link, String downloadLink) {
            this.fileName = fileName;
            this.size = size;
            this.link = link;
            this.downloadLink = downloadLink;
        }

        public String getFileName() {
            return fileName;
        }

        public String getSize() {
            return size;
        }

        public String getLink() {
            return link;
        }

        public String getDownloadLink() {
            return downloadLink;
        }
    }
}
