package nl.debhver.debedrijfshulpverlener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
<<<<<<< HEAD
<<<<<<< HEAD

import com.parse.SendCallback;

=======
import com.parse.SendCallback;
>>>>>>> origin/master
=======
import com.parse.SendCallback;
>>>>>>> parent of 4f9d065... Trainingadd
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Branch;
<<<<<<< HEAD
<<<<<<< HEAD

import nl.debhver.debedrijfshulpverlener.models.Incident;

import nl.debhver.debedrijfshulpverlener.models.Training;

=======
import nl.debhver.debedrijfshulpverlener.models.Incident;
>>>>>>> origin/master
=======
import nl.debhver.debedrijfshulpverlener.models.Incident;
>>>>>>> parent of 4f9d065... Trainingadd
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

<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> origin/master
=======
>>>>>>> parent of 4f9d065... Trainingadd
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
        pushIncident(i);
    }

    void pushIncident(final Incident i){
        ParsePush parsePush = new ParsePush();
        parsePush.setMessage(i.getDescription() + " @ " + i.getLocation());
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d("ParseSuccess", i.getDescription() + " @ " + i.getLocation());
                }
                else{
                    Log.d("ParseError", e.toString());
                }
            }
        });
    }

<<<<<<< HEAD
    public void subscribeUserToBranch(){
        User user = (User) User.getCurrentUser();
        if(user != null){
            try {
                String branch = user.getBranch().fetchIfNeeded().toString();
                ParsePush.subscribeInBackground(branch);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }
<<<<<<< HEAD

    static void createFakeBranchesAndAdminRights() {
        Branch b = new Branch();
        Branch c = new Branch();
        b.setName("coolName");
        c.setName("secondBranchName");
    }
=======
>>>>>>> parent of 4f9d065... Trainingadd

    public void unsubscribeUserFromBranch(){
        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");

        for(String channel : subscribedChannels){
            ParsePush.unsubscribeInBackground(channel);
        }
    }

<<<<<<< HEAD
    void createFakeTraining() {
        Training tss = new Training();
        tss.setName("Jaarlijkese Ehbo");
        tss.setDescription("Ehbo leuk man");
        tss.setType("EHBO ");
        tss.saveInBackground();

    }

    void createUser(User u, final AdminAddUserActivity adminAddUserActivity) {
=======
    void createUser(User u, final AdminAddUserActivity adminAddUserActivity){
>>>>>>> origin/master
=======
    void createUser(User u, final AdminAddUserActivity adminAddUserActivity){
>>>>>>> parent of 4f9d065... Trainingadd
        u.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    doToastMessageInView(adminAddUserActivity, "User saved to database.");
                    adminAddUserActivity.clearFieldsAfterAddingUser();
                } else {
                    Log.d("ParseError", e.toString());
<<<<<<< HEAD
<<<<<<< HEAD

                    doToastMessageInView(adminAddUserActivity, "ERROR: User was not saved to database.");

                    adminAddUserActivity.popupShortToastMessage("ERROR: User was not saved to database.");

=======
                    doToastMessageInView(adminAddUserActivity, "ERROR: User was not saved to database.");
>>>>>>> origin/master
=======
                    doToastMessageInView(adminAddUserActivity, "ERROR: User was not saved to database.");
>>>>>>> parent of 4f9d065... Trainingadd
                }
            }
        });
    }

<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> origin/master
=======
>>>>>>> parent of 4f9d065... Trainingadd
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

