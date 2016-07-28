package helpers;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import WebParser.DataSource;

/**
 * Created by Andrew on 28.07.2016.
 */
public abstract class SimpleAsyncLoader extends AsyncTask<String, Void, Document> {

    @Override
    protected Document doInBackground(String... params) {
        if(params[0] == null) throw new IllegalArgumentException("URL must be provided. " + params[0] + "given");
        return DataSource.executeQuery(params[0]);
    }

    public abstract void onPostExecute(Document document);
}