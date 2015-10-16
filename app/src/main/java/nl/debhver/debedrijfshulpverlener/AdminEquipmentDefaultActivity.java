package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.ParseObject;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Branch;

public class AdminEquipmentDefaultActivity extends HomeActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_default);
        listView = (ListView)findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveBranches();
    }

    public void FABClicked(View v) {
        Intent intent = new Intent(this, AdminEquipmentAddActivity.class);
        startActivity(intent);
    }

    private void populateListView(List<ParseObject> result) {
        final List<Branch> items = (List)result;

        listView.post(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<Branch> adapter = new ArrayAdapter<Branch>(AdminEquipmentDefaultActivity.this, android.R.layout.activity_list_item, items);
                listView.setAdapter(adapter);
            }
        });
    }

    private void retrieveBranches(){
        new AsyncTask<Void, Void, List<ParseObject>>() {
            @Override
            protected List<ParseObject> doInBackground(Void... p) {
                return DBManager.getInstance().getEquipments();
            }

            @Override
            protected void onPostExecute(List<ParseObject> result) {
                populateListView(result);
            }
        }.execute();
    }
}
