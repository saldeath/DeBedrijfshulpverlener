package nl.debhver.debedrijfshulpverlener;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.parse.FunctionCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.User;

/**
 * Created by Koen on 6-10-2015.
 */
public class AdminUserDefaultActivity extends HomeActivity {
    public static int FILTER_PICK = 1;
    public static String FILTER_EXTRA_ERO = "filter_extra_ero";
    public static String FILTER_EXTRA_RIGHTS = "filter_extra_rights";
    public static String USER_EXTRA = "user_extra";
    private boolean FILTER_RECEIVED = false;
    private List<User> userList = null;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_default);
        addListenerToSearchView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(FILTER_RECEIVED==false){
            retrieveUsers();
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        FILTER_RECEIVED = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == FILTER_PICK) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                FILTER_RECEIVED = true;
                Bundle bundleObject = data.getExtras();
                DBManager.getInstance().getFilteredUserList(this, bundleObject.getStringArrayList(FILTER_EXTRA_RIGHTS), bundleObject.getStringArrayList(FILTER_EXTRA_ERO));
            }
        }
    }

    public void addListenerToSearchView(){
        final SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchViewString) {

                if (searchView.getWidth() > 0) {
                    filterUserListOnlyByName(searchViewString);
                }

                return false;
            }
        });
    }

    public void FABClicked(View v) {

        if (v.getId() == R.id.AdminAddUser) {
            // doStuff
            Intent intent = new Intent(AdminUserDefaultActivity.this, AdminAddUserActivity.class);
            AdminUserDefaultActivity.this.startActivity(intent);
            Log.i("Content ", " start AddUserActivity ");
        }
        else if(v.getId() == R.id.AdminFilterUserList){
            Intent intent = new Intent(AdminUserDefaultActivity.this, AdminUserFilterActivity.class);
            AdminUserDefaultActivity.this.startActivityForResult(intent, FILTER_PICK);
            Log.i("Content ", " start AdminUserFilterActivity ");
        }
    }

    public void retrieveUsers(){
        showProgressBar(true);
        DBManager.getInstance().getUsers(this);
    }

    public void setUserList(List<User> userListFromDatabase){ // new listreset the ListView
        this.userList = userListFromDatabase;
        System.out.println("size " + userListFromDatabase.size());
        for(User user : userListFromDatabase){
            System.out.println( "set userlist " + user.getName());
        }
        prepareListData(userList);
        //populateListView(userList);
    }

    public void filterUserListOnlyByName(String nameMustContain){
        List<User> tempUserList = new ArrayList<User>(userList);

        List<User> toRemove = new ArrayList<User>();

        for(User user: tempUserList){
            if(containsIgnoreCase(user.toString(),nameMustContain)==false){ // user does not contain string
                toRemove.add(user);
            }

        }
        tempUserList.removeAll(toRemove);
        prepareListData(tempUserList);
        //populateListView(userList);
    }

//    public void populateListView(List<User> userListPara){
//        final ListView userListView = (ListView)findViewById(R.id.userListView);
//        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_dropdown_item, userListPara);
//        userListView.setAdapter(adapter);
//        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                User user = (User) userListView.getItemAtPosition(position);
//                if (user == null) {
//                    Log.d("AdminDefaultUser", "null user ??");
//                } else {
//                    Log.d("AdminDefaultUser", user.getName() + "selected");
//                }
//                Intent intent = new Intent(AdminUserDefaultActivity.this, AdminAddUserActivity.class);
//                intent.putExtra(USER_EXTRA, user.getObjectId());
//                AdminUserDefaultActivity.this.startActivity(intent);
//
//            }
//        });
//        //intent.putExtra(USER_EXTRA,)
//    }

    // code by http://stackoverflow.com/users/1705598/icza
    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public void prepareListData(final List<User> userListPara) {
        expListView = (ExpandableListView) findViewById(R.id.userExpendableListView);
        expListView.setGroupIndicator(null);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> options = new ArrayList<String>();
        options.add(getResources().getString(R.string.edit));
        options.add(getResources().getString(R.string.delete));

        List<Drawable> icons = new ArrayList<>();
        icons.add(ContextCompat.getDrawable(this, R.drawable.ic_edit));
        icons.add(ContextCompat.getDrawable(this, R.drawable.ic_recycle_bin));
        // Adding header/child data
        int i = 0;
        for (User u : userListPara) {
            listDataHeader.add(i, u.toString());
            listDataChild.put(listDataHeader.get(i), options); // Header, Child data
            i++;
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, icons);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, int childPosition, long id) {
                if (childPosition == 0) {
                    System.out.println(userListPara.get(groupPosition).getName());
                    Intent intent = new Intent(AdminUserDefaultActivity.this, AdminAddUserActivity.class);
                    intent.putExtra(USER_EXTRA, userListPara.get(groupPosition).getObjectId());
                    AdminUserDefaultActivity.this.startActivity(intent);
                }
                else if(childPosition == 1){

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdminUserDefaultActivity.this);

                    alertDialogBuilder
                            .setTitle(R.string.warning)
                            .setMessage(getString(R.string.warning_delete_user) + "\n" + userListPara.get(groupPosition).toString())
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String objectId = userListPara.get(groupPosition).getObjectId();
                                    DBManager.getInstance().deleteUserById(AdminUserDefaultActivity.this , userListPara.get(groupPosition).getObjectId(), new FunctionCallback<String>(){
                                        @Override
                                        public void done(String object, ParseException e) {
                                            if (e == null) {
                                                popupShortToastMessage(getString(R.string.user_delete_succes));
                                                retrieveUsers();
                                            } else {
                                                Log.d("ParseError", e.toString());
                                                popupShortToastMessage(getString(R.string.user_delete_error));
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                return true;
            }
        });
		showProgressBar(false);
    }

}
