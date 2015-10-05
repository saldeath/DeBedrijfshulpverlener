package nl.debhver.debedrijfshulpverlener.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("incident")
public class Incident extends ParseObject{
    public Incident() {
        super();
    }

    public Date getTime() {
        return getDate("time");
    }
    public void setTime(Date time) {
        put("time", time);
    }
    public String getType() {
        return getString("type");
    }
    public void setType(String type) {
        put("type", type);
    }
}
