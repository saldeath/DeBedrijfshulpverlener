package nl.debhver.debedrijfshulpverlener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import nl.debhver.debedrijfshulpverlener.models.Incident;

/**
 * Created by Koen on 15-10-2015.
 */
public class IncidentOpener extends HomeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean previous = getIntent().getBooleanExtra(UserIncidentDefaultActivity.hasPreviousScreen, false);

        if(previous)
            setTitle(R.string.title_activity_user_incident_details);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_opener);
        String incidentId = getIntent().getStringExtra(CustomPushReceiver.EXTRA_INCIDENTID);


        if(previous)
            setBackButtonOnToolbar(false);


        if(incidentId != null){ // user was added in intent
            DBManager.getInstance().getSingleIncidentById(this, incidentId);
        }
        else{
            Log.d("getDesc", "no extra");
        }
    }

    public void loadIncidentDetails(Incident incident){
        TextView textView;
        textView = (TextView)findViewById(R.id.receivedDescription);
        textView.setText(incident.getDescription());

        textView = (TextView)findViewById(R.id.receivedLocation);
        textView.setText(incident.getLocation());

        textView = (TextView)findViewById(R.id.receivedType);
        textView.setText(incident.getType().toString());
    }

    public void loadIncidentPhoto(byte[] data){
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        ImageView imageView = (ImageView)findViewById(R.id.receivedImage);
        imageView.setImageBitmap(bmp);
    }

}
