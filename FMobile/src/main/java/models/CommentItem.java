package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentItem implements Parcelable {
    public static final String COMMENT_ITEM_SELECTOR            = ".b-item-material-comments__item";
    public static final String COMMENT_AUTHOR_IMAGE_SELECTOR    = ".b-item-material-comments__item-photo";
    public static final String COMMENT_AUTHOR_NAME_SELECTOR     = ".b-item-material-comments__item-name span";
    public static final String COMMENT_TIME_SELECTOR            = ".b-item-material-comments__item-time time";
    public static final String COMMENT_VOTE_YES_SELECTOR        = ".m-item-material-comments__item-answer_type_yes span:last-child";
    public static final String COMMENT_VOTE_NO_SELECTOR         = ".m-item-material-comments__item-answer_type_no span:last-child";
    public static final String COMMENT_TEXT_SELECTOR            = ".b-item-material-comments__item-text";

    private String author, image, time, positiveVotes, negativeVotes, text;

    public CommentItem(String author, String image, String time, String positiveVotes, String negativeVotes, String text) {
        this.author         = author;
        this.image          = image;
        this.time           = time;
        this.positiveVotes  = positiveVotes;
        this.negativeVotes  = negativeVotes;
        this.text           = text;
    }


    protected CommentItem(Parcel in) {
        author              = in.readString();
        image               = in.readString();
        time                = in.readString();
        positiveVotes       = in.readString();
        negativeVotes       = in.readString();
        text                = in.readString();
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getPositiveVotes() {
        return positiveVotes;
    }

    public String getNegativeVotes() {
        return negativeVotes;
    }

    public String getText() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(image);
        dest.writeString(time);
        dest.writeString(positiveVotes);
        dest.writeString(negativeVotes);
        dest.writeString(text);
    }
}
