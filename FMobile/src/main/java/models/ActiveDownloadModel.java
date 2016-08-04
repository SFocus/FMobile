package models;

import android.databinding.BaseObservable;
import android.databinding.Observable;

import helpers.ObservableInt;
import helpers.ObservableString;
import interfaces.IBindingProgressable;
import interfaces.IBindingTextable;

/**
 * Created by Andrew on 04.08.2016.
 */
public class ActiveDownloadModel extends BaseObservable {

    private ObservableInt percentage;

    private ObservableString title;

    public ActiveDownloadModel() {};

    public ActiveDownloadModel(int percentage, String title) {
        this.percentage = new ObservableInt(percentage);
        this.title = new ObservableString(title);
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
}
