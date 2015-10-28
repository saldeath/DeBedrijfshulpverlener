package nl.debhver.debedrijfshulpverlener;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

public class UserTrainingDefaultActivity extends HomeActivity {
    private List<UserTraining> trainingListAchieved;
    private List<UserTraining> trainingListScheduled;
    private List<UserTraining> trainingListExpired;
    private List<UserTraining> trainingListFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_training_default);
    }

    @Override
    protected void onResume(){
        super.onResume();
        retrieveTrainings();
    }

    private void retrieveTrainings(){
        Map<String, List<Object>> args = new HashMap<String, List<Object>>();
        List<Object> usersObject = new ArrayList<>();
        usersObject.add(getUser());
        args.put("user", usersObject);
        DBManager.getInstance().getListParseObjects(Table.USERTRAINING, args, new FindCallback<UserTraining>() {
            @Override
            public void done(List<UserTraining> objects, ParseException e) {
                if (e == null)
                    setTrainingLists(objects);
            }
        });
    }

    private void setTrainingLists(List<UserTraining> trainingList){ // new listreset the ListView
        trainingListAchieved = new ArrayList<>();
        trainingListScheduled = new ArrayList<>();
        trainingListExpired = new ArrayList<>();
        trainingListFailed = new ArrayList<>();

        for(UserTraining training : trainingList) {
            if(training.isExpired()) {
                trainingListExpired.add(training);
            } else if(training.isAchieved() && !training.isExpired()) {
                trainingListAchieved.add(training);
            } else if(training.isScheduled()) {
                trainingListScheduled.add(training);
            } else if(training.isFailed()) {
                trainingListFailed.add(training);
            } else {
                trainingListScheduled.add(training);
            }
        }

        populateActivity();
    }

    private void populateActivity(){
        LinearLayout list;
        TextView view;

        list  = (LinearLayout)findViewById(R.id.listViewAchieved);
        if(trainingListAchieved.size() > 0) {
            for (UserTraining training : trainingListAchieved) {
                view = new TextView(this);
                view.setText(training.toString());
                list.addView(view);
            }
        } else {
            view = new TextView(this);
            view.setText(getString(R.string.nothing));
            list.addView(view);
        }

        list  = (LinearLayout)findViewById(R.id.listViewScheduled);
        if(trainingListScheduled.size() > 0) {
            for (UserTraining training : trainingListScheduled) {
                view = new TextView(this);
                view.setText(training.toString());
                list.addView(view);
            }
        } else {
            view = new TextView(this);
            view.setText(getString(R.string.nothing));
            list.addView(view);
        }

        list  = (LinearLayout)findViewById(R.id.listViewExpired);
        if(trainingListExpired.size() > 0) {
            for (UserTraining training : trainingListExpired) {
                view = new TextView(this);
                view.setText(training.toString());
                list.addView(view);
            }
        } else {
            view = new TextView(this);
            view.setText(getString(R.string.nothing));
            list.addView(view);
        }

        list  = (LinearLayout)findViewById(R.id.listViewFailed);
        if(trainingListFailed.size() > 0) {
            for (UserTraining training : trainingListFailed) {
                view = new TextView(this);
                view.setText(training.toString());
                list.addView(view);
            }
        } else {
            view = new TextView(this);
            view.setText(getString(R.string.nothing));
            list.addView(view);
        }
        /*ArrayAdapter<UserTraining> adapter;
        ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[] { getString(R.string.nothing) });
        ListView listView;

        listView = (ListView) findViewById(R.id.listViewAchieved);
        if(trainingListAchieved.size() > 0) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trainingListAchieved);
            listView.setAdapter(adapter);
        } else {
            listView.setAdapter(emptyAdapter);
        }

        listView = (ListView) findViewById(R.id.listViewScheduled);
        if(trainingListScheduled.size() > 0) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trainingListScheduled);
            listView.setAdapter(adapter);
        } else {
            listView.setAdapter(emptyAdapter);
        }

        listView = (ListView) findViewById(R.id.listViewExpired);
        if(trainingListExpired.size() > 0) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trainingListExpired);
            listView.setAdapter(adapter);
        } else {
            listView.setAdapter(emptyAdapter);
        }

        listView = (ListView) findViewById(R.id.listViewFailed);
        if(trainingListFailed.size() > 0) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trainingListFailed);
            listView.setAdapter(adapter);
        } else {
            listView.setAdapter(emptyAdapter);
        }*/
    }
}
