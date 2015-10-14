package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import nl.debhver.debedrijfshulpverlener.models.User;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkLoggedIn();
    }

    private void checkLoggedIn() {
        User user = (User) User.getCurrentUser();
        if(user != null)
        {
            DBManager.getInstance().subscribeUserToBranch();
            Intent i = new Intent(MainActivity.this, HomeUserActivity.class);
            startActivity(i);
        }
        else{
            DBManager.getInstance().unsubscribeUserFromBranch();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signinButtonClicked(View view) {

        EditText email = (EditText)findViewById(R.id.inputEmail);
        EditText password = (EditText)findViewById(R.id.inputPassword);
        //System.out.println("Email: " + email.getText().toString());
        //System.out.println("Password: " + password.getText().toString());
        ParseUser.logInInBackground(email.getText().toString(), password.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    checkLoggedIn();
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
