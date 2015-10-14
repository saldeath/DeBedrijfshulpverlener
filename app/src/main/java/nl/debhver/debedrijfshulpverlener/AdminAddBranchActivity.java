package nl.debhver.debedrijfshulpverlener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import nl.debhver.debedrijfshulpverlener.models.Branch;

public class AdminAddBranchActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_branch);
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

    void popupShortToastMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void addBranch(View view){
        if(checkFields()){
            Branch branch = new Branch();
            EditText tempField;

            tempField = (EditText)findViewById(R.id.inputName);
            branch.setName(tempField.getText().toString());

            tempField = (EditText)findViewById(R.id.inputCity);
            branch.setCity(tempField.getText().toString());

            tempField = (EditText)findViewById(R.id.inputPostalCode);
            branch.setZipCode(tempField.getText().toString());

            tempField = (EditText)findViewById(R.id.inputAddress);
            branch.setAddress(tempField.getText().toString());

            DBManager.getInstance().createBranch(branch, this);
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
}
