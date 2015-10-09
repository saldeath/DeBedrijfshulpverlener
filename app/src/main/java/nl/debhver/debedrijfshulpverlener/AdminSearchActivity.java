package nl.debhver.debedrijfshulpverlener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.User;

/**
 * Created by Koen on 6-10-2015.
 */
public class AdminSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_users);
        retrieveUsers();
    }

    public void retrieveUsers(){
        DBManager.getInstance().getUsers(this);
    }


    public void populateListView(List<User> userList){
        ListView userListView = (ListView)findViewById(R.id.userListView);

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_dropdown_item, userList);

        userListView.setAdapter(adapter);





  //      List<User> items = (ArrayList)userList;

  //      ArrayAdapter<User> adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, items);

    }

}