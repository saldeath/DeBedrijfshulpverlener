package nl.debhver.debedrijfshulpverlener;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AdminEquipmentAddActivity extends AppCompatActivity {
    private Calendar dateOfPurchase, expirationDate, dateOfInspection;
    private TextView inputName, inputDescription, inputLocation, inputDateOfPurchase, inputExpirationDate ,inputDateOfInspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_add);

        inputDateOfPurchase = (TextView)findViewById(R.id.inputDateOfPurchase);
        inputExpirationDate = (TextView)findViewById(R.id.inputExpirationDate);
        inputDateOfInspection = (TextView)findViewById(R.id.inputDateOfInspection);
        inputName = (TextView)findViewById(R.id.inputName);
        inputDescription = (TextView)findViewById(R.id.inputDescription);
        inputLocation = (TextView)findViewById(R.id.inputLocation);

        inputDateOfPurchase.setOnFocusChangeListener(new DateInputFocusListener());
        inputDateOfPurchase.setOnClickListener(new DateInputClickedListener());
        inputExpirationDate.setOnFocusChangeListener(new DateInputFocusListener());
        inputExpirationDate.setOnClickListener(new DateInputClickedListener());
        inputDateOfInspection.setOnFocusChangeListener(new DateInputFocusListener());
        inputDateOfInspection.setOnClickListener(new DateInputClickedListener());
    }

    public void addEquipmentOnClick(View v) {

    }

    public void showDatePickerDialog(final View v) {
        final int id = v.getId();
        final int inputDateOfPurchaseId = R.id.inputDateOfPurchase;
        final int inputExpirationDateId = R.id.inputExpirationDate;
        final int inputDateOfInspectionId = R.id.inputDateOfInspection;
        DatePickerDialog dialog;

        DatePickerDialog.OnDateSetListener datePickerListener
                = new DatePickerDialog.OnDateSetListener() {
            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                Calendar cal = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM dd, yyyy");
                switch(id) {
                    case (inputDateOfPurchaseId): {
                        inputDateOfPurchase.setText(DATE_FORMATTER.format(cal.getTime()));
                        dateOfPurchase = cal;
                        break;
                    }
                    case (inputExpirationDateId): {
                        inputExpirationDate.setText(DATE_FORMATTER.format(cal.getTime()));
                        expirationDate = cal;
                        break;
                    }
                    case (inputDateOfInspectionId): {
                        inputDateOfInspection.setText(DATE_FORMATTER.format(cal.getTime()));
                        dateOfInspection = cal;
                        break;
                    }
                }
            }
        };

        if(id == inputDateOfPurchaseId && dateOfPurchase != null) {
            dialog = new DatePickerDialog(this, datePickerListener,
                    dateOfPurchase.get(Calendar.YEAR), dateOfPurchase.get(Calendar.MONTH), dateOfPurchase.get(Calendar.DAY_OF_MONTH));
        }
        else if(id == inputExpirationDateId && expirationDate != null) {
            dialog = new DatePickerDialog(this, datePickerListener,
                    expirationDate.get(Calendar.YEAR), expirationDate.get(Calendar.MONTH), expirationDate.get(Calendar.DAY_OF_MONTH));
        }
        else if(id == inputDateOfInspectionId && dateOfInspection != null) {
            dialog = new DatePickerDialog(this, datePickerListener,
                    dateOfInspection.get(Calendar.YEAR), dateOfInspection.get(Calendar.MONTH), dateOfInspection.get(Calendar.DAY_OF_MONTH));
        }
        else {
            Calendar cal = Calendar.getInstance();
            dialog = new DatePickerDialog(this, datePickerListener,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
        dialog.show();
    }

    private class DateInputClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDatePickerDialog(v);
        }
    }
    private class DateInputFocusListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                showDatePickerDialog(v);
            }
        }
    }
}
