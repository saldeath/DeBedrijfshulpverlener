package nl.debhver.debedrijfshulpverlener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.Incident;
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

    void createIncident(Incident i, final HomeUserActivity homeUserActivity){
        i.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    doToastMessageInView(homeUserActivity, "Incident saved to database.");
                    //homeUserActivity.clearFieldsAfterAddingBranch();
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(homeUserActivity, "ERROR: Incident was not saved to database.");
                }
            }
        });
    }

    void createUser(User u, final AdminAddUserActivity adminAddUserActivity){
        u.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    doToastMessageInView(adminAddUserActivity, "User saved to database.");
                    adminAddUserActivity.clearFieldsAfterAddingUser();
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminAddUserActivity, "ERROR: User was not saved to database.");
                }
            }
        });
    }

    void updateUser(User u, final AdminAddUserActivity adminAddUserActivity){
        u.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    doToastMessageInView(adminAddUserActivity, "User updated to database.");
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminAddUserActivity, "ERROR: User was not saved to database.");
                }
            }
        });
    }

    public List<ParseObject> getBranches() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("branch");
        List<ParseObject> list = null;
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }


    void createBranch(Branch b, final AdminAddBranchActivity adminAddBranchActivity){
        b.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    doToastMessageInView(adminAddBranchActivity, "Branch saved to database.");
                    adminAddBranchActivity.clearFieldsAfterAddingBranch();
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminAddBranchActivity, "ERROR: Branch was not saved to database.");
                }
            }
        });
    }

    void getUsers(final AdminUserDefaultActivity adminDefaultActivity){
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    adminDefaultActivity.setUserList(objects);
                    adminDefaultActivity.populateListView(objects);
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminDefaultActivity, "ERROR: Failed to retrieve users.");
                }
            }
        });
    }

    void doToastMessageInView(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    void getSingleUserById(final AdminAddUserActivity adminAddUserActivity, String userObjId){
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("objectId",userObjId);
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    adminAddUserActivity.loadSingleUserDetails(objects);
                } else {
                    // error
                }
            }
        });
    }

    void getFilteredUserList(final AdminUserDefaultActivity adminDefaultActivity, List<String> userRightList, List<String> userEROFunctionList){
        List<ParseQuery<User>> queries = new ArrayList<ParseQuery<User>>();

        for(String userRightName : userRightList){
            ParseQuery<User> query = ParseQuery.getQuery(User.class);
            query.whereEqualTo("right", userRightName);
            queries.add(query);
            System.out.println("looking for right-" + userRightName);
        }
        for(String userEROFunction : userEROFunctionList){
            ParseQuery<User> query = ParseQuery.getQuery(User.class);
            query.whereEqualTo("ERO_function", userEROFunction);
            //query.whereContains()
            queries.add(query);
            System.out.println("looking for erofunction-" + userEROFunction);
        }

        //ParseQuery<User> superQuery = (ParseQuery) User.getQuery();
        ParseQuery<User> superQuery = ParseQuery.getQuery(User.class);
        superQuery = ParseQuery.or(queries);

        superQuery.findInBackground(new FindCallback<User>() {
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {
                        System.out.println("nietleeg");
                    } else {
                        System.out.println("lEEG");
                    }
                    adminDefaultActivity.setUserList(objects);
                    adminDefaultActivity.populateListView(objects);
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminDefaultActivity, "ERROR: Failed to retrieve users.");
                }
            }
        });
    }

    void deleteUserById(final AdminUserDefaultActivity adminUserDefaultActivity, String userObjId){
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("objectId",userObjId);
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    // iterate over all messages and delete them
                    for(User user : objects)
                    {
                        user.deleteInBackground();
                        doToastMessageInView(adminUserDefaultActivity, user.getName() + " deleted from database.");
                    }
                    adminUserDefaultActivity.retrieveUsers();
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminUserDefaultActivity, "ERROR: Couldnt delete user.");
                }
            }
        });
    }

}