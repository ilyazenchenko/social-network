package ru.zenchenko.socialsecuritymicrocervice.models;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
public class Post implements Comparable<Post>{
    private Integer id;

    private Date time;

    private String text;

    private User user;

    @Override
    public int compareTo(Post o) {
        return time.compareTo(o.getTime());
    }

    public String getFormattedDate() {
        if (time != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM hh:mm",
                    new Locale("ru", "RU"));
            return dateFormat.format(time).charAt(0) == '0' ?
                    dateFormat.format(time).substring(1) :
                    dateFormat.format(time);
        } else {
            return "No date available";
        }
    }

}