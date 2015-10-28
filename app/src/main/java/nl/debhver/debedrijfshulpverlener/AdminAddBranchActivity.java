package nl.debhver.debedrijfshulpverlener;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.enums.Table;
import nl.debhver.debedrijfshulpverlener.models.Branch;

public class AdminAddBranchActivity extends HomeActivity{
    private Branch selectedBranch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String branchObjId = getIntent().getStringExtra(AdminBranchDefaultActivity.BRANCH_EXTRA);

        if(branchObjId != null)
            setTitle(R.string.title_activity_admin_branch_edit);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_branch);
        setBackButtonOnToolbar(true);

        if(branchObjId != null){ // user was added in intent
            showProgressBar(true);
            DBManager.getInstance().getParseObjectById(Table.BRANCH, branchObjId, new FindCallback<Branch>() {
                @Override
                public void done(List<Branch> objects, ParseException e) {
                    if(e==null)
                        loadSingleBranchDetails(objects);
                }
            });
        }
        else{
            System.out.println("NO EXTRA");
        }
    }

    public boolean checkFields(){
        EditText tempField;
        tempField = (EditText)findViewById(R.id.inputName);
        boolean valid = true;

        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_name));
            valid = false;
        }

        tempField = (EditText)findViewById(R.id.inputCity);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_city));
            valid = false;
        }

        tempField = (EditText)findViewById(R.id.inputPostalCode);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_postal_code));
            valid = false;
        }

        tempField = (EditText)findViewById(R.id.inputAddress);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_address));
            valid = false;
        }
        return valid;
    }

    public void FABSaveClicked(View view){
        if(checkFields()){

            if(selectedBranch == null) {
                selectedBranch = new Branch();
            }

            EditText tempField;

            tempField = (EditText)findViewById(R.id.inputName);
            selectedBranch.setName(tempField.getText().toString());

            tempField = (EditText)findViewById(R.id.inputCity);
            selectedBranch.setCity(tempField.getText().toString());

            tempField = (EditText)findViewById(R.id.inputPostalCode);
            selectedBranch.setZipCode(tempField.getText().toString());

            tempField = (EditText)findViewById(R.id.inputAddress);
            selectedBranch.setAddress(tempField.getText().toString());

            DBManager.getInstance().save(selectedBranch, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null) {
                        setSaved(true);
                        popupShortToastMessage(getString(R.string.branch_save_succes));
                        finish();
                    } else {
                        popupShortToastMessage(getString(R.string.branch_save_error));
                    }
                }
            });
        }
    }

    public void loadSingleBranchDetails(List<Branch> branches) {
        if(branches.size()==1){
            EditText editText;
            selectedBranch = branches.get(0);
            editText = (EditText)findViewById(R.id.inputName);
            editText.setText(selectedBranch.getName());
            editText = (EditText)findViewById(R.id.inputCity);
            editText.setText(selectedBranch.getCity());
            editText = (EditText)findViewById(R.id.inputPostalCode);
            editText.setText(selectedBranch.getZipCode());
            editText = (EditText)findViewById(R.id.inputAddress);
            editText.setText(selectedBranch.getAddress());
        }
        showProgressBar(false);
    }
}
