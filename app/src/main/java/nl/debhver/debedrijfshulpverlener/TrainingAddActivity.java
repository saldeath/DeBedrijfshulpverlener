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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;

import nl.debhver.debedrijfshulpverlener.models.Training;

public class TrainingAddActivity extends AppCompatActivity {

    //final EditText name = (EditText)findViewById(R.id.inputTrainingName);
    //final EditText description = (EditText)findViewById(R.id.inputTrainingDescription);
    //final EditText type = (EditText)findViewById(R.id.inputTrainingType);
    private Training oldTraining = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_add);
        Button btn = (Button) findViewById(R.id.addTrainingButton);
        Button delbtn = (Button)findViewById(R.id.deleteTrainingButton);
        String trainingObjectID = getIntent().getStringExtra("Training");
        if(trainingObjectID != null)
        {
            btn.setText("Update");
            DBManager.getInstance().getTrainingbyID(this, trainingObjectID);
        }
        else
        {
            delbtn.setVisibility(View.GONE);
        }
    }

    void loadSingleTraining(Training object){
        oldTraining = object;
        EditText name = (EditText)findViewById(R.id.inputTrainingName);
        EditText description = (EditText)findViewById(R.id.inputTrainingDescription);
        EditText type = (EditText)findViewById(R.id.inputTrainingType);
        name.setText(oldTraining.getName());
        description.setText(oldTraining.getDescription());
        type.setText(oldTraining.getType());
    }


    void popupShortToastMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void addTrainingClicked(View view) {
        EditText name = (EditText)findViewById(R.id.inputTrainingName);
        EditText description = (EditText)findViewById(R.id.inputTrainingDescription);
        EditText type = (EditText)findViewById(R.id.inputTrainingType);
            if(oldTraining != null)
            {
                oldTraining.setName(name.getText().toString());
                oldTraining.setDescription(description.getText().toString());
                oldTraining.setType(type.getText().toString());
                DBManager.getInstance().updateTraining(oldTraining, this);
            }
            else
            {
                Training t = new Training();
                t.setName(name.getText().toString());
                t.setDescription(description.getText().toString());
                t.setType(type.getText().toString());
                DBManager.getInstance().createTraining(t, this);
            }

        }

    public void deleteTrainingClicked(View view) {
        DBManager.getInstance().deleteTraining(oldTraining, this);
    }
}


