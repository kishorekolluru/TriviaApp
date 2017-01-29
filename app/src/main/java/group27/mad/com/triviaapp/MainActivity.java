package group27.mad.com.triviaapp;
/*
Group 27
Homework 4
MainActivity.java
Nanda kishore Kolluru
Ravi Teja Yarlagadda
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadTriviaTask.TriviaDownloadInterface {

    ProgressBar pdialog;
    boolean isInternet;
    String baseUrl = "http://dev.theappsdr.com/apis/trivia_json/index.php";
    List<Question> triviaQuestionList;
    public static final String QUESTIONS="questions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hookInterestingThings();
        isInternet = Util.isConnectedOnline(this);
        downloadTrivia();
        Log.d("kishore", "In On create Main");
        getSupportActionBar().hide();
    }


public void startTriviaAction(View v){
    Intent intent = new Intent(this, TriviaActivity.class);
    intent.putParcelableArrayListExtra(QUESTIONS, (ArrayList<? extends Parcelable>)triviaQuestionList);
    startActivity(intent);
    finish();
}

    private void downloadTrivia() {
        if (isInternet) {
//            hideBeforeDownload();
            try {
                URL url = new URL(baseUrl);
                new DownloadTriviaTask(this).execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } else {
            toast("No Internet Connection!");
        }
    }


    ImageView imgTrivia;
    TextView tvTriviaStatus;
    Button btnstartTriv;

    private void hookInterestingThings() {
        tvTriviaStatus = (TextView) findViewById(R.id.tvTriviaReady);
        btnstartTriv = (Button) findViewById(R.id.btnStartTrivia);
        pdialog = (ProgressBar) findViewById(R.id.pbarMain);


    }

    @Override
    public void returnTriviaQuestion(List<Question> questions) {
        triviaQuestionList = questions;
    }

    @Override
    public void startProgress() {

    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG);
    }

    @Override
    public void stopProgress() {
        if (triviaQuestionList != null && triviaQuestionList.size() > 0) {
            RelativeLayout lay = (RelativeLayout) findViewById(R.id.relLayMain);
            lay.removeView(pdialog);
            lay.removeView(tvTriviaStatus);

            imgTrivia = new ImageView(this);
            RelativeLayout.LayoutParams imgLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imgLayout.addRule(RelativeLayout.BELOW, R.id.tvHeading);
            imgLayout.setMargins(0,16,0,0);
            imgTrivia.setImageResource(R.drawable.trivia);
            imgTrivia.setVisibility(View.VISIBLE);
            imgTrivia.setId(View.generateViewId());
            imgTrivia.setLayoutParams(imgLayout);


            RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvParams.addRule(RelativeLayout.BELOW, imgTrivia.getId());
            tvTriviaStatus.setText(R.string.trivia_ready);
            tvTriviaStatus.setLayoutParams(tvParams);

            lay.addView(imgTrivia);
            lay.addView(tvTriviaStatus);
            btnstartTriv.setEnabled(true);

        }else {
            toast("No Questions Found!");
            pdialog.setVisibility(View.INVISIBLE);
            tvTriviaStatus.setText("");
        }

    }

    public void exitApp(View view) {
        finish();
    }
}
