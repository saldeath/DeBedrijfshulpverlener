package nl.debhver.debedrijfshulpverlener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.enums.UserEROFunction;
import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.Equipment;
import nl.debhver.debedrijfshulpverlener.models.EvacuationPlan;
import nl.debhver.debedrijfshulpverlener.models.ImageModel;
import nl.debhver.debedrijfshulpverlener.models.Incident;
import nl.debhver.debedrijfshulpverlener.models.Training;
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

	public void save(ParseObject object, final SaveCallback callback){
        object.saveInBackground(callback);
    }

    public void delete(ParseObject object, final DeleteCallback callback){
        object.deleteInBackground(callback);
    }

    public void getListParseObjects(Table table, final FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(table.toString());
        query.findInBackground(callback);
    }

    public void getParseObjectById(Table table, String objectId, final FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(table.toString());
        query.whereEqualTo("objectId",objectId);
        query.findInBackground(callback);
    }

    public void getListParseObjects(Table table, Map<String, List<String>> argsEquelTo, final FindCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(table.toString());
        for (Map.Entry<String, List<String>> entry : argsEquelTo.entrySet()) {
            for(String arg : entry.getValue()) {
                query.whereEqualTo(entry.getKey(),arg);
            }
        }
        query.findInBackground(callback);
    }

    void createIncident(final Incident i, final HomeUserActivity homeUserActivity){
        i.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    doToastMessageInView(homeUserActivity, "Incident saved to database.");
                    pushIncident(i);
                } else {
                    Log.d("ParseError", e.toString());
                    doToastMessageInView(homeUserActivity, "ERROR: Incident was not saved to database.");
                }
            }
        });

    }



    void pushIncident(final Incident i){
        Log.d("pushIncident", "starting push incident");
        ParsePush parsePush = new ParsePush();
        User user = (User) User.getCurrentUser();
        try {
            Log.d("pushIncident", "try1");
            String channel = user.getBranch().fetchIfNeeded().toString();
            JSONObject data = new JSONObject();
            try{
                Log.d("pushIncident", "try2");
                data.put("title", "Alarm");
                data.put("description", i.getDescription());
                data.put("location", i.getLocation());
                data.put("incidentId", i.getObjectId());
                parsePush.setData(data);
                parsePush.setChannel(channel);
                parsePush.sendInBackground(new SendCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("ParseSuccess", i.getDescription() + " @ " + i.getLocation());
                        } else {
                            Log.d("ParseError", e.toString());
                        }
                    }
                });
            } catch(JSONException ex) {
                ex.printStackTrace();
            }


        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }



    }

    public void subscribeUserToBranch(){
        final User user = (User) User.getCurrentUser();
        if(user != null){
            try {
                final String branch = user.getBranch().fetchIfNeeded().toString();
                branch.replaceAll("\\s", ""); // remove whitespaces
                ParsePush.subscribeInBackground(branch, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Log.d("ParseSuccess", "Subscribed to " + branch);

                        }
                        else{
                            Log.d("ParseError", e.toString());
                        }
                    }
                });
                final String userId  = user.getObjectId();
                branch.replaceAll("\\s", ""); // remove whitespaces
                ParsePush.subscribeInBackground(userId, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Log.d("ParseSuccess", "Subscribed to " + userId);

                        }
                        else{
                            Log.d("ParseError", e.toString());
                        }
                    }
                });
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public void unsubscribeUserFromBranch() {
        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        if(parseInstallation != null){
            List<String> subscribedChannels = parseInstallation.getList("channels");
            if(subscribedChannels!=null){
                if(!subscribedChannels.isEmpty()){
                    for(String channel : subscribedChannels){
                        ParsePush.unsubscribeInBackground(channel);
                    }
                }
            }
        }
    }

    void createUser(User user, String password, final AdminAddUserActivity adminAddUserActivity) {
        Log.d("cloudecode", user.getName());
        Log.d("cloudecode", user.getTelephoneNumber());
        Log.d("cloudecode", user.getEmail());
        Log.d("cloudecode", password);
        Log.d("cloudecode", user.getEROFunction().toString());
        Log.d("cloudecode", user.getRight().toString());

        JSONArray jArray = new JSONArray();
        for(UserEROFunction userEROFunction : user.getEROFunction()){
            jArray.put(userEROFunction.name());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", user.getUsername() );
        params.put("branch", user.getBranch().getObjectId());
        params.put("name", user.getName() );
        params.put("email", user.getEmail());
        params.put("password", password);
        params.put("ERO_function", jArray);
        params.put("right", user.getRight().name() );
        params.put("telephone_number", user.getTelephoneNumber());
        ParseCloud.callFunctionInBackground("addUser", params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (e == null) {
                    adminAddUserActivity.popupShortToastMessage("OK: " + object);
                    adminAddUserActivity.setSaved(true);
                    System.out.println(object);
                } else {
                    adminAddUserActivity.popupShortToastMessage("ERROR: " + e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    void updateUser(User user, final AdminAddUserActivity adminAddUserActivity){
        Log.d("cloudecode", user.getEROFunction().toString());

        JSONArray jArray = new JSONArray();
        for(UserEROFunction userEROFunction : user.getEROFunction()){
            jArray.put(userEROFunction.name());
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", user.getUsername() );
        params.put("branch", user.getBranch().getObjectId());
        params.put("name", user.getName() );
        params.put("ERO_function", jArray);
        params.put("right", user.getRight().name() );
        params.put("telephone_number",user.getTelephoneNumber());
        ParseCloud.callFunctionInBackground("modifyUser", params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (e == null) {
                    adminAddUserActivity.popupShortToastMessage("OK: " + object);
                    adminAddUserActivity.setSaved(true);
                } else {
                    adminAddUserActivity.popupShortToastMessage("ERROR: " + e.getMessage());
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
                    for(User userRec : objects){
                        System.out.println(userRec.getName());
                    }
                    adminDefaultActivity.setUserList(objects);
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
                    for (User user : objects) {
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

    void getBranchesOld(final AdminBranchDefaultActivity adminBranchDefaultActivity){
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
    void getBranchForUser(final UserEquipmentDefaultActivity userEquipmentDefaultActivity, String branchObjectId){
        ParseQuery<Branch> query = ParseQuery.getQuery(Branch.class);
        query.whereEqualTo("objectId",branchObjectId);
        query.findInBackground(new FindCallback<Branch>() {
            public void done(List<Branch> objects, ParseException e) {
                if (e == null) {
                    getEquipmentByUserBranch(userEquipmentDefaultActivity, objects.get(0));
                } else {
                    // error
                }
            }
        });
    }
    void getBranchForUser(final UserIncidentDefaultActivity userIncidentDefaultActivity, String branchObjectId){
        ParseQuery<Branch> query = ParseQuery.getQuery(Branch.class);
        query.whereEqualTo("objectId",branchObjectId);
        query.findInBackground(new FindCallback<Branch>() {
            public void done(List<Branch> objects, ParseException e) {
                if (e == null) {
                    getIncidentsByUserBranch(userIncidentDefaultActivity, objects.get(0));
                } else {
                    // error
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

                }
            }
        });
    }

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
    }

    void getEquipmentByUserBranch(final UserEquipmentDefaultActivity userEquipmentDefaultActivity, Branch branchObject){

        ParseQuery<Equipment> query = ParseQuery.getQuery(Equipment.class);
        query.whereEqualTo("branch",branchObject);
        query.findInBackground(new FindCallback<Equipment>() {
            public void done(List<Equipment> objects, ParseException e) {
                if (e == null) {
                    userEquipmentDefaultActivity.setEquipmentList(objects);
                } else {
                    Log.d("ParseError", e.toString());
                    userEquipmentDefaultActivity.popupShortToastMessage("ERROR: Nothing was retrieved from database.");
                }
            }
        });
    }
    void getIncidentsByUserBranch(final UserIncidentDefaultActivity userIncidentDefaultActivity, Branch branchObject){

        ParseQuery<Incident> query = ParseQuery.getQuery(Incident.class);
        query.whereEqualTo("branch",branchObject);
        query.findInBackground(new FindCallback<Incident>() {
            public void done(List<Incident> objects, ParseException e) {
                if (e == null) {
                    userIncidentDefaultActivity.setIncidentList(objects);
                } else {
                    Log.d("ParseError", e.toString());
                    userIncidentDefaultActivity.popupShortToastMessage("ERROR: Nothing was retrieved from database.");
                }
            }
        });
    }

    void getSingleIncidentById(final IncidentOpener incidentOpener, String incidentId){
        ParseQuery<Incident> query = ParseQuery.getQuery(Incident.class);
        query.whereEqualTo("objectId",incidentId);
        query.findInBackground(new FindCallback<Incident>() {
            public void done(List<Incident> objects, ParseException e) {
                if (e == null) {
                    if(!objects.isEmpty()){
                        for(Incident incident : objects){
                            incidentOpener.loadIncidentDetails(incident);
                            try{
                                ImageModel image = incident.getImage();
                                if(image != null){
                                    image = image.fetchIfNeeded();
                                    ParseFile imageParseFile = image.getParseFile("image");
                                    imageParseFile.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if (e == null) {
                                                incidentOpener.loadIncidentPhoto(data);
                                            } else {
                                            }
                                        }
                                    });
                                }
                            } catch (ParseException f) {
                                f.printStackTrace();
                            }
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    public void getSingleEuipmentById(final UserEquipmentShowActivity userEquipmentShowActivity, String equipmentObjId) {
        ParseQuery<Equipment> query = ParseQuery.getQuery(Equipment.class);
        query.whereEqualTo("objectId",equipmentObjId);
        query.findInBackground(new FindCallback<Equipment>() {
            public void done(List<Equipment> objects, ParseException e) {
                if (e == null) {
                    userEquipmentShowActivity.loadSingleBranchDetails(objects);
                } else {
                    // error
                }
            }
        });
    }

    public void getEvacuationPlansFromCurrentBranch(final SingleEmergencyDetailsActivity singleEmergencyDetailsActivity){
        User user = (User) User.getCurrentUser();
        Branch branch = null;
        try {
            branch = user.getBranch().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(branch != null){
            ParseQuery<EvacuationPlan> query = ParseQuery.getQuery(EvacuationPlan.class);
            query.whereEqualTo("branch", branch);
            query.findInBackground(new FindCallback<EvacuationPlan>() {
                public void done(List<EvacuationPlan> objects, ParseException e) {
                    if (e == null) {
                        if(!objects.isEmpty()){
                            singleEmergencyDetailsActivity.createDropdownWithFloors(objects);
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void getEvacuationPlan(final SingleEmergencyDetailsActivity singleEmergencyDetailsActivity, EvacuationPlan evacuationPlan){
        try{
            ImageModel image = evacuationPlan.getEvacuationPlan();
            if(image != null){
                image = image.fetchIfNeeded();
                ParseFile imageParseFile = image.getParseFile("image");
                imageParseFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            singleEmergencyDetailsActivity.loadEvacuationPlan(data);
                        } else {
                        }
                    }
                });
            }
        } catch (ParseException f) {
            f.printStackTrace();
        }
    }

}