package nl.debhver.debedrijfshulpverlener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.enums.EquipmentType;
import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.Equipment;

public class AdminEquipmentAddActivity extends HomeActivity {
    private static final int CALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Calendar dateOfPurchase, expirationDate, dateOfInspection;
    private TextView inputName, inputDescription, inputLocation, inputDateOfPurchase, inputExpirationDate ,inputDateOfInspection;
    private Spinner inputType, inputBranch;
    private ImageView equipmentTypeImage, inputPicture;
    private Equipment selectedEquipment = null;
    private Bitmap equipmentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_add);
        backButtonOnToolbar();
        initializeInput();

        populateEquipmentTypeSpinner();
        retrieveBranches();
    }

    //source: http://www.vogella.com/tutorials/AndroidCamera/article.html
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            InputStream stream = null;
            if (requestCode == CALLERY_REQUEST_CODE) {
                try {
                    // recyle unused bitmaps
                    if (equipmentImage != null) {
                        equipmentImage.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    equipmentImage = BitmapFactory.decodeStream(stream);
                    //Bitmap scaledEquipmentImage = Bitmap.createScaledBitmap(equipmentImage, inputPicture.getWidth(), inputPicture.getHeight(), false);

                    Matrix m = new Matrix();
                    m.setRectToRect(new RectF(0, 0, equipmentImage.getWidth(), equipmentImage.getHeight()), new RectF(0, 0, inputPicture.getWidth(), inputPicture.getHeight()), Matrix.ScaleToFit.CENTER);
                    Bitmap scaledEquipmentImage = Bitmap.createBitmap(equipmentImage,0,0,inputPicture.getWidth(), inputPicture.getHeight(), m, false);

                    inputPicture.setImageBitmap(scaledEquipmentImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else if(requestCode == CAMERA_REQUEST_CODE) {

            }
        }
    }

    private void populateBranches(List<ParseObject> branches){
        final List<Branch> items = (List)branches;
        final Spinner dropdown = inputBranch;
        dropdown.post(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<Branch> adapter = new ArrayAdapter<Branch>(AdminEquipmentAddActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
            }
        });
    }

    private void populateEquipmentTypeSpinner() {
        ArrayAdapter<EquipmentType> adapter = new ArrayAdapter<EquipmentType>(this, android.R.layout.simple_spinner_dropdown_item, EquipmentType.values());
        inputType.setAdapter(adapter);
    }

    private void retrieveBranches(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                populateBranches(DBManager.getInstance().getBranches());
            }
        });
        t.start();
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
                Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
                switch(id) {
                    case (inputDateOfPurchaseId): {
                        inputDateOfPurchase.setText(DATE_FORMATTER.format(calendar.getTime()));
                        dateOfPurchase = calendar;
                        break;
                    }
                    case (inputExpirationDateId): {
                        inputExpirationDate.setText(DATE_FORMATTER.format(calendar.getTime()));
                        expirationDate = calendar;
                        break;
                    }
                    case (inputDateOfInspectionId): {
                        inputDateOfInspection.setText(DATE_FORMATTER.format(calendar.getTime()));
                        dateOfInspection = calendar;
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
            Calendar calendar = Calendar.getInstance();
            dialog = new DatePickerDialog(this, datePickerListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        dialog.show();
    }

    public void imageViewClicked(View v) {
        if(equipmentImage == null) {
            openGallery();
        }
    }

    //source: http://www.vogella.com/tutorials/AndroidCamera/article.html
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void FABSaveClicked(View v) {
        if(isValidInput()) {

        }
    }

    private boolean isValidInput() {
        return true;
    }

    @Override
    public void finish() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AdminEquipmentAddActivity.this);
        alertDialogBuilder
            .setTitle(R.string.warning)
            .setMessage(getString(R.string.warning_exit_without_save))
            .setCancelable(false)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id){
                    AdminEquipmentAddActivity.super.finish();
                    dialog.dismiss();
                }
            })
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void initializeInput() {
        inputDateOfPurchase = (TextView)findViewById(R.id.inputDateOfPurchase);
        inputExpirationDate = (TextView)findViewById(R.id.inputExpirationDate);
        inputDateOfInspection = (TextView)findViewById(R.id.inputDateOfInspection);
        inputName = (TextView)findViewById(R.id.inputName);
        inputDescription = (TextView)findViewById(R.id.inputDescription);
        inputLocation = (TextView)findViewById(R.id.inputLocation);
        inputBranch = (Spinner)findViewById(R.id.spinner_branches);
        inputType = (Spinner)findViewById(R.id.spinner_equipment_type);
        equipmentTypeImage = (ImageView) findViewById(R.id.equipmentTypeImage);
        inputPicture = (ImageView) findViewById(R.id.equipmentImage);

        inputDateOfPurchase.setOnFocusChangeListener(new DateInputFocusListener());
        inputDateOfPurchase.setOnClickListener(new DateInputClickedListener());
        inputExpirationDate.setOnFocusChangeListener(new DateInputFocusListener());
        inputExpirationDate.setOnClickListener(new DateInputClickedListener());
        inputDateOfInspection.setOnFocusChangeListener(new DateInputFocusListener());
        inputDateOfInspection.setOnClickListener(new DateInputClickedListener());
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
