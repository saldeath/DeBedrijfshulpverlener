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

import nl.debhver.debedrijfshulpverlener.models.Equipment;



public class UserEquipmentDefaultActivity extends HomeActivity {
    public static String EQUIP_EXTRA = "equip_extra";
    private List<Equipment> equipList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_equipment_default);
        addListenerToSearchView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        retrieveEquips();

    }

    public void retrieveEquips(){
        showProgressBar(true);
        DBManager.getInstance().getBranchForUser(this, getBranchObjectId());
    }



    public void setEquipmentList(List<Equipment> equipListFromDatabase){ // new listreset the ListView
        this.equipList = equipListFromDatabase;
        populateListView(equipList);
    }

    public void populateListView(List<Equipment> equipmentListPara){
        final ListView equipmentListView = (ListView)findViewById(R.id.equipmentListView);
        ArrayAdapter<Equipment> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, equipmentListPara);
        equipmentListView.setAdapter(adapter);
        equipmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Equipment equipment = (Equipment) equipmentListView.getItemAtPosition(position);
                Intent intent = new Intent(UserEquipmentDefaultActivity.this, UserEquipmentShowActivity.class);
                intent.putExtra(EQUIP_EXTRA, equipment.getObjectId());
                UserEquipmentDefaultActivity.this.startActivity(intent);

            }
        });
        showProgressBar(false);
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
                    filterEquipmentListOnlyByName(searchViewString);
                }

                return false;
            }
        });
    }

    public void filterEquipmentListOnlyByName(String nameMustContain){
        List<Equipment> tempEquipList = new ArrayList<>(equipList);

        List<Equipment> toRemove = new ArrayList<>();

        for(Equipment equip: tempEquipList){
            if(containsIgnoreCase(equip.toString(),nameMustContain)==false){ // Equipment does not contain string
                toRemove.add(equip);
            }

        }
        tempEquipList.removeAll(toRemove);
        populateListView(tempEquipList);
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
