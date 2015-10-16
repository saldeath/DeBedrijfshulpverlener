package nl.debhver.debedrijfshulpverlener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.enums.EquipmentType;
import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.Equipment;
import nl.debhver.debedrijfshulpverlener.models.ImageModel;

public class AdminEquipmentAddActivity extends HomeActivity {
    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Calendar dateOfPurchase, expirationDate, dateOfInspection;
    private TextView inputName, inputDescription, inputLocation, inputDateOfPurchase, inputExpirationDate ,inputDateOfInspection;
    private Spinner inputType, inputBranch;
    private ImageView equipmentTypeImage, inputPicture;
    private Equipment selectedEquipment = null;
    private Bitmap equipmentImage;
    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
    }
    //source: http://stackoverflow.com/questions/6185966/converting-a-date-object-to-a-calendar-object
    private static Calendar getDateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_add);
        setBackButtonOnToolbar(true);

        initializeInput();
        retrieveBranches();
        populateEquipmentTypeSpinner();

        String equipmentObjId = getIntent().getStringExtra(AdminEquipmentDefaultActivity.EQUIPMENT_EXTRA);
        if(equipmentObjId != null) { // user was added in intent
            setTitle(R.string.title_activity_admin_equipment_edit);
            DBManager.getInstance().getParseObjectById(Table.EQUIPMENT, equipmentObjId, new FindCallback<Equipment>() {
                @Override
                public void done(List<Equipment> objects, ParseException e) {
                    setInputSelectedEquipment(selectedEquipment = objects.get(0));
                }
            });
        }
    }

    //source: http://www.vogella.com/tutorials/AndroidCamera/article.html
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            // recyle unused bitmaps
            if (equipmentImage != null) {
                equipmentImage.recycle();
            }

            if (requestCode == GALLERY_REQUEST_CODE) {
                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(data.getData());
                    equipmentImage = scaleBitmap(BitmapFactory.decodeStream(stream));
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
                equipmentImage = scaleBitmap((Bitmap) data.getExtras().get("data"));
            }
            inputPicture.setImageBitmap(equipmentImage);
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = 0;
        float scaleHeight = 0;

        if(width > height) {
            scaleWidth = 500 * width / height;
            scaleHeight = 500;
        }
        else {
            scaleWidth = 500;
            scaleHeight = 500 * height / width;
        }
        return Bitmap.createScaledBitmap(bitmap, Math.round(scaleWidth), Math.round(scaleHeight), true);
    }

    private void populateBranches(List<Branch> branches){
        final List<Branch> items = (List)branches;
        final Spinner dropdown = inputBranch;
                ArrayAdapter<Branch> adapter = new ArrayAdapter<Branch>(AdminEquipmentAddActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
    }

    private void populateEquipmentTypeSpinner() {
        ArrayAdapter<EquipmentType> adapter = new ArrayAdapter<EquipmentType>(this, android.R.layout.simple_spinner_dropdown_item, EquipmentType.values());
        inputType.setAdapter(adapter);
    }

    private void retrieveBranches(){
        DBManager.getInstance().getListParseObjects(Table.BRANCH, new FindCallback<Branch>() {
            @Override
            public void done(List<Branch> objects, ParseException e) {
                populateBranches(objects);
            }
        });
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
                switch(id) {
                    case (inputDateOfPurchaseId): {
                        inputDateOfPurchase.setText(getDateFormat().format(calendar.getTime()));
                        dateOfPurchase = calendar;
                        break;
                    }
                    case (inputExpirationDateId): {
                        inputExpirationDate.setText(getDateFormat().format(calendar.getTime()));
                        expirationDate = calendar;
                        break;
                    }
                    case (inputDateOfInspectionId): {
                        inputDateOfInspection.setText(getDateFormat().format(calendar.getTime()));
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

    private void imageViewClicked() {
        final Dialog dialog = new Dialog(AdminEquipmentAddActivity.this);
        dialog.setTitle(R.string.hint_equipment_image);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.image_preview_dialog, null));
        dialog.show();

        Button delete = (Button) dialog.findViewById(R.id.deleteButton);
        Button gallery = (Button) dialog.findViewById(R.id.gallaryButton);
        Button camera = (Button) dialog.findViewById(R.id.cameraButton);

        if (equipmentImage == null) {
            delete.setVisibility(View.GONE);
        } else {
            ((ImageView) (dialog.findViewById(R.id.imageView))).setImageBitmap(equipmentImage);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteImage();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera();
            }
        });
    }

    //source: http://www.vogella.com/tutorials/AndroidCamera/article.html
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void deleteImage() {

    }

    public void FABSaveClicked(View v) {
        if(isValidInput()) {
            ImageModel imageModel;
            if(selectedEquipment == null) {
                imageModel = new ImageModel();
                selectedEquipment = new Equipment();
                selectedEquipment.setImage(imageModel);
            }
            imageModel = selectedEquipment.getImage();
            selectedEquipment.setName(inputName.getText().toString());
            selectedEquipment.setDescription(inputDescription.getText().toString());
            selectedEquipment.setLocation(inputLocation.getText().toString());
            int id = inputType.getSelectedItemPosition();
            EquipmentType[] equipmentType = EquipmentType.values();
            selectedEquipment.setType(equipmentType[id]);
            selectedEquipment.setDateOfPurchase(dateOfPurchase.getTime());
            selectedEquipment.setExpirationDate(expirationDate.getTime());
            if(dateOfInspection != null)
                selectedEquipment.setDateOfInspection(dateOfInspection.getTime());
            selectedEquipment.setBranch((Branch) inputBranch.getSelectedItem());
            //imageModel.setImage(ImageModel.getBytes(equipmentImage));
            //selectedEquipment.setImage(imageModel);

            DBManager.getInstance().save(selectedEquipment, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        AdminEquipmentAddActivity.this.popupShortToastMessage(getString(R.string.equipment_save_succes));
                        AdminEquipmentAddActivity.this.setSaved(true);
                        AdminEquipmentAddActivity.this.finish();
                    } else {
                        Log.d("ParseError", e.toString());
                        AdminEquipmentAddActivity.this.popupShortToastMessage(getString(R.string.equipment_save_error));
                    }
                }
            });
        }
    }

    private boolean isValidInput() {
        Boolean validInput = true;
        if(inputName.getText().toString().equals("")) {
            popupShortToastMessage(getString(R.string.error_empty_name));
            validInput = false;
        }
        if(inputLocation.getText().toString().equals("")) {
            popupShortToastMessage(getString(R.string.error_empty_location));
            validInput = false;
        }
        if(dateOfPurchase == null) {
            popupShortToastMessage(getString(R.string.error_empty_date_of_purchase));
            validInput = false;
        }
        if(expirationDate == null) {
            popupShortToastMessage(getString(R.string.error_empty_expiration_date));
            validInput = false;
        }
        return validInput;
    }

    private void setInputSelectedEquipment(Equipment equipment) {
        selectedEquipment = equipment;

        inputName.setText(equipment.getName());
        inputDescription.setText(equipment.getDescription());
        inputLocation.setText(equipment.getLocation());
        int i = 0;
        for(EquipmentType type : EquipmentType.values()) {
            if(type == equipment.getType())
                inputType.setSelection(i, false);
            i++;
        }
        dateOfPurchase = getDateToCalendar(equipment.getDateOfPurchase());
        inputDateOfPurchase.setText(getDateFormat().format(dateOfPurchase.getTime()));
        expirationDate = getDateToCalendar(equipment.getExpirationDate());
        inputExpirationDate.setText(getDateFormat().format(expirationDate.getTime()));
        dateOfInspection = getDateToCalendar(equipment.getDateOfInspection());
        if(dateOfInspection != null)
            inputDateOfInspection.setText(getDateFormat().format(dateOfInspection.getTime()));
        ArrayAdapter<Branch> adapter= (ArrayAdapter)inputBranch.getAdapter();
        inputBranch.setSelection(adapter.getPosition(equipment.getBranch()));
        //imageModel.setImage(ImageModel.getBytes(equipmentImage));
        //selectedEquipment.setImage(imageModel);
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

        inputPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewClicked();
            }
        });
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                showDatePickerDialog(v);
            }
        }
    }
}