<<<<<<< HEAD
<<<<<<< HEAD
    void getBranches(final AdminBranchDefaultActivity adminBranchDefaultActivity){
        ParseQuery<Branch> query = ParseQuery.getQuery(Branch.class);
        query.findInBackground(new FindCallback<Branch>() {
            public void done(List<Branch> objects, ParseException e) {
                if (e == null) {
                    adminBranchDefaultActivity.setBranchList(objects);
                    adminBranchDefaultActivity.prepareListData(objects);
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(adminBranchDefaultActivity, "ERROR: Failed to retrieve users.");
                }
            }
        });
    }

    void getBranchNames(final AdminAddUserActivity adminAddUserActivity) {

=======
    public List<ParseObject> getBranches() {
>>>>>>> origin/master
=======
    public List<ParseObject> getBranches() {
>>>>>>> parent of 4f9d065... Trainingadd
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
<<<<<<< HEAD
<<<<<<< HEAD

                    doToastMessageInView(adminAddBranchActivity, "ERROR: Branch was not saved to database.");


=======
                    doToastMessageInView(adminAddBranchActivity, "ERROR: Branch was not saved to database.");
>>>>>>> origin/master
=======
                    doToastMessageInView(adminAddBranchActivity, "ERROR: Branch was not saved to database.");
>>>>>>> parent of 4f9d065... Trainingadd
                }
            }
        });
    }

<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> origin/master
=======
>>>>>>> parent of 4f9d065... Trainingadd
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
<<<<<<< HEAD
<<<<<<< HEAD
                }
            }

        });
    }

    void createTraining(Training training, final TrainingAddActivity trainingAddActivity) {
        training.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    trainingAddActivity.popupShortToastMessage("Saved Succesfully");
                    trainingAddActivity.finish();
                } else {
                    trainingAddActivity.popupShortToastMessage("Unable to save");

=======
>>>>>>> origin/master
=======
>>>>>>> parent of 4f9d065... Trainingadd
                }
            }
        });
    }
<<<<<<< HEAD
<<<<<<< HEAD


    void doToastMessageInView(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();


    void updateTraining(Training training, final TrainingAddActivity trainingAddActivity)
    {
        training.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    trainingAddActivity.popupShortToastMessage("Updated Succesfully");
                    trainingAddActivity.finish();
                } else {
                    trainingAddActivity.popupShortToastMessage("Unable to save");
                }
            }
        });
    }

    void deleteTraining(Training oldTraining, final TrainingAddActivity trainingAddActivity) {
        oldTraining.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    trainingAddActivity.popupShortToastMessage("Deleted Succesfully");
                    trainingAddActivity.finish();

                } else {
                    trainingAddActivity.popupShortToastMessage("Unable to Delete");
                }
            }
        });
    }


    void getAllTraining(final TrainingActivity trainingActivity) {
        ParseQuery<Training> query = ParseQuery.getQuery("training");
        query.findInBackground(new FindCallback<Training>() {
            public void done(List<Training> objects, ParseException e) {
                if (e == null) {
                    trainingActivity.populateTrainingList(objects);
                } else {
                    Log.d("ParseError", e.toString());
                    trainingActivity.popupShortToastMessage("ERROR: Nothing was retrieved from database.");
                }
            }
        });
    }

    void getTrainingbyID(final TrainingAddActivity trainingAddActivity, String trainingObjectId) {
        ParseQuery<Training> query = ParseQuery.getQuery("training");
        query.getInBackground(trainingObjectId, new GetCallback<Training>() {
            @Override
            public void done(Training object, com.parse.ParseException e) {
                if (e == null) {
                    trainingAddActivity.loadSingleTraining(object);
                } else {
                    // something went wrong
                }
            }
        });
=======

    void doToastMessageInView(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
>>>>>>> origin/master
=======

    void doToastMessageInView(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
>>>>>>> parent of 4f9d065... Trainingadd
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

    void getSingleBranchById(final AdminAddBranchActivity adminAddBranchActivity, String branchObjId){
        ParseQuery<Branch> query = ParseQuery.getQuery(Branch.class);
        query.whereEqualTo("objectId",branchObjId);
        query.findInBackground(new FindCallback<Branch>() {
            public void done(List<Branch> objects, ParseException e) {
                if (e == null) {
                    adminAddBranchActivity.loadSingleBranchDetails(objects);
                } else {
                    // error
                }
            }
        });
    }

}