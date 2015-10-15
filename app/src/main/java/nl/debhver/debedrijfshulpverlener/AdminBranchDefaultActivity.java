package nl.debhver.debedrijfshulpverlener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
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
import com.parse.ParseException;
import com.parse.SaveCallback;

import nl.debhver.debedrijfshulpverlener.models.Branch;


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
        DBManager.getInstance().getBranchesOld(this);
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

    public void prepareListData(final List<Branch> branchListPara) {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);


        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> options = new ArrayList<String>();
        options.add("Wijzigen");
        options.add("Verwijderen");
        // Adding header/child data
        int i = 0;
        for (Branch b : branchListPara) {
            listDataHeader.add(i, b.toString());
            listDataChild.put(listDataHeader.get(i), options); // Header, Child data
            i++;
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(childPosition == 0){
                    Intent intent = new Intent(AdminBranchDefaultActivity.this, AdminAddBranchActivity.class);
                    intent.putExtra(BRANCH_EXTRA, branchListPara.get(groupPosition).getObjectId());
                    AdminBranchDefaultActivity.this.startActivity(intent);
                }
                if(childPosition == 1){
                    branchListPara.get(groupPosition).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getApplicationContext(), "Branch was deleted", Toast.LENGTH_SHORT).show();
                                retrieveBranches();
                            }else{
                                Toast.makeText(getApplicationContext(), "Branch was not deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return true;
            }
        });
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
