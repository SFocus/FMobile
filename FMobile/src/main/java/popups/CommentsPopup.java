package popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.androidbelieve.drawerwithswipetabs.R;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentsPopup extends Activity{

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        setContentView(R.layout.popup_comments);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(
                ( int ) ( dm.widthPixels * 0.8 ),
                ( int ) ( dm.heightPixels * 0.8 )
        );

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f; // уровень затемнения от 1.0 до 0.0
        getWindow().setAttributes(lp);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

}
