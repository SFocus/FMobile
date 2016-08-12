package helpers;

import android.databinding.BaseObservable;

import interfaces.IBindingProgressable;
import interfaces.IBindingTextable;

/**
 * Created by Andrew on 04.08.2016.
 */
public class ObservableString extends BaseObservable implements IBindingTextable {

    private String string = "";

    public ObservableString(String string) {
        this.string = string;
    }

    public void set(String text) {
        if(!this.string.equals(text))
        {
            this.string = text;
            this.notifyChange();
        }
    }

    @Override
    public String getText() {
        return this.string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
