package group27.mad.com.triviaapp;
/*
Group 27
Homework 4
TriviaActivity.java
Nanda kishore Kolluru
Ravi Teja Yarlagadda
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TriviaActivity extends AppCompatActivity implements ImageDowloadTask.ImageDownloadable {
    List<Question> questions;
    Bitmap currentQuestionImage;
    ProgressBar progressDialog;
    Question currentQuestion;
    TextView tvQuesition;
    TextView tvQnum;
    TextView tvSecs;
    ImageView imageView;
    RadioGroup rg;
    HashMap<Integer, Integer> choiceSelections = new HashMap<>();
    private int currQid = -1;
    CountDownTimer timer;
    public static final String ANSWER_CHOICES = "answerchoices";
    private long millisLeft=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        hookInterestingThings();
        progressDialog.setIndeterminate(true);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        imageView.setVisibility(View.VISIBLE);

        questions = (List<Question>) extras.get(MainActivity.QUESTIONS);

        currentQuestion = questions.get(0);
        currQid++;
        timerStart(2 * 60 * 1000);

        millisLeft = 2 * 60 * 1000;
        tvSecs.setText(millisLeft/1000 + " seconds");
        displayQuestion(currentQuestion);


    }

    public void timerStart(long timeLengthMilli) {
        timer = new CountDownTimer(timeLengthMilli, 1000) {

            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                tvSecs.setText(millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                toast("Time Over!");
                startStatActivity();
            }
        }.start();
    }

    public void timerPause(){
        timer.cancel();
    }

    private void hookInterestingThings() {
        tvQuesition = (TextView) findViewById(R.id.tvQuestion);
        imageView = (ImageView) findViewById(R.id.imgView);
        tvQnum = (TextView) findViewById(R.id.tv1);
        tvSecs = (TextView) findViewById(R.id.tv3);
        progressDialog = (ProgressBar) findViewById(R.id.pbarImage);
    }
    AsyncTask<String, Void, Bitmap> currTask;
    private void displayQuestion(final Question currentQuestion) {
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.VISIBLE);
        String url = currentQuestion.getImageUrl();
        if (url != null && !url.equals("") && Util.isConnectedOnline(this)) {
            currTask = new ImageDowloadTask(this).execute(url);
        }
        tvQuesition.setText(currentQuestion.getText());
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relLay);
        if(rg!=null)
            rg.removeAllViews();
        rg = new RadioGroup(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,R.id.tvQuestion);
        rg.setLayoutParams(params);

        ScrollView scrollView = new ScrollView(this);
        RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 560);
        par.addRule(RelativeLayout.BELOW, R.id.tvQuestion);
        scrollView.setFillViewport(true);
        scrollView.setLayoutParams(par);
        scrollView.addView(rg);
        rl.addView(scrollView);


        List<RadioButton> radioBtns = createRadioChoices(currentQuestion.getChoices());
        for (RadioButton b : radioBtns) {
            rg.addView(b);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                if(checkedId!=-1)
                    choiceSelections.put(currentQuestion.getId(),id-100);
                Log.d("kishore", "radio clicked"+(id - 100));
            }
        });


    }


    private List<RadioButton> createRadioChoices(List<String> choices) {
        RadioButton btn;
        List<RadioButton> radioBtns = new ArrayList<>();
        int i=101;
        for (String s : choices) {
            btn = new RadioButton(this);
            btn.setText(s);
            btn.setId(i++);
            btn.setEnabled(true);
            radioBtns.add(btn);

        }
        return radioBtns;
    }


    @Override
    public void setImageData(Bitmap image) {
        currentQuestionImage = image;
        imageView.setImageBitmap(currentQuestionImage);
    }

    @Override
    public void startProgress() {
        progressDialog.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        timerPause();
    }

    @Override
    public void stopProgress() {
        progressDialog.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        timerResume();
    }

    private void timerResume() {
        timerStart(millisLeft);
    }

    public void nextAction(View view) {
        if (++currQid < questions.size()) {
            tvQnum.setText(new StringBuilder().append("Q").append(currQid + 1).toString());
            currTask.cancel(true);
            displayQuestion(questions.get(currQid));
        } else {
            startStatActivity();
        }

    }

    private void startStatActivity() {
        Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
        intent.putExtra(ANSWER_CHOICES, choiceSelections);
        intent.putParcelableArrayListExtra(MainActivity.QUESTIONS, (ArrayList<? extends Parcelable>) questions);
        startActivity(intent);
        finish();
    }
    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG);
    }

    public void quitFromQuiz(View view) {
        Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
        startActivity(intent);
        if(currTask!=null && (currTask.getStatus() == AsyncTask.Status.RUNNING
                || currTask.getStatus() == AsyncTask.Status.PENDING)){
            currTask.cancel(true);
        }
        finish();
    }

}
