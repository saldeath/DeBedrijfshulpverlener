package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Incident;


public class UserIncidentDefaultActivity extends HomeActivity {
    public static String EXTRA_INCIDENTID = "incident_id";
    public static String hasPreviousScreen = "hasPreviousScreen";
    private List<Incident> incidentList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_incident_default);
        addListenerToSearchView();
    }
    @Override
    protected void onResume(){
        super.onResume();
        retrieveIncidents();
    }
    public void retrieveIncidents(){
        DBManager.getInstance().getBranchForUser(this, getBranchObjectId());
    }
    public void setIncidentList(List<Incident> incidentListFromDatabase){ // new listreset the ListView
        this.incidentList = incidentListFromDatabase;
        populateListView(incidentList);
    }
    public void populateListView(List<Incident> incidentListPara){
        final ListView incidentListView = (ListView)findViewById(R.id.incidentListView);
        ArrayAdapter<Incident> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, incidentListPara);
        incidentListView.setAdapter(adapter);
        incidentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Incident incident = (Incident) incidentListView.getItemAtPosition(position);
                Intent intent = new Intent(UserIncidentDefaultActivity.this, IncidentOpener.class);
                intent.putExtra(EXTRA_INCIDENTID, incident.getObjectId());
                intent.putExtra(hasPreviousScreen, true);
                UserIncidentDefaultActivity.this.startActivity(intent);

            }
        });
    }
    public void addListenerToSearchView(){
        final SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchViewString) {

                if (searchView.getWidth() > 0) {
                    filterIncidentListOnlyByName(searchViewString);
                }

                return false;
            }
        });
    }

    public void filterIncidentListOnlyByName(String nameMustContain){
        List<Incident> tempIncidentList = new ArrayList<>(incidentList);

        List<Incident> toRemove = new ArrayList<>();

        for(Incident incident: tempIncidentList){
            if(containsIgnoreCase(incident.toString(),nameMustContain)==false){ // Incident does not contain string
                toRemove.add(incident);
            }

        }
        tempIncidentList.removeAll(toRemove);
        populateListView(tempIncidentList);
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
