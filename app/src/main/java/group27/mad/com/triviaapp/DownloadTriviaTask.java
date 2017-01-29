package group27.mad.com.triviaapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishorekolluru on 9/22/16.
 */
public class DownloadTriviaTask extends AsyncTask<URL, Void, List<Question>> {
    TriviaDownloadInterface activity;
    public DownloadTriviaTask(TriviaDownloadInterface activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        super.onPostExecute(questions);
        Log.d("kishore", "on post execution");
        activity.returnTriviaQuestion(questions);
        activity.stopProgress();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("kishore", "on pre execution");
        activity.startProgress();
    }

    @Override
    protected List<Question> doInBackground(URL... params) {
        BufferedReader br = null;
        List<Question> questions = new ArrayList<>();
        HttpURLConnection con = null;
        try {
            Log.d("kishore","in download task");
            con = (HttpURLConnection) params[0].openConnection();
    con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK){
                Log.d("kishore","got content");
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line=br.readLine())!=null){
                    sb.append(line);
                }
                questions = Util.parseQuestions(sb.toString());
                Log.d("kishore", "parsed questions");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }if(con!=null)
                con.disconnect();
        }
        return questions;
    }
    public interface TriviaDownloadInterface{
        public void returnTriviaQuestion(List<Question> question);
        public void startProgress();

        public void stopProgress();
    }
}
