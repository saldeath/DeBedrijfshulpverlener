package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.models.Training;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

public class AdminEditUserTraining extends HomeActivity{
    private UserTraining userTraining = null;
    Button schelduleDateBtn, expirationDateBtn;
    Calendar schelduleDate, expirationDate;

    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user_training);
        setBackButtonOnToolbar(true);
        String userTrainingObjectID = getIntent().getStringExtra("UserTraining");
        if(userTrainingObjectID != null)
        {
            DBManager.getInstance().getUserTrainingbyID(AdminEditUserTraining.this, userTrainingObjectID);
        }
        else
        {
        }
    }


    public void loadSingleUserTraining(UserTraining object)
    {
        userTraining = object;
        System.out.println(userTraining.getUser().getName());
        TextView trainingName = (TextView)findViewById(R.id.adminEditUserTrainingTrainingName);
        TextView trainingDescription = (TextView)findViewById(R.id.adminEditUserTrainingTrainingDescription);
        TextView trainingType = (TextView)findViewById(R.id.adminEditUserTrainingTrainingType);
        TextView userName = (TextView)findViewById(R.id.adminEditUserTrainingUserName);
        schelduleDateBtn = (Button)findViewById(R.id.adminEditUserUserTrainingSCHD);
        expirationDateBtn = (Button)findViewById(R.id.adminEditUserUserTrainingEXPD);
        trainingName.setText(userTraining.getTraining().getName());
        trainingDescription.setText(userTraining.getTraining().getDescription());
        trainingType.setText(userTraining.getTraining().getType().toString());
        userName.setText(userTraining.getUser().getName());
        schelduleDateBtn.setText(getDateFormat().format(userTraining.getScheduleDate()));
        expirationDateBtn.setText(getDateFormat().format(userTraining.getExpirationDate()));

    }

    public void showDatePickerDialog(View view) {
        final int id = view.getId();
        final int inputDateScheldule = R.id.adminEditUserUserTrainingSCHD;
        final int inputDateExpiration = R.id.adminEditUserUserTrainingEXPD;
        DatePickerDialog dialog;
        DatePickerDialog.OnDateSetListener datePickerListener
                = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
                switch(id) {
                    case (inputDateScheldule): {
                        schelduleDateBtn.setText(getDateFormat().format(calendar.getTime()));
                        schelduleDate = calendar;
                        userTraining.setScheduleDate(schelduleDate.getTime());
                        userTraining.setDateAchieved(schelduleDate.getTime());
                        break;
                    }
                    case (inputDateExpiration):{
                        expirationDateBtn.setText(getDateFormat().format(calendar.getTime()));
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
    public void editUserTraining(View view) {
        DBManager.getInstance().editUserTraining(AdminEditUserTraining.this, userTraining);
    }
    public void deleteUserTraining(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Waarschuwing");
        builder.setMessage("Weet je zeker dat je dit wilt verwijderen?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DBManager.getInstance().deleteUserTraining(AdminEditUserTraining.this, userTraining);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
