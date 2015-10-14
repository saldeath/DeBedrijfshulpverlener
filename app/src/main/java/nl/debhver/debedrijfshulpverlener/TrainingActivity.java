package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Training;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.trainingAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrainingActivity.this, TrainingAddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retreiveTraining();
    }

    private void retreiveTraining() {
        DBManager.getInstance().getAllTraining(this);
    }

    void popupShortToastMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void populateTrainingList(final List<Training> trainings) {

        final ListView trainingListView = (ListView)findViewById(R.id.traininglistView);
        ArrayAdapter<Training> adapter = new ArrayAdapter<Training>(this, android.R.layout.simple_spinner_dropdown_item, trainings);
        trainingListView.setAdapter(adapter);


        trainingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = trainings.get(position).getName();
                Intent i = new Intent(TrainingActivity.this, TrainingAddActivity.class);
                i.putExtra("Training", trainings.get(position).getObjectId());
                startActivity(i);
            }
        });


    }




}
