package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.Training;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

public class TrainingActivity extends HomeActivity{

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
        retreiveUserTraining();
    }

    private void retreiveUserTraining() {
        DBManager.getInstance().getAllTraining(this);
    }

    void popupShortToastMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void populateTrainingList(final List<Training> trainings) {
        final ListView trainingListView = (ListView)findViewById(R.id.traininglistView);
        final TrainingAdapter trainingAdapter = new TrainingAdapter(TrainingActivity.this, trainings);
        trainingListView.setAdapter(trainingAdapter);
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
