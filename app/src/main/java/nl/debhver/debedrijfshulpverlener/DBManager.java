package nl.debhver.debedrijfshulpverlener;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.User;

/**
 * Created by Koen on 6-10-2015.
 */
public class DBManager {
    private static DBManager instance = null;

    public static DBManager getInstance(){
        if(instance == null){
            instance = new DBManager();
           //createFakeBranches();
        }
        return instance;
    }

    static void createFakeBranchesAndAdminRights(){
        Branch b = new Branch();
        Branch c = new Branch();
        b.setName("coolName");
        c.setName("secondBranchName");

        b.saveInBackground();
        c.saveInBackground();
        Log.d("score", " saved in background");

    }

    void createUser(User u, final AdminAddUserActivity adminAddUserActivity){
        u.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                   adminAddUserActivity.popupShortToastMessage("User saved to database.");
                } else {
                   adminAddUserActivity.popupShortToastMessage("ERROR: User was not saved to database.");
                }
            }
        });
    }

    void getBranchNames(final AdminAddUserActivity adminAddUserActivity){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("branch");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    adminAddUserActivity.populateBranchesDropdown(objects);
                } else {
                    adminAddUserActivity.popupShortToastMessage("ERROR: Nothing was retrieved from database.");
                }
            }
        });
    }

    void getEROFunctions(final AdminAddUserActivity aaua){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("EROFunction");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    aaua.populateEROFunctionList(objects);
                } else {
                    aaua.popupShortToastMessage("ERROR: Nothing was retrieved from database.");
                }
            }
        });

    }


}
