package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.enums.TrainingType;
import nl.debhver.debedrijfshulpverlener.models.Training;

public class TrainingAddActivity extends AppCompatActivity {

    private Training oldTraining = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_add);
        Button editBtn = (Button) findViewById(R.id.addTrainingButton);
        Button delBtn = (Button)findViewById(R.id.deleteTrainingButton);
        String trainingObjectID = getIntent().getStringExtra("Training");
        fillSpinner();
        if(trainingObjectID != null)
        {
            editBtn.setText("Update");
            DBManager.getInstance().getTrainingbyID(this, trainingObjectID);
        }
        else
        {
            delBtn.setVisibility(View.GONE);
        }
    }

    void loadSingleTraining(Training object){
        oldTraining = object;
        EditText name = (EditText)findViewById(R.id.inputTrainingName);
        EditText description = (EditText)findViewById(R.id.inputTrainingDescription);
        //Spinner type = (Spinner)findViewById(R.id.trainingSpinner);
        name.setText(oldTraining.getName());
        description.setText(oldTraining.getDescription());

    }


    void popupShortToastMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void addTrainingClicked(View view) {
        EditText name = (EditText)findViewById(R.id.inputTrainingName);
        EditText description = (EditText)findViewById(R.id.inputTrainingDescription);
        Spinner typeSpinner = (Spinner)findViewById(R.id.trainingSpinner);
        TrainingType[] trainingsTypes = TrainingType.values();
        int id = typeSpinner.getSelectedItemPosition();
        if(oldTraining != null)
        {
            oldTraining.setName(name.getText().toString());
            oldTraining.setDescription(description.getText().toString());
            oldTraining.setType(trainingsTypes[id]);
            DBManager.getInstance().updateTraining(oldTraining, this);
        }
        else
        {
            Training t = new Training();
            t.setName(name.getText().toString());
            t.setDescription(description.getText().toString());
            t.setType(trainingsTypes[id]);
            DBManager.getInstance().createTraining(t, this);
        }

    }

    public void deleteTrainingClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DBManager.getInstance().deleteTraining(oldTraining, TrainingAddActivity.this);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    public void fillSpinner()
    {
        Spinner typeSpinner = (Spinner)findViewById(R.id.trainingSpinner);
        String[] incidentTypesList = new String[]{TrainingType.BASIS.toString(), TrainingType.REPEAT.toString()};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, incidentTypesList);
        typeSpinner.setAdapter(adapter);
    }
}
