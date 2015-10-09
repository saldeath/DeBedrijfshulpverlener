package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import nl.debhver.debedrijfshulpverlener.enums.UserEROFunction;
import nl.debhver.debedrijfshulpverlener.enums.UserRight;
import nl.debhver.debedrijfshulpverlener.models.Branch;
import nl.debhver.debedrijfshulpverlener.models.User;

/**
 * Created by Koen on 6-10-2015.
 */
public class AdminAddUserActivity extends HomeActivity {
    private boolean anyCheckboxChecked = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        retrieveBranches();
        populateEROFunctionList();
        populateUserRightsDropdown();
    }

    public void populateUserRightsDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinner_adminrights);
        ArrayAdapter<UserRight> adapter = new ArrayAdapter<UserRight>(this, android.R.layout.simple_spinner_dropdown_item, UserRight.values());
        dropdown.setAdapter(adapter);
    }

    public void retrieveBranches(){
        DBManager.getInstance().getBranchNames(this);
    }

    public void populateBranchesDropdown(List<ParseObject> branches){
        List<Branch> items = (List)branches;
        Spinner dropdown = (Spinner)findViewById(R.id.spinner_working_at_branch);
        ArrayAdapter<Branch> adapter = new ArrayAdapter<Branch>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

    }

    public void populateEROFunctionList(){
        LinearLayout ero_functions_ll = (LinearLayout)findViewById(R.id.ero_functions_ll);
        for (UserEROFunction function : UserEROFunction.values()) {
            if(function == UserEROFunction.NONE){
                continue;
            }
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setTextColor(Color.BLACK);
            checkBox.setText(function.toString());
            //checkBox.setButtonDrawable(Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android")); // needed to make the button more visible
            ero_functions_ll.addView(checkBox);
        }
    }


    void popupShortToastMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public boolean checkFields(){
        EditText tempField;
        anyCheckboxChecked=true;
        tempField = (EditText)findViewById(R.id.inputName);

        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_name));
            return false;
        }

        tempField = (EditText)findViewById(R.id.inputTelephonenumber);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_telephone));
            return false;
        }

        tempField = (EditText)findViewById(R.id.inputEmail);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_email));
            return false;
        }

        tempField = (EditText)findViewById(R.id.inputPassword);
        if(tempField.getText().toString().isEmpty()){
            this.popupShortToastMessage(getString(R.string.error_empty_password));
            return false;
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.ero_functions_ll);
        int count = layout.getChildCount();
        CheckBox checkBox = null;
        anyCheckboxChecked = false;
        for(int i=0; i<count; i++) {
            checkBox = (CheckBox)layout.getChildAt(i);
            if(checkBox != null && checkBox.isChecked()){
                anyCheckboxChecked = true;
            }
        }
        if(anyCheckboxChecked == false){
            return false;
        }

        Spinner tempSpinner = (Spinner)findViewById(R.id.spinner_adminrights);
        if(tempSpinner.getSelectedItem() != null){
            if(tempSpinner.getSelectedItem().toString().isEmpty()){
                this.popupShortToastMessage(getString(R.string.error_empty_adminrights));
                return false;
            }
        }

        tempSpinner = (Spinner)findViewById(R.id.spinner_working_at_branch);
        if(tempSpinner.getSelectedItem() != null){
            if(tempSpinner.getSelectedItem().toString().isEmpty()){
                this.popupShortToastMessage(getString(R.string.error_empty_branch));
                return false;
            }
        }
        else{
            this.popupShortToastMessage(getString(R.string.error_empty_branch));
            Log.d("Error","No item selected? Failed to retrieve from database?");
            return false;
        }

        return true;
    }

    public void checkBoxDialog(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AdminAddUserActivity.this);

        // set title
        alertDialogBuilder.setTitle("");

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.error_empty_erofunctions))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        AdminAddUserActivity.this.createUser();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void addUserOnClick(View v){
        if(v.getId() == R.id.addUserButton) {
            boolean checkFields;
            checkFields = checkFields();
            if(checkFields == true){ // fields were ok, create user
                createUser();
            }
            else if(anyCheckboxChecked == false){ // none of the checkboxes were checked, is user sure?
                checkBoxDialog();
            }
        }


    }

    public void createUser(){
        User userToCreate = new User();
        EditText tempField;

        tempField = (EditText)findViewById(R.id.inputName);
        userToCreate.setName(tempField.getText().toString());

        tempField = (EditText)findViewById(R.id.inputTelephonenumber);
        userToCreate.setTelephoneNumber(tempField.getText().toString());

        tempField = (EditText)findViewById(R.id.inputEmail);
        userToCreate.setEmail(tempField.getText().toString());
        userToCreate.setUsername(tempField.getText().toString());

        tempField = (EditText)findViewById(R.id.inputPassword);
        userToCreate.setPassword(tempField.getText().toString());

        Spinner tempSpinner = (Spinner)findViewById(R.id.spinner_adminrights);
        int id = tempSpinner.getSelectedItemPosition();
        UserRight[] userRights = UserRight.values();
        System.out.println(userRights[id] + " " + userRights[id+1]);
        userToCreate.setRight(userRights[id + 1]);

        //tempSpinner = (Spinner)findViewById(R.id.spinner_working_at_branch);
        //userToCreate.setRight(tempSpinner.getSelectedItem().toString());

        Branch branch = (Branch) ( (Spinner) findViewById(R.id.spinner_working_at_branch)).getSelectedItem();
        userToCreate.setBranch(branch);

        LinearLayout layout = (LinearLayout) findViewById(R.id.ero_functions_ll);
        int count = layout.getChildCount();
        CheckBox checkBox = null;
        ArrayList<UserEROFunction> ERO = new ArrayList<UserEROFunction>();
        for(int i=0; i<count; i++) {
            checkBox = (CheckBox)layout.getChildAt(i);
            if(checkBox != null && checkBox.isChecked()){
                UserEROFunction[] userEROFunctions = UserEROFunction.values();
                ERO.add(userEROFunctions[i]);
                System.out.println(userEROFunctions[i]);
                //ERO.add(checkBox.getText().toString());
            }
        }

        if(ERO.isEmpty()){
            ERO.add(UserEROFunction.NONE); // replace with enum
        }
        userToCreate.setEROFunction(ERO);



        DBManager.getInstance().createUser(userToCreate, this);
    }

    public void clearFieldsAfterAddingUser(){
        EditText tempField;
        tempField = (EditText)findViewById(R.id.inputName);
        tempField.setText("");
        tempField = (EditText)findViewById(R.id.inputTelephonenumber);
        tempField.setText("");
        tempField = (EditText)findViewById(R.id.inputEmail);
        tempField.setText("");
        tempField = (EditText)findViewById(R.id.inputPassword);
        tempField.setText("");

        Spinner spinner;
        spinner = (Spinner)findViewById(R.id.spinner_adminrights);
        spinner.setSelection(0, true);
        spinner = (Spinner)findViewById(R.id.spinner_working_at_branch);
        spinner.setSelection(0,true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.ero_functions_ll);
        int count = layout.getChildCount();
        CheckBox checkBox = null;
        ArrayList<String> ERO = new ArrayList<String>();
        for(int i=0; i<count; i++) {
            checkBox = (CheckBox)layout.getChildAt(i);
            checkBox.setChecked(false);
        }
    }
}