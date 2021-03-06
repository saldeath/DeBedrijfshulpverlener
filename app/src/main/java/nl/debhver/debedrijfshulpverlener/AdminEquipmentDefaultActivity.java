package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.Equipment;

public class AdminEquipmentDefaultActivity extends HomeActivity {
    public static String EQUIPMENT_EXTRA = "equipment_extra";
    private List<Equipment> equipmentList;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_default);
        addListenerToSearchView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveEquipment();
    }

    public void FABClicked(View v) {
        Intent intent = new Intent(this, AdminEquipmentAddActivity.class);
        startActivity(intent);
    }

    private void populateListView(final List<Equipment> result) {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();
        List<String> options = new ArrayList<>();
        options.add(getString(R.string.edit));
        options.add(getString(R.string.delete));
        List<Drawable> icons = new ArrayList<>();
        icons.add(ContextCompat.getDrawable(this, R.drawable.ic_edit));
        icons.add(ContextCompat.getDrawable(this, R.drawable.ic_recycle_bin));
        // Adding header/child data
        int i = 0;
        for (Equipment e : result) {
            listDataHeader.add(i, e.toString());
            listDataChild.put(listDataHeader.get(i), options); // Header, Child data
            i++;
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, icons);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, int childPosition, long id) {
                if(childPosition == 0){
                    Intent intent = new Intent(AdminEquipmentDefaultActivity.this, AdminEquipmentAddActivity.class);
                    intent.putExtra(EQUIPMENT_EXTRA, result.get(groupPosition).getObjectId());
                    AdminEquipmentDefaultActivity.this.startActivity(intent);
                } else if(childPosition == 1){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdminEquipmentDefaultActivity.this);
                    alertDialogBuilder
                            .setTitle(R.string.warning)
                            .setMessage(getString(R.string.warning_delete_equipment) + "\n" + result.get(groupPosition).toString())
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String objectId = result.get(groupPosition).getObjectId();
                                    DBManager.getInstance().getParseObjectById(Table.EQUIPMENT, objectId, new FindCallback<Equipment>() {
                                        @Override
                                        public void done(List<Equipment> objects, ParseException e) {
                                            DBManager.getInstance().delete(objects.get(0), new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        popupShortToastMessage(getString(R.string.equipment_delete_succes));
                                                        retrieveEquipment();
                                                    } else {
                                                        Log.d("ParseError", e.toString());
                                                        popupShortToastMessage(getString(R.string.equipment_delete_error));
                                                    }
                                                }
                                            });
                                        }
                                    });
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
                return true;
            }
        });
        showProgressBar(false);
    }

    private void retrieveEquipment(){
        showProgressBar(true);
        DBManager.getInstance().getListParseObjects(Table.EQUIPMENT, new FindCallback<Equipment>() {
            @Override
            public void done(List<Equipment> objects, ParseException e) {
                equipmentList = new ArrayList<Equipment>(objects);
                populateListView(objects);
            }
        });
    }

    private void filterEquipmentListOnlyByName(String nameMustContain){
        List<Equipment> tempEquipmentList = new ArrayList<>(equipmentList);

        List<Equipment> toRemove = new ArrayList<>();

        for(Equipment equipment: tempEquipmentList){
            if(!containsIgnoreCase(equipment.toString(),nameMustContain)){ // equipment does not contain string
                toRemove.add(equipment);
            }
        }
        tempEquipmentList.removeAll(toRemove);
        populateListView(tempEquipmentList);
    }

    private static boolean containsIgnoreCase(String src, String what) {
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

    private void addListenerToSearchView() {
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
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
}
