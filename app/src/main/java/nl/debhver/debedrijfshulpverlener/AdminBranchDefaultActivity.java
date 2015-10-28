package nl.debhver.debedrijfshulpverlener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.Equipment;


public class AdminBranchDefaultActivity extends HomeActivity {
    public static String BRANCH_EXTRA = "branch_extra";
    private List<Branch> branchList = null;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_branch_default);
        addListenerToSearchView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveBranches();
    }

    public void addListenerToSearchView() {
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchViewString) {

                if (searchView.getWidth() > 0) {
                    filterBranchListOnlyByName(searchViewString);
                }

                return false;
            }
        });
    }
    public void FABClicked(View v) {

        if (v.getId() == R.id.AdminAddBranch) {
            // doStuff
            Intent intent = new Intent(AdminBranchDefaultActivity.this, AdminAddBranchActivity.class);
            AdminBranchDefaultActivity.this.startActivity(intent);
            Log.i("Content ", " start AddBranchActivity ");
        }
    }

    public void retrieveBranches(){
        showProgressBar(true);
        DBManager.getInstance().getListParseObjects(Table.BRANCH, new FindCallback<Branch>() {
            @Override
            public void done(List<Branch> objects, ParseException e) {
                if(e==null)
                    setBranchList(objects);
            }
        });
    }

    public void setBranchList(List<Branch> branchListFromDatabase){ // new listreset the ListView
        this.branchList = branchListFromDatabase;
        //populateListView(branchList);
        prepareListData(branchList);

    }

    public void filterBranchListOnlyByName(String nameMustContain){
        List<Branch> tempBranchList = new ArrayList<>(branchList);

        List<Branch> toRemove = new ArrayList<>();

        for(Branch branch: tempBranchList){
            if(containsIgnoreCase(branch.toString(),nameMustContain)==false){ // branch does not contain string
                toRemove.add(branch);
            }

        }
        tempBranchList.removeAll(toRemove);
        prepareListData(tempBranchList);

    }

    public void prepareListData(final List<Branch> result) {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> options = new ArrayList<String>();
        options.add(getString(R.string.edit));
        options.add(getString(R.string.delete));
        List<Drawable> icons = new ArrayList<>();
        icons.add(getDrawable(R.drawable.ic_edit));
        icons.add(getDrawable(R.drawable.ic_recycle_bin));
        // Adding header/child data
        int i = 0;
        for (Branch b : result) {
            listDataHeader.add(i, b.toString());
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
                    Intent intent = new Intent(AdminBranchDefaultActivity.this, AdminAddBranchActivity.class);
                    intent.putExtra(BRANCH_EXTRA, result.get(groupPosition).getObjectId());
                    AdminBranchDefaultActivity.this.startActivity(intent);
                }
                if(childPosition == 1){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdminBranchDefaultActivity.this);
                    alertDialogBuilder
                            .setTitle(R.string.warning)
                            .setMessage(getString(R.string.warning_delete_branch) + "\n" + result.get(groupPosition).toString())
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String objectId = result.get(groupPosition).getObjectId();
                                    DBManager.getInstance().getParseObjectById(Table.BRANCH, objectId, new FindCallback<Equipment>() {
                                        @Override
                                        public void done(List<Equipment> objects, ParseException e) {
                                            DBManager.getInstance().delete(objects.get(0), new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        popupShortToastMessage(getString(R.string.branch_delete_succes));
                                                        retrieveBranches();
                                                    } else {
                                                        Log.d("ParseError", e.toString());
                                                        popupShortToastMessage(getString(R.string.branch_delete_error));
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
                // code by http://stackoverflow.com/users/1705598/icza

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
