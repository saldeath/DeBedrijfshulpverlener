package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("user_training")
public class UserTraining extends ParseObject {
    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    public UserTraining() {
        super();
    }

    public Date getScheduleDate() {
        return getDate("schedule_date");
    }
    public void setScheduleDate(Date scheduleDate) {
        put("schedule_date", scheduleDate);
    }
    public Date getDateAchieved() {
        return getDate("date_achieved");
    }
    public void setDateAchieved(Date dateAchieved) {
        put("date_achieved", dateAchieved);
    }
    public Date getExpirationDate() {
        return getDate("expiration_date");
    }
    public void setExpirationDate(Date expirationDate) {
        put("expiration_date", expirationDate);
    }
    public void setUser(User user) {
        put("user", user);
    }
    public User getUser()  {
        return (User) getParseObject("user");
    }
    public void setTraining(Training training) {
        put("training", training);
    }
    public Training getTraining()  {
        return (Training) getParseObject("training");
    }

    @Override
    public String toString() {
        Training training;
        try {
            training = getTraining().fetch();
        } catch (ParseException e) {
            return null;
        }

        if(getDateAchieved() != null)
            return training.getName().toString() + " - " + getDateFormat().format(getDateAchieved()) + " - " + getDateFormat().format(getExpirationDate());
        else
            return null;
    }
}
