package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koen on 26-10-2015.
 */
public class EmergencyManualsActivity extends HomeActivity {
    static final String EMERGENCY_EXTRA = "emergency_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_manuals);
        populateEmergencyList();
    }

    private void populateEmergencyList(){
        final ListView emergencyListView = (ListView)findViewById(R.id.emergency_listview);
        List<String> emergenciesList = new ArrayList<String>();
        emergenciesList.add(getResources().getString(R.string.heart_attack));
        emergenciesList.add(getResources().getString(R.string.suffocation));
        emergenciesList.add(getResources().getString(R.string.evacuation_plan));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, emergenciesList);
        emergencyListView.setAdapter(adapter);

        emergencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String emergencyType = (String) emergencyListView.getItemAtPosition(position);
                Intent intent = new Intent(EmergencyManualsActivity.this, SingleEmergencyDetailsActivity.class);
                intent.putExtra(EMERGENCY_EXTRA, emergencyType);
                startActivity(intent);
            }
        });
    }
}
