package group27.mad.com.triviaapp;
/*
Group 27
Homework 4
StatsActivity.java
Nanda kishore Kolluru
Ravi Teja Yarlagadda
 */
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    HashMap<Integer, Integer> answers = new LinkedHashMap<>();
    List<Question> questions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ProgressBar pbar = (ProgressBar) findViewById(R.id.pbarStats);
        TextView tv = (TextView) findViewById(R.id.tvTryAgain);
        TextView tv1 = (TextView) findViewById(R.id.tvPercentage);
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(TriviaActivity.ANSWER_CHOICES) && bundle.containsKey(MainActivity.QUESTIONS)){
            answers = (HashMap<Integer, Integer>) bundle.get(TriviaActivity.ANSWER_CHOICES);
            questions = (List<Question>) bundle.get(MainActivity.QUESTIONS);
            double percent = getQuizPercentage();
            pbar.setProgress((int)percent);
            tv1.setText((int)percent+"%");
            if (percent == 100) {
                tv.setText("Congratulations! You scored a 100%");
            }
        }

    }

    private double getQuizPercentage() {
        double correct = 0;
        double total = questions.size();
        for(Question q : questions){
            int ans = q.getAnswer();
            Log.d("kishore", "answer :" + ans);
            if(answers.containsKey(q.getId()) && answers.get(q.getId())==ans){
                correct++;
                Log.d("kishore", "Incremented ");
            }
        }
        return (correct/total)*100;
    }

    public void tryAction(View view) {
        Intent intent = new Intent(this, TriviaActivity.class);
        intent.putParcelableArrayListExtra(MainActivity.QUESTIONS, (ArrayList<? extends Parcelable>) questions);
        startActivity(intent);
        finish();
    }
    public void quitToMain(View view) {
        Intent intent = new Intent(StatsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
