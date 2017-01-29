package group27.mad.com.triviaapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishorekolluru on 9/22/16.
 */
public class Util {
    public static boolean isConnectedOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static List<Question> parseQuestions(String s) {
        List<Question> questions = new ArrayList<>();
        if (s != null) {
            try {
                JSONObject jobj = new JSONObject(s);
                JSONArray array = jobj.getJSONArray("questions");
                JSONObject choicesObj;
                for (int i = 0; i < array.length(); i++) {
                    Question q = new Question();
                    JSONObject o = array.getJSONObject(i);
                    q.setText(o.getString("text"));
                    q.setId(o.getInt("id"));
                    q.setImageUrl(o.optString("image"));

                    choicesObj = o.getJSONObject("choices");
                    q.setAnswer(choicesObj.getInt("answer"));
                    q.setChoices(getAnswerChoices(choicesObj));
questions.add(q);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return questions;
    }

    private static List<String> getAnswerChoices(JSONObject choice) {
        JSONArray arr;
        List<String> choices = new ArrayList<>();
        try {
            arr = choice.getJSONArray("choice");
            for(int j=0; j<arr.length();j++){
                choices.add((String) arr.get(j));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return choices;
    }
}
