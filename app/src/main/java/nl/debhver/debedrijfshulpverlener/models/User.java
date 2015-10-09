package nl.debhver.debedrijfshulpverlener.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;
import nl.debhver.debedrijfshulpverlener.enums.UserEROFunction;
import nl.debhver.debedrijfshulpverlener.enums.UserRight;

import java.util.ArrayList;
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
    public List<UserEROFunction> getEROFunction() {
        List<UserEROFunction> EROFunction = new ArrayList<>();
        List<String> functions = getList("ERO_function");
        for(String function : functions)
            EROFunction.add(UserEROFunction.valueOf(function));
        return EROFunction;
    }
    public void setEROFunction(List<UserEROFunction> EROFunction) {
        List<String> functions = new ArrayList<>();
        for(UserEROFunction function : EROFunction)
            functions.add(function.name());
        put("ERO_function", functions);
    }
    public String getTelephoneNumber() {
        return getString("telephone_number");
    }
    public void setTelephoneNumber(String telephoneNumber) {
        put("telephone_number", telephoneNumber);
    }
    public UserRight getRight() {
        return UserRight.valueOf(getString("right"));
    }
    public void setRight(UserRight right) {
        put("right", right.name());
    }
    public void setBranch(Branch branch) {
        put("branch", branch);
    }
    public Branch getBranch()  {
        return (Branch) getParseObject("branch");
    }
}
