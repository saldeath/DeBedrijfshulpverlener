package nl.debhver.debedrijfshulpverlener;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
        showProgressBar(true);
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

    private void setTrainingLists(final List<UserTraining> trainingList){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
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
        });
        t.start();
    }

    private void populateActivity(){
        LinearLayout list;
        TextView view;

        for(int i = 0; i < 4;i++) {
            List<UserTraining> tempList;

            if(i == 0) {
                list = (LinearLayout) findViewById(R.id.listViewAchieved);
                tempList = trainingListAchieved;
            } else if(i == 1) {
                list = (LinearLayout) findViewById(R.id.listViewScheduled);
                tempList = trainingListScheduled;
            } else if(i == 2) {
                list = (LinearLayout) findViewById(R.id.listViewExpired);
                tempList = trainingListExpired;
            } else if(i == 3) {
                list = (LinearLayout) findViewById(R.id.listViewFailed);
                tempList = trainingListFailed;
            } else {
                continue;
            }

            if(tempList.size() > 0) {
                for (UserTraining training : tempList) {
                    view = new TextView(this);
                    view.setText(training.toString() + "\n");
                    final TextView finalView = view;
                    final LinearLayout finalList = list;
                    list.post(new Runnable() {
                        @Override
                        public void run() {
                            finalList.addView(finalView);
                        }
                    });
                }
            } else {
                view = new TextView(this);
                view.setText(getString(R.string.nothing));
                final TextView finalView = view;
                final LinearLayout finalList = list;
                list.post(new Runnable() {
                    @Override
                    public void run() {
                        finalList.addView(finalView);
                    }
                });
            }
        }
        showProgressBar(false);
    }
}
