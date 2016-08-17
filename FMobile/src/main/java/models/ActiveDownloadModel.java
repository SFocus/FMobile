package models;

import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;

import helpers.ObservableInt;
import helpers.ObservableString;

/**
 * Created by Andrew on 04.08.2016.
 */
public class ActiveDownloadModel extends BaseObservable {

    private ObservableInt percentage;

    private ObservableString title;

    private ObservableString complete;

    private String fileSize;

    public ActiveDownloadModel(int percentage, String title, String fileSize) {
        this.percentage = new ObservableInt(percentage);
        this.title = new ObservableString(title);
        this.fileSize = fileSize;

        this.complete = new ObservableString("0.0b");
    }

    public ObservableInt getPercentage() {
        return percentage;
    }

    public void setPercentage(int value)
    {
        this.percentage.set(value);
    }

    public ObservableString getTitle() {
        return title;
    }

    public ObservableString getComplete() {
        return complete;
    }

    public void setComplete(String value)
    {
        this.complete.set(value);
    }

    public String getFileSize() {
        return fileSize;
    }

    public void clicked(View v)
    {
        Log.d("CLICK","K");
    }
}
