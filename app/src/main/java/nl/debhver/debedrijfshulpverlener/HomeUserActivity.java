package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import nl.debhver.debedrijfshulpverlener.enums.IncidentType;

public class HomeUserActivity extends HomeActivity {

    private ImageButton incidentButton;
    private Spinner incidentTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        incidentButton = (ImageButton)findViewById(R.id.incident_button);


        incidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(HomeUserActivity.this).inflate(R.layout.incident_dialog,null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeUserActivity.this);
                alertDialog.setView(view);

                incidentTypes = (Spinner)view.findViewById(R.id.incident_type);
                String[] incidentTypesList = new String[]{IncidentType.FIRE.toString(), IncidentType.MEDICAL.toString()};

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HomeUserActivity.this, android.R.layout.simple_spinner_dropdown_item, incidentTypesList);
                incidentTypes.setAdapter(dataAdapter);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setNegativeButton("Cancel", null);
                alertDialog.setTitle("Incident Melden");
                Dialog dialog = alertDialog.create();
                dialog.show();



            }
        });




    }

}
