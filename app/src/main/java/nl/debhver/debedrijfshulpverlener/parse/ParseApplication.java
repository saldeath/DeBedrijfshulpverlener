package nl.debhver.debedrijfshulpverlener.parse;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "f7TM8VHqrH17pUmlzPSgq567RryuYOjvnB96FC0P", "vdB7HpL58bnYc6TOij3NolTyq72PDh32fXiwYjiL");
    }
}
