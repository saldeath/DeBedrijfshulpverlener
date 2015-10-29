package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Training;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

public class AdminUserTraining extends HomeActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_training);
        setBackButtonOnToolbar(false);
        fillUserTrainingList();
    }

    private void fillUserTrainingList()
    {
        showProgressBar(true);
        DBManager.getInstance().getAllUserTraining(AdminUserTraining.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillUserTrainingList();
    }

    public void populateUserTrainingList(final List<UserTraining> objects)
    {
        ListView userTrainingListView = (ListView)findViewById(R.id.userTraininglistView);
        final UserTrainingAdapter userTrainingAdapter = new UserTrainingAdapter(AdminUserTraining.this, objects);
        userTrainingListView.setAdapter(userTrainingAdapter);

        userTrainingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AdminUserTraining.this, AdminEditUserTraining.class);
                i.putExtra("UserTraining", objects.get(position).getObjectId());
                startActivity(i);
            }
        });
        showProgressBar(false);
    }



}
