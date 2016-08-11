package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;


public class VideoPlayerActivity extends Activity {
    private String url;
    public static final String EXTRA_INDEX = "EXTRA_INDEX";
    public static final int PLAYLIST_ID = 6; //Arbitrary, for the example (different from audio)

    protected EMVideoView emVideoView;

    protected int selectedIndex;
    protected boolean pausedInOnStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.

        setContentView(R.layout.activity_video_player);

        url = getIntent().getStringExtra("link");
        if(url == null) throw new IllegalArgumentException("Link must be provided");
        // Get the URL from String VideoURL
        final Uri video = Uri.parse(url);
        emVideoView = (EMVideoView) findViewById(R.id.video_view);
        emVideoView.setVideoURI(Uri.parse(url));
        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                emVideoView.start();
            }
        });
    }
}

