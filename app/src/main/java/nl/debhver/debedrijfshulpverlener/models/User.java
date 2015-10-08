package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Tim on 10/5/15.
 */
@ParseClassName("user")
public class User extends ParseUser {
    public User() {
        super();
    }

    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }
    public List<String> getEROFunction() {
        return getList("ERO_function");
    }
    public void setEROFunction(List<String> EROFunction) {
        put("ERO_function", EROFunction);
    }
    public String getTelephoneNumber() {
        return getString("telephone_number");
    }
    public void setTelephoneNumber(String telephoneNumber) {
        put("telephone_number", telephoneNumber);
    }
    public String getRight() {
        return getString("right");
    }
    public void setRight(String right) {
        put("right", right);
    }
    public void setBranch(Branch branch) {
        put("branch", branch);
    }
    public Branch getBranch()  {
        return (Branch) getParseObject("branch");
    }
}
