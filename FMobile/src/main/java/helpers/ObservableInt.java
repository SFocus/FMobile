package helpers;

import android.databinding.BaseObservable;

import interfaces.IBindingProgressable;
import interfaces.IBindingTextable;

/**
 * Created by Andrew on 04.08.2016.
 */
public class ObservableInt extends BaseObservable implements IBindingTextable, IBindingProgressable {

    private Integer value;


    public ObservableInt(int value) {
        this.value = value;
    }

    public void set(int value)
    {
        if(value != this.value)
        {
            this.value = value;
            this.notifyChange();
        }
    }


    @Override
    public int getPercentage() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.value.toString();
    }

    @Override
    public String toString()
    {
        return this.value.toString();
    }
}
