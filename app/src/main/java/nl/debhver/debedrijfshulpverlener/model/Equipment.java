package nl.debhver.debedrijfshulpverlener.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.util.Date;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("equipment")
public class Equipment extends ParseObject{
    public Equipment() {
        super();
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }
    public String getDescription() {
        return getString("description");
    }
    public void setDescription(String description) {
        put("description", description);
    }
    public String getLocation() {
        return getString("location");
    }
    public void setLocation(String location) {
        put("location", location);
    }
    public String getType() {
        return getString("type");
    }
    public void setType(String type) {
        put("type", type);
    }
    public Date getDateOfPurchase() {
        return getDate("date_of_purchase");
    }
    public void setDateOfPurchase(Date dateOfPurchase) {
        put("date_of_purchase", dateOfPurchase);
    }
    public Date getExpirationDate() {
        return getDate("expiration_date");
    }
    public void setExpirationDate(Date expirationDate) {
        put("expiration_date", expirationDate);
    }
    public Date getDateOfInspection() {
        return getDate("date_of_inspection");
    }
    public void setDateOfInspection(Date dateOfInspection) {
        put("date_of_inspection", dateOfInspection);
    }
    public void setImage(ImageModel imageModel) {
        put("image", imageModel);
    }
    public ImageModel getImage()  {
        return (ImageModel) getParseObject("image");
    }
    public void setBranch(Branch branch) {
        put("branch", branch);
    }
    public Branch getBranch()  {
        return (Branch) getParseObject("branch");
    }
}
