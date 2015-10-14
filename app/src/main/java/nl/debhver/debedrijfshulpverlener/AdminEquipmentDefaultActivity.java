package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AdminEquipmentDefaultActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_default);
    }

    public void FABClicked(View v) {
        Intent intent = new Intent(this, AdminEquipmentAddActivity.class);
        startActivity(intent);
    }
}
