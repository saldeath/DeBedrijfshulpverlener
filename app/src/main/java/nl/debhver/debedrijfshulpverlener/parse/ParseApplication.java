package nl.debhver.debedrijfshulpverlener.parse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import nl.debhver.debedrijfshulpverlener.model.Branch;
import nl.debhver.debedrijfshulpverlener.model.Equipment;
import nl.debhver.debedrijfshulpverlener.model.EvacuationPlan;
import nl.debhver.debedrijfshulpverlener.model.ImageModel;
import nl.debhver.debedrijfshulpverlener.model.Incident;
import nl.debhver.debedrijfshulpverlener.model.Training;
import nl.debhver.debedrijfshulpverlener.model.User;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Branch.class);
        ParseObject.registerSubclass(ImageModel.class);
        ParseObject.registerSubclass(Equipment.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(EvacuationPlan.class);
        ParseObject.registerSubclass(Incident.class);
        ParseObject.registerSubclass(Training.class);

        Parse.initialize(this, "f7TM8VHqrH17pUmlzPSgq567RryuYOjvnB96FC0P", "vdB7HpL58bnYc6TOij3NolTyq72PDh32fXiwYjiL");
    }
}
