package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("branch")
public class Branch extends ParseObject {

    public Branch() {
        super();
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }
    public String getCity() {
        return getString("city");
    }
    public void setCity(String city) {
        put("city", city);
    }
    public String getZipCode() {
        return getString("zip_code");
    }
    public void setZipCode(String zipCode) {
        put("zip_code", zipCode);
    }
    public String getAddress() {
        return getString("address");
    }
    public void setAddress(String address) {
        put("address", address);
    }
    @Override
    public String toString(){
        return getName();
    }
}
