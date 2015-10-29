package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import nl.debhver.debedrijfshulpverlener.enums.TrainingType;
import nl.debhver.debedrijfshulpverlener.models.Training;

public class AdminTrainingAddActivity extends HomeActivity {

    private Training oldTraining = null;
    private boolean ableToDeleteTraining = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String trainingObjectID = getIntent().getStringExtra("Training");
        if(trainingObjectID != null)
            setTitle(R.string.title_activity_admin_training_edit);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_add);
        setBackButtonOnToolbar(true);

        Button editBtn = (Button) findViewById(R.id.addTrainingButton);
        Button delBtn = (Button)findViewById(R.id.deleteTrainingButton);
        Button addUserToTrainingBtn = (Button)findViewById(R.id.addUserToTrainingbutton);
        fillSpinner();
        if(trainingObjectID != null)
        {
            editBtn.setText(getString(R.string.save));
            DBManager.getInstance().getTrainingbyID(this, trainingObjectID);
        }
        else
        {
            delBtn.setVisibility(View.GONE);
            addUserToTrainingBtn.setVisibility(View.GONE);
        }
    }

    void loadSingleTraining(Training object){
        showProgressBar(true);
        oldTraining = object;
        EditText name = (EditText)findViewById(R.id.inputTrainingName);
        EditText description = (EditText)findViewById(R.id.inputTrainingDescription);
        Spinner type = (Spinner)findViewById(R.id.trainingSpinner);
        if (oldTraining.getType().toString().equals("Basis"))
            type.setSelection(0);
        else
            type.setSelection(1);
        name.setText(oldTraining.getName());
        description.setText(oldTraining.getDescription());
        DBManager.getInstance().checkAvailibleUserTraining(oldTraining, AdminTrainingAddActivity.this);
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
            if(name.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
                Toast emptyFields = Toast.makeText(this, "Er zijn velden leeg", Toast.LENGTH_LONG);
                emptyFields.show();
            }
            else
                DBManager.getInstance().createTraining(t, this);
        }

    }
    public void ableToDeleteTraining(boolean bool)
    {
        this.ableToDeleteTraining = bool;
    }

    public void deleteTrainingClicked(View view) {
        if(ableToDeleteTraining == false)
        {
            Toast.makeText(this, "Cursus niet te verwijderen, er horen gebruikerscusrussen bij", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Waarschuwing");
            builder.setMessage("Weet je zeker dat je dit wilt verwijderen?");
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DBManager.getInstance().deleteTraining(oldTraining, AdminTrainingAddActivity.this);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public void fillSpinner()
    {
        Spinner typeSpinner = (Spinner)findViewById(R.id.trainingSpinner);
        String[] incidentTypesList = new String[]{TrainingType.BASIS.toString(), TrainingType.REPEAT.toString()};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, incidentTypesList);
        typeSpinner.setAdapter(adapter);
    }

    public void addUsertoTrainingClicked(View view) {
        ableToDeleteTraining = false;
        Intent i = new Intent(AdminTrainingAddActivity.this, AdminTrainingUserAddActivity.class);
        i.putExtra("Training", oldTraining.getObjectId());
        startActivity(i);
    }
}
