package nl.debhver.debedrijfshulpverlener;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.Training;
import nl.debhver.debedrijfshulpverlener.models.User;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

public class AdminTrainingUserAddActivity extends HomeActivity {
    List<User> userList = new ArrayList<>();
    Training training = null;
    UserTraining userTraining = new UserTraining();
    private Calendar schelduleDate, expirationDate;
    Button sbtn,ebtn;
    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_user_add);
        String trainingObjectID = this.getIntent().getStringExtra("Training");
        if(trainingObjectID != null)
        {
            DBManager.getInstance().getTrainingbyID(AdminTrainingUserAddActivity.this, trainingObjectID);
        }
        UsersBranches();
        sbtn = (Button)findViewById(R.id.schelduleDateBtn);
        ebtn = (Button)findViewById(R.id.expirationDateBtn);
    }
    public void loadSingleTraining(Training object) {
        this.training = object;
        TextView trainingName = (TextView)findViewById(R.id.TrainingNametextView);
        TextView trainingDescription = (TextView)findViewById(R.id.TrainingDescriptiontextView);
        TextView trainingType = (TextView)findViewById(R.id.TrainingTypetextView);
        trainingName.setText(object.getName());
        trainingDescription.setText(object.getDescription());
        trainingType.setText(object.getType().toString());
    }


    private void UsersBranches(){
        DBManager.getInstance().getListParseObjects(Table.USER, new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                populateUsers(objects);
            }
        });
    }
    private void populateUsers(final List<User> userList)
    {
        this.userList = userList;
        LinearLayout ll = (LinearLayout)findViewById(R.id.UserTrainingLayout);
        for (User u : userList)
        {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setTextColor(Color.BLACK);
            checkBox.setText(u.toString());
            ll.addView(checkBox);
        }
    }
    public void saveUserstoTraining(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.UserTrainingLayout);
        int count = layout.getChildCount();
        CheckBox checkBox = null;
        for(int i=0; i<count; i++) {
            checkBox = (CheckBox)layout.getChildAt(i);
            if(checkBox != null && checkBox.isChecked()){
                UserTraining ut = new UserTraining();
                ut.setTraining(this.training);
                ut.setUser(userList.get(i));
                if (userTraining.getDateAchieved() == null || userTraining.getExpirationDate() == null){
                    Toast dateWarning = Toast.makeText(this, "Geen datum ingevuld", Toast.LENGTH_LONG);
                    dateWarning.show();
                }
                else
                {
                    ut.setScheduleDate(userTraining.getScheduleDate());
                    ut.setExpirationDate(userTraining.getExpirationDate());
                    ut.setDateAchieved(userTraining.getDateAchieved());
                    DBManager.getInstance().createUserTraining(AdminTrainingUserAddActivity.this, ut);
                }
            }
        }
    }
    public void showDatePickerDialog(View view) {
        final int id = view.getId();
        final int inputDateScheldule = R.id.schelduleDateBtn;
        final int inputDateExpiration = R.id.expirationDateBtn;
        DatePickerDialog dialog;
        DatePickerDialog.OnDateSetListener datePickerListener
                = new DatePickerDialog.OnDateSetListener() {
            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
                switch(id) {
                    case (inputDateScheldule): {
                        sbtn.setText(getDateFormat().format(calendar.getTime()));
                        schelduleDate = calendar;
                        userTraining.setScheduleDate(schelduleDate.getTime());
                        userTraining.setDateAchieved(schelduleDate.getTime());
                        break;
                    }
                    case (inputDateExpiration):{
                        ebtn.setText(getDateFormat().format(calendar.getTime()));
                        expirationDate = calendar;
                        userTraining.setExpirationDate(expirationDate.getTime());
                        break;
                    }
                }
            }
        };
        if(id == inputDateScheldule && schelduleDate != null) {
            dialog = new DatePickerDialog(this, datePickerListener,
                    schelduleDate.get(Calendar.YEAR), schelduleDate.get(Calendar.MONTH), schelduleDate.get(Calendar.DAY_OF_MONTH));
        }
        else if (id == inputDateExpiration && expirationDate != null)
        {
            dialog = new DatePickerDialog(this, datePickerListener,
                    expirationDate.get(Calendar.YEAR), expirationDate.get(Calendar.MONTH), expirationDate.get(Calendar.DAY_OF_MONTH));
        }
        else {
            Calendar calendar = Calendar.getInstance();
            dialog = new DatePickerDialog(this, datePickerListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        dialog.show();

    }
}
