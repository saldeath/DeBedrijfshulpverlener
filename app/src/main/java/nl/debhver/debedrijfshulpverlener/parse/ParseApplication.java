package nl.debhver.debedrijfshulpverlener.parse;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import nl.debhver.debedrijfshulpverlener.models.*;

public class ParseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Branch.class);
        ParseObject.registerSubclass(ImageModel.class);
        ParseObject.registerSubclass(Equipment.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(EvacuationPlan.class);
        ParseObject.registerSubclass(Incident.class);
        ParseObject.registerSubclass(Training.class);
        ParseObject.registerSubclass(UserTraining.class);

        Parse.initialize(this, "f7TM8VHqrH17pUmlzPSgq567RryuYOjvnB96FC0P", "vdB7HpL58bnYc6TOij3NolTyq72PDh32fXiwYjiL");
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("ParseSuccess", "device registered");
                } else {
                    Log.d("ParseError", "Could not register device. "+ e.toString());
                }
            }
        });

    }
    public static Context getContext(){
        return mContext;
    }
}
