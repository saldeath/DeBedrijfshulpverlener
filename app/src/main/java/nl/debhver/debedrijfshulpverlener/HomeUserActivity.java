package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

import nl.debhver.debedrijfshulpverlener.enums.IncidentType;
import nl.debhver.debedrijfshulpverlener.models.ImageModel;
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
    //private Bitmap equipmentImage;
    //pivate byte[] scaledImageByte;
    private ImageModel model = null;
    private String location;
    private String description;
    private int spinnerId;
    private byte[] image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        checkState(savedInstanceState);

        incidentButton = (ImageButton) findViewById(R.id.incident_button);

        incidentButton.setOnClickListener(new imageClick() {
        });

    }

    private void checkState(Bundle savedInstanceState) {
        if( savedInstanceState != null ) {
            location = savedInstanceState.getString("location");
            description = savedInstanceState.getString("description");
            spinnerId = savedInstanceState.getInt("spinnerId");
            image = savedInstanceState.getByteArray("image");

            //show dialog on restore
            imageClick a = new imageClick();
            View view = LayoutInflater.from(HomeUserActivity.this).inflate(R.layout.incident_dialog, null);
            a.onClick(view);

        }
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

    public void startCamera(View view) {
        openCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_FOTO_REQUEST && resultCode == RESULT_OK) {

            // equipmentImage = (Bitmap) data.getExtras().get("data");
            //Bitmap scaledIncident = Bitmap.createScaledBitmap(equipmentImage, 500, 500 * equipmentImage.getHeight() / equipmentImage.getWidth(), false);

            // Resize photo from camera byte array
            Bitmap mealImage = (Bitmap) data.getExtras().get("data");
            Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 500, 500
                    * mealImage.getHeight() / mealImage.getWidth(), false);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mealImageScaled.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            byte[] scaledData = bos.toByteArray();
            image = scaledData;
            Incident incident = new Incident();

            // Save the scaled image to Parse
            ParseFile photoFile = new ParseFile("incident.jpg", scaledData);
            model = new ImageModel();
            model.setImageParseFile(photoFile);
            incident.setImage(model);

        }
    }

    public boolean checkInputFields() {

        if (incidentLocation.getText().toString().isEmpty()) {
            popupShortToastMessage("Locatie is verplicht");
            return false;
        }

        if (incidentDescription.getText().toString().isEmpty()) {
            popupShortToastMessage("Beschrijving is verplicht");
            return false;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(!incidentLocation.getText().toString().isEmpty()){
            outState.putString("location", incidentLocation.getText().toString());
        }

        if(!incidentDescription.getText().toString().isEmpty()){
            outState.putString("description", incidentDescription.getText().toString());
        }

        int id = incidentTypes.getSelectedItemPosition();
        outState.putInt("spinnerId", id);

        outState.putByteArray("image", image);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class imageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            View view = LayoutInflater.from(HomeUserActivity.this).inflate(R.layout.incident_dialog, null);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeUserActivity.this);
            alertDialog.setView(view);

            initSpinner(view);

            incidentLocation = (EditText) view.findViewById(R.id.incident_location);
            incidentDescription = (EditText) view.findViewById(R.id.incident_description);
            incidentLocation.setText(location);
            incidentDescription.setText(description);
            incidentTypes.setSelection(spinnerId);

            alertDialog.setPositiveButton("ok", null);

            alertDialog.setNegativeButton("Annuleren", null);
            alertDialog.setTitle("Incident Melden");
            Dialog dialog = alertDialog.create();


            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (checkInputFields()) {
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

                                ParseFile photoFile = new ParseFile("incident.jpg", image);
                                model = new ImageModel();
                                model.setImageParseFile(photoFile);

                                incident.setImage(model);
                                if (model != null) {
                                    incident.setImage(model);
                                    model = null;
                                }

                                DBManager.getInstance().createIncident(incident, HomeUserActivity.this);
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });
            dialog.show();
        }
    }
}
