package helpers;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import interfaces.IBindingProgressable;

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
    public static void bindTextView(TextView view, Object in)
    {
        view.setText(in.toString());
    }

    @BindingAdapter("android:src")
    public static void loadImage(ImageView view, String poster) {
        Log.d("URL", "http:" + poster);
        Picasso.with(view.getContext())
                .load("http:" + poster)
                .into(view);
    }

    @BindingAdapter("android:fontFamily")
    public static void setFont(TextView view, String font) {
        Typeface fonts = Typeface.createFromAsset(view.getContext().getAssets(), font);
        view.setTypeface(fonts);
    }

}
