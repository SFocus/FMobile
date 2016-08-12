package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import adapters.RecyclerBindingAdapter;
import models.ActiveDownloadModel;

/**
 * Created by Andrew on 03.08.2016.
 */
public class DownloadActivity extends Activity {

    private static int notifyId = 0;
    private static NotificationManager notificationManager;
    private static List<DownloadTask> activeDownloads = new ArrayList<>();
    private static List<DownloadTask> completeDownloads = new ArrayList<>();


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_downloads);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_active_downloads);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(
                new RecyclerBindingAdapter<>(R.layout.card_active_download_item, com.androidbelieve.drawerwithswipetabs.BR.model, getActiveDownloads())
        );
    }

    private ArrayList<ActiveDownloadModel> getActiveDownloads()
    {
        ArrayList<ActiveDownloadModel> out = new ArrayList<>();
        for(DownloadTask task : activeDownloads)
        {
            out.add(task.model);
        }
        return out;
    }


    public static void createBackgroundDownload(String url, String fileName, Context context, String fileSize)
    {
        DownloadTask task = new DownloadTask(url, fileName, context, fileSize);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        activeDownloads.add(
                task
        );
    }

    private static class DownloadTask extends AsyncTask<String, Integer, Void>
    {
        public ActiveDownloadModel model;

        private String url, fileName;

        private Context context;

        private NotificationCompat.Builder mBuilder;

        private NotificationManager mNotifyManager;

        public DownloadTask(String url, String fileName, Context notificationManager, String fileSize) {
            this.url = url;
            this.fileName = fileName;
            this.model = new ActiveDownloadModel(0, fileName, fileSize);
            this.context = notificationManager;

            notifyId++;

            Intent intent = new Intent(context, DownloadActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle(fileName)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_video)
                    .setContentText("Download in progress");
        }

        @Override
        protected Void doInBackground(String... strings) {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
            new DownloadFile(url, fileName) {
                @Override
                public void onProgressChange(int value)
                {
                    publishProgress(value);
                }

                @Override
                public void onChange(String value) {
                    model.setComplete(value);
                }
            };
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... params)
        {
            super.onProgressUpdate(params);
            mBuilder.setProgress(100, params[0], false);
            mNotifyManager.notify(notifyId, mBuilder.build());
            model.setPercentage(params[0]);
        }

        @Override
        protected void onPostExecute(Void params)
        {
            Log.d("PROGRESS", "finished");
            moveToComplete(this);
        }
    }

    private static void moveToComplete(DownloadTask task)
    {
        activeDownloads.remove(task);
        completeDownloads.add(task);
    }

    private static abstract class DownloadFile {
        public DownloadFile(String fileURL, String fileName)
        {
            try {
                int buffSize = 1024;
                long start = System.nanoTime();
                final int MB = 1048576;
                String RootDir = Environment.getExternalStorageDirectory()
                        + File.separator;
                File file = new File(fileName);
                URL url = new URL(fileURL);

                URLConnection ucon = url.openConnection();

                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is, buffSize);
                FileOutputStream outStream = new FileOutputStream(RootDir + file);

                byte[] buff = new byte[buffSize];
                int len;
                long total = 0;
                long contentLength = ucon.getContentLength();
                long lastChange = 0;
                long lastSpeedChange = System.currentTimeMillis();
                while ((len = inStream.read(buff)) != -1) {
                    total += len;
                    outStream.write(buff, 0, len);
                    long percent = (total * 100) / contentLength;
                    if(percent != lastChange)
                    {
                        onProgressChange((int) percent);
                    }
                    lastChange = percent;
                    if(System.currentTimeMillis() - lastSpeedChange > 1000)
                    {
                        lastSpeedChange = System.currentTimeMillis();
                        onChange(humanReadableByteCount(total));
                    }
                }

                outStream.flush();
                outStream.close();
                inStream.close();

            } catch (Exception e) {

                Log.d("Error: ", e.toString());
            }
        }

        public abstract void onProgressChange(int value);

        public abstract void onChange(String value);

        public static String humanReadableByteCount(long bytes) {
            int unit = 1000;
            if (bytes < unit) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(unit));
            String pre = ("kMGTPE").charAt(exp-1) + "";
            return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
        }
    }

}
