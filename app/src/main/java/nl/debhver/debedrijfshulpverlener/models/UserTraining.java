package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.R;
import nl.debhver.debedrijfshulpverlener.parse.ParseApplication;

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
    public Boolean getHasAchieved() {
        return getBoolean("achieved");
    }
    public void setHasAchieved(Boolean achieved) {
        put("achieved", achieved);
    }
    public User getUser()  {
        return (User) getParseObject("user");
    }
    public void setUser(User user) {
        put("user", user);
    }
    public Training getTraining()  {
        return (Training) getParseObject("training");
    }
    public void setTraining(Training training) {
        put("training", training);
    }

    @Override
    public String toString() {
        Training training;
        try {
            training = getTraining().fetch();
        } catch (ParseException e) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(training.getName().toString());
        builder.append(" - ");

        if(isAchieved())
            builder.append(getDateFormat().format(getDateAchieved()) + " - " + getDateFormat().format(getExpirationDate()));
        else if(!isAchieved())
            builder.append(getDateFormat().format(getScheduleDate()));

        builder.append("\n");
        builder.append(ParseApplication.getContext().getString(R.string.Description));
        builder.append(":  ");
        builder.append(training.getDescription());

        return builder.toString();
    }

    public boolean isAchieved() {
        if(getDateAchieved() != null)
            return true;
        return false;
    }

    public boolean isScheduled() {
        if(!isAchieved()) {
            if (getScheduleDate() != null) {
                Calendar calendar = Calendar.getInstance();
                Calendar scheduled = Calendar.getInstance();
                scheduled.setTime(getScheduleDate());
                if (scheduled.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
                    return true;
                } else if (scheduled.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                    if(scheduled.get(Calendar.DAY_OF_YEAR) >= calendar.get(Calendar.DAY_OF_YEAR)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isExpired() {
        if(isAchieved()) {
            if (getExpirationDate() != null) {
                Calendar calendar = Calendar.getInstance();
                Calendar expiration = Calendar.getInstance();
                expiration.setTime(getExpirationDate());
                System.out.println(expiration.get(Calendar.YEAR) + " - " + expiration.get(Calendar.DAY_OF_YEAR));
                System.out.println(calendar.get(Calendar.YEAR) + " - " + calendar.get(Calendar.DAY_OF_YEAR));
                if (expiration.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
                    return false;
                } else if (expiration.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                    if(expiration.get(Calendar.DAY_OF_YEAR) >= calendar.get(Calendar.DAY_OF_YEAR)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFailed() {
        if(!isScheduled() && !getHasAchieved())
            return true;
        return  false;
    }
}
