package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.Training;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

public class UserTrainingDefaultActivity extends HomeActivity {
    public static String USERTRAINING_EXTRA_OBJID = "training_extra_objid";
    private List<UserTraining> trainingList = null;

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
        String columnExists = "date_achieved";
        DBManager.getInstance().getListParseObjects(Table.USERTRAINING, args, columnExists,new FindCallback<UserTraining>() {
            @Override
            public void done(List<UserTraining> objects, ParseException e) {
                if(e==null)
                    setTrainingList(objects);
            }
        });
    }

    private void setTrainingList(List<UserTraining> trainingList){ // new listreset the ListView
        this.trainingList = trainingList;
        populateListView(trainingList);
    }

    private void populateListView(List<UserTraining> trainingListPara){
        final ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<UserTraining> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trainingListPara);
        listView.setAdapter(adapter);
    }
}
