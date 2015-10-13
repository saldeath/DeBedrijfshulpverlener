package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public static String USER_EXTRA = "user_extra";
    private List<User> userList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_default);
        addListenerToSearchView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        retrieveUsers();
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
    }

    public void retrieveUsers(){
        DBManager.getInstance().getUsers(this);
    }

    public void setUserList(List<User> userListFromDatabase){ // new listreset the ListView
        this.userList = userListFromDatabase;
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
                Intent intent = new Intent(AdminUserDefaultActivity.this, AdminAddUserActivity.class);
                intent.putExtra(USER_EXTRA, user.getObjectId());
                AdminUserDefaultActivity.this.startActivity(intent);

            }
        });
        //intent.putExtra(USER_EXTRA,)
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
