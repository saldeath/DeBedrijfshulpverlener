package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONObject;

import nl.debhver.debedrijfshulpverlener.enums.IncidentType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public IncidentType getType() {
        return IncidentType.valueOf(getString("type"));
    }
    public void setType(IncidentType type) {
        put("type", type.name());
    }
    public String getLocation() {
        return getString("location");
    }
    public void setLocation(String location) {
        put("location", location);
    }
    public String getDescription() {
        return getString("description");
    }
    public void setDescription(String description) {
        put("description", description);
    }
    public void setImage(ImageModel imageModel) {
        if(imageModel == null)
            put("image", JSONObject.NULL);
        else
            put("image", imageModel);
    }
    public ImageModel getImage()  {
        return (ImageModel) getParseObject("image");
    }
    public void setUser(User user) {
        put("user", user);
    }
    public User getUser()  {
        return (User) getParseObject("user");
    }
    public void setBranch(Branch branch){
        put("branch", branch);
    }
    public Branch getBranch(){
        return (Branch) getParseObject("branch");
    }

    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("HH:mm MMM dd, yyyy", Locale.getDefault());
    }

    @Override
    public String toString() {
        return  getType() + " @ " + getLocation() + " - " + getDateFormat().format(this.getTime());
    }

}
