package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import nl.debhver.debedrijfshulpverlener.models.User;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginProgressBar = (ProgressBar)findViewById(R.id.login_progressbar);

        final TextView password = (TextView)findViewById(R.id.inputPassword);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    signinButtonClicked(password.getRootView());
                }
                return false;
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    public void signinButtonClicked(View view) {
        loginProgressBar.setVisibility(View.VISIBLE);
        EditText email = (EditText)findViewById(R.id.inputEmail);
        EditText password = (EditText)findViewById(R.id.inputPassword);
        //System.out.println("Email: " + email.getText().toString());
        //System.out.println("Password: " + password.getText().toString());
        ParseUser.logInInBackground(email.getText().toString(), password.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                loginProgressBar.setVisibility(View.INVISIBLE);
                if (user != null) {
                    checkLoggedIn();
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}