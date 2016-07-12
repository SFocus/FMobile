package models;

/**
 * Created by Andrew on 12.07.2016.
 */
public class CommentItem {
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
}
