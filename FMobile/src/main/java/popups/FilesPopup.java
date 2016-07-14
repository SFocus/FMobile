package popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.androidbelieve.drawerwithswipetabs.R;

/**
 * Created by Andrew on 14.07.2016.
 */
public class FilesPopup extends Activity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.popup_files);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.8 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        String link = getIntent().getStringExtra("link");
        if(link == null) throw new IllegalArgumentException("Link must be provided");
    }
}
