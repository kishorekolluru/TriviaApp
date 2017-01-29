package group27.mad.com.triviaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kishorekolluru on 9/22/16.
 */
public class ImageDowloadTask extends AsyncTask<String, Void, Bitmap> {
    ImageDownloadable activity;
    public ImageDowloadTask(ImageDownloadable activity){
        this.activity = activity;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection con = null;
        Bitmap image= null;
        try {
            URL url = new URL(params[0]);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        } catch (MalformedURLException e1) {
            e1.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.startProgress();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.setImageData(bitmap);
activity.stopProgress();

    }
    static interface ImageDownloadable{
        public void setImageData(Bitmap image);
        public void startProgress();

        public void stopProgress();
    }
}
