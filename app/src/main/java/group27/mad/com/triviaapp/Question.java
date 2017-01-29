package group27.mad.com.triviaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by kishorekolluru on 9/22/16.
 */
public class Question implements Parcelable{
    private String text;
    private int id;
    private List<String> choices;
    private int answer;
    private String imageUrl;

    public Question(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Question(Parcel in){
        text = in.readString();
        id = in.readInt();
        answer = in.readInt();
        choices = in.readArrayList(ClassLoader.getSystemClassLoader());
        imageUrl = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(text);
        dest.writeInt(id);
        dest.writeInt(answer);
        dest.writeList(choices);
        dest.writeString(imageUrl);
    }
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
