package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private User selectedUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        Intent i = getIntent();

        retrieveBranches();
        populateEROFunctionList();
        populateUserRightsDropdown();

        String userObjId = getIntent().getStringExtra(AdminUserDefaultActivity.USER_EXTRA);

        if(userObjId != null){ // user was added in intent
            findViewById(R.id.inputPassword).setVisibility(View.GONE); // admin cannot change password
            Button button = (Button)findViewById(R.id.addUserButton);
            button.setText(R.string.update_user);
            DBManager.getInstance().getSingleUserById(this, userObjId);
            //
        }
        else{
            System.out.println("NO EXTRA");
        }


    }

    public void populateUserRightsDropdown(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinner_adminrights);
        ArrayAdapter<UserRight> adapter = new ArrayAdapter<UserRight>(this, android.R.layout.simple_spinner_dropdown_item, UserRight.values());
        dropdown.setAdapter(adapter);
    }

    public void retrieveBranches(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                populateBranchesDropdown(DBManager.getInstance().getBranches());
            }
        });
        t.start();
    }

    public void loadSingleUserDetails(List<User> users){
        if(users.size()==1){
            EditText editText;
            selectedUser = users.get(0);
            editText = (EditText)findViewById(R.id.inputName);
            editText.setText(selectedUser.getName());
            editText = (EditText)findViewById(R.id.inputTelephonenumber);
            editText.setText(selectedUser.getTelephoneNumber());
            editText = (EditText)findViewById(R.id.inputEmail);
            editText.setText(selectedUser.getEmail());
            editText = (EditText)findViewById(R.id.inputPassword);
            editText.setText("dummy"); // not used, but needs to be set in order to pass checkFields()


            List<UserEROFunction> userEROFunctions = selectedUser.getEROFunction();
            if(userEROFunctions != null){
                LinearLayout layout = (LinearLayout) findViewById(R.id.ero_functions_ll);
                int count = layout.getChildCount();
                CheckBox checkBox = null;
                ArrayList<UserEROFunction> ERO = new ArrayList<UserEROFunction>();

                for(int i=0, j = 0; i<count && j<userEROFunctions.size(); i++) {
                    checkBox = (CheckBox)layout.getChildAt(i);
                    if(checkBox.getText().toString() == userEROFunctions.get(j).toString()){
                        checkBox.setChecked(true);
                        j++;
                    }
                }

            }

            Spinner spinner;
            spinner = (Spinner)findViewById(R.id.spinner_adminrights);
            ArrayAdapter<UserRight> adapter1;
            adapter1= (ArrayAdapter)spinner.getAdapter();
            spinner.setSelection(adapter1.getPosition(selectedUser.getRight()));

            spinner = (Spinner)findViewById(R.id.spinner_working_at_branch);
            ArrayAdapter<Branch> adapter2;
            adapter2= (ArrayAdapter)spinner.getAdapter();
            spinner.setSelection(adapter2.getPosition(selectedUser.getBranch()));
        }
    }

    public void populateBranchesDropdown(List<ParseObject> branches){
        final List<Branch> items = (List)branches;
        final Spinner dropdown = (Spinner)findViewById(R.id.spinner_working_at_branch);
        dropdown.post(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<Branch> adapter = new ArrayAdapter<Branch>(AdminAddUserActivity.this, android.R.layout.simple_spinner_dropdown_item, items);

                dropdown.setAdapter(adapter);
            }
        });
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
                if(selectedUser==null){ // user is new and should be created
                    createUser();
                }
                else{
                    updateUser();
                }

            }
            else if(anyCheckboxChecked == false){ // none of the checkboxes were checked, is user sure?
                checkBoxDialog();
            }
        }
    }


    public void updateUser(){
        EditText tempField;

        tempField = (EditText)findViewById(R.id.inputName);
        selectedUser.setName(tempField.getText().toString());

        tempField = (EditText)findViewById(R.id.inputTelephonenumber);
        selectedUser.setTelephoneNumber(tempField.getText().toString());

        tempField = (EditText)findViewById(R.id.inputEmail);
        selectedUser.setEmail(tempField.getText().toString());
        selectedUser.setUsername(tempField.getText().toString());

        System.out.println("before spinner");
        Spinner tempSpinner = (Spinner)findViewById(R.id.spinner_adminrights);
        int id = tempSpinner.getSelectedItemPosition();
        UserRight[] userRights = UserRight.values();
        System.out.println(userRights[id] + " " + userRights[id+1]);
        selectedUser.setRight(userRights[id]);

        //tempSpinner = (Spinner)findViewById(R.id.spinner_working_at_branch);
        //userToCreate.setRight(tempSpinner.getSelectedItem().toString());

        Branch branch = (Branch) ( (Spinner) findViewById(R.id.spinner_working_at_branch)).getSelectedItem();
        selectedUser.setBranch(branch);

        LinearLayout layout = (LinearLayout) findViewById(R.id.ero_functions_ll);
        int count = layout.getChildCount();
        CheckBox checkBox = null;
        ArrayList<UserEROFunction> ERO = new ArrayList<UserEROFunction>();
        UserEROFunction[] userEROFunctions = UserEROFunction.values();
        for(int i=0; i<count; i++) {
            checkBox = (CheckBox)layout.getChildAt(i);
            if(checkBox != null && checkBox.isChecked()){
                ERO.add(userEROFunctions[i]);
            }
        }

        if(ERO.isEmpty()){
            ERO.add(UserEROFunction.NONE); // replace with enum
        }
        selectedUser.setEROFunction(ERO);
        DBManager.getInstance().updateUser(selectedUser, this);
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
        System.out.println(userRights[id].toString());
        userToCreate.setRight(userRights[id]);

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