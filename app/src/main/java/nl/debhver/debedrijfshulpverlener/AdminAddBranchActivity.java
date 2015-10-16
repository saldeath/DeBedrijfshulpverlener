package nl.debhver.debedrijfshulpverlener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.enums.UserRight;
import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.User;

public class AdminAddBranchActivity extends HomeActivity {
    private Branch selectedBranch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_branch);
        setBackButtonOnToolbar(true);

        String branchObjId = getIntent().getStringExtra(AdminBranchDefaultActivity.BRANCH_EXTRA);
        if(branchObjId != null){ // user was added in intent
            Button button = (Button)findViewById(R.id.buttonAddBranch);
            button.setText(R.string.update_branch);
            DBManager.getInstance().getSingleBranchById(this, branchObjId);
            //
        }
        else{
            System.out.println("NO EXTRA");
        }
    }

    public boolean checkFields(){
        EditText tempField;
        tempField = (EditText)findViewById(R.id.inputName);

        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_name));
            return false;
        }

        tempField = (EditText)findViewById(R.id.inputCity);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_telephone));
            return false;
        }

        tempField = (EditText)findViewById(R.id.inputPostalCode);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_email));
            return false;
        }

        tempField = (EditText)findViewById(R.id.inputAddress);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_password));
            return false;
        }

        else {
            return true;
        }
    }

    public void addBranch(View view){
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

            DBManager.getInstance().createBranch(selectedBranch, this);

            finish();
        }
    }

    public void clearFieldsAfterAddingBranch() {
        EditText tempField;
        tempField = (EditText) findViewById(R.id.inputName);
        tempField.setText("");
        tempField = (EditText) findViewById(R.id.inputCity);
        tempField.setText("");
        tempField = (EditText) findViewById(R.id.inputPostalCode);
        tempField.setText("");
        tempField = (EditText) findViewById(R.id.inputAddress);
        tempField.setText("");
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
        }}
}
