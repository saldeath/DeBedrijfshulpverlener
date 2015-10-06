package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import nl.debhver.debedrijfshulpverlener.model.User;

/**
 * Created by Koen on 6-10-2015.
 */
public class AdminDefaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_default);

    }


    public void FABClicked(View v) {

        if (v.getId() == R.id.AdminAddUser) {
            // doStuff
            Intent intent = new Intent(AdminDefaultActivity.this, AdminAddUserActivity.class);
            AdminDefaultActivity.this.startActivity(intent);
            Log.i("Content ", " start AddUserActivity ");
        }

        if (v.getId() == R.id.AdminSearchUser) {
            // doStuff
            Intent intent = new Intent(AdminDefaultActivity.this, AdminSearchActivity.class);
            AdminDefaultActivity.this.startActivity(intent);
            Log.i("Content ", " start SearchUserActivity ");
        }
    }

    public void setUICreateUser(){
        // button + clicked, give the user options to create a user
    }

    public void setUISearchUsers(){

    }

    public void setUISelectedUser(){

    }

    public void createUser(){
        User userToCreate = new User();
        /*
        userToCreate.setName();
        userToCreate.setBranch();
        userToCreate.setEmail("");
        userToCreate.setEROFunction();
        userToCreate.setRight();
        userToCreate.setTelephoneNumber();
        */
    }


}
