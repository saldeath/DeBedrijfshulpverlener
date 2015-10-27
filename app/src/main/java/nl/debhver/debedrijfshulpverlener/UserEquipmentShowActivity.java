package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import nl.debhver.debedrijfshulpverlener.models.Equipment;
import nl.debhver.debedrijfshulpverlener.models.ImageModel;

public class UserEquipmentShowActivity extends HomeActivity {
    private Equipment selectedEquipment = null;
    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_equipment_show);
        setBackButtonOnToolbar(false);
        String equipmentObjId = getIntent().getStringExtra(UserEquipmentDefaultActivity.EQUIP_EXTRA);

        if(equipmentObjId != null){ // equipment was added in intent
            DBManager.getInstance().getSingleEuipmentById(this, equipmentObjId);
            //
        }
        else{
            Intent intent = new Intent(UserEquipmentShowActivity.this, UserEquipmentDefaultActivity.class);
            UserEquipmentShowActivity.this.startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    public void loadSingleBranchDetails(List<Equipment> objects) {
        if(objects.size()==1){
            TextView text;
            selectedEquipment = objects.get(0);
            text = (TextView)findViewById(R.id.name);
            text.setText(selectedEquipment.getName());
            text = (TextView)findViewById(R.id.description);
            text.setText(selectedEquipment.getDescription());
            text = (TextView)findViewById(R.id.location);
            text.setText(selectedEquipment.getLocation());
            text = (TextView)findViewById(R.id.type);
            text.setText(selectedEquipment.getType().toString());
            text = (TextView)findViewById(R.id.purchaseDate);
            text.setText(getDateFormat().format(selectedEquipment.getDateOfPurchase()));
            text = (TextView)findViewById(R.id.expireDate);
            text.setText(getDateFormat().format(selectedEquipment.getExpirationDate()));
            text = (TextView)findViewById(R.id.inspectionDate);
            if(selectedEquipment.getDateOfInspection() != null)
                text.setText(getDateFormat().format(selectedEquipment.getDateOfInspection()));

            final ImageView image = (ImageView)findViewById(R.id.equipmentPhoto);
            ImageModel equipmentPhoto = selectedEquipment.getImage();
            if(equipmentPhoto != null) {
                ParseFile file = equipmentPhoto.getImageParseFile();
                if (file != null) {
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null)
                                if (data.length > 1) {
                                    Bitmap equipmentImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    image.setImageBitmap(equipmentImage);
                                } else
                                    popupShortToastMessage(getString(R.string.error_loading_picture));
                        }
                    });
                }
            }
        }
    }
}
