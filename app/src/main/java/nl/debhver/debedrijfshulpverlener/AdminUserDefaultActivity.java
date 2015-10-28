package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
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
        populateListView(userList);
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
        populateListView(tempUserList);
    }

    public void populateListView(List<User> userListPara){
        final ListView userListView = (ListView)findViewById(R.id.userListView);
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_dropdown_item, userListPara);
        userListView.setAdapter(adapter);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                User user = (User) userListView.getItemAtPosition(position);
                if(user == null){
                    Log.d("AdminDefaultUser","null user ??");
                }
                else{
                    Log.d("AdminDefaultUser", user.getName() + "selected");
                }
                Intent intent = new Intent(AdminUserDefaultActivity.this, AdminAddUserActivity.class);
                intent.putExtra(USER_EXTRA, user.getObjectId());
                AdminUserDefaultActivity.this.startActivity(intent);

            }
        });
        //intent.putExtra(USER_EXTRA,)
        showProgressBar(false);
    }

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

}
