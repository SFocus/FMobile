package helpers;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import interfaces.IBindingProgressable;
import interfaces.IBindingTextable;

/**
 * Created by Andrew on 04.08.2016.
 */

public final class BindingAdapters {

    private BindingAdapters() { throw new AssertionError(); }

    @SuppressWarnings("unchecked")
    @BindingAdapter("android:progress")
    public static void bindProgressBar(ProgressBar view, IBindingProgressable in) {
        view.setProgress(in.getPercentage());
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("android:text")
    public static void bindTextView(TextView view, IBindingTextable in)
    {
        view.setText(in.getText());
    }
}
