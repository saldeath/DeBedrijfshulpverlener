package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nl.debhver.debedrijfshulpverlener.enums.IncidentType;
import nl.debhver.debedrijfshulpverlener.enums.UserRight;
import nl.debhver.debedrijfshulpverlener.models.Incident;
import nl.debhver.debedrijfshulpverlener.models.User;

public class HomeUserActivity extends HomeActivity {

    private ImageButton incidentButton;
    private Spinner incidentTypes;
    private EditText incidentLocation;
    private EditText incidentDescription;
    private User user = (User) User.getCurrentUser();
    private Calendar c = Calendar.getInstance();
    private static final int TAKE_FOTO_REQUEST = 0;
    private Bitmap equipmentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        incidentButton = (ImageButton) findViewById(R.id.incident_button);

        incidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(HomeUserActivity.this).inflate(R.layout.incident_dialog, null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeUserActivity.this);
                alertDialog.setView(view);

                initSpinner(view);

                incidentLocation = (EditText) view.findViewById(R.id.incident_location);
                incidentDescription = (EditText) view.findViewById(R.id.incident_description);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get selected item from spinner
                        int id = incidentTypes.getSelectedItemPosition();
                        IncidentType[] incidentTypes = IncidentType.values();

                        //save incident
                        Incident incident = new Incident();
                        incident.setDescription(incidentDescription.getText().toString());
                        incident.setLocation(incidentLocation.getText().toString());
                        incident.setUser(user);
                        incident.setType(incidentTypes[id]);
                        incident.setTime(getDate());

                        DBManager.getInstance().createIncident(incident, HomeUserActivity.this);

                    }
                });

                alertDialog.setNegativeButton("Cancel", null);
                alertDialog.setTitle("Incident Melden");
                Dialog dialog = alertDialog.create();
                dialog.show();

            }
        });

    }

    @NonNull
    private Date getDate() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    private void initSpinner(View view) {
        incidentTypes = (Spinner) view.findViewById(R.id.incident_type);
        String[] incidentTypesList = new String[]{IncidentType.FIRE.toString(), IncidentType.MEDICAL.toString()};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HomeUserActivity.this, android.R.layout.simple_spinner_dropdown_item, incidentTypesList);
        incidentTypes.setAdapter(dataAdapter);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_FOTO_REQUEST);
    }

    public void startCamera(View view){
        openCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_FOTO_REQUEST && resultCode == RESULT_OK){

            equipmentImage = (Bitmap) data.getExtras().get("data");
            Bitmap scaledIncident = Bitmap.createScaledBitmap(equipmentImage, 500, 500 * equipmentImage.getHeight() / equipmentImage.getWidth(), false);


            Toast.makeText(this, "camera captured", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(this, "nothing captured", Toast.LENGTH_LONG).show();
        }
    }
}
