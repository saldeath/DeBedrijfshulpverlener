package nl.debhver.debedrijfshulpverlener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.model.User;

/**
 * Created by Koen on 6-10-2015.
 */
public class AdminAddUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        retrieveBranches();
        retrieveEROFunctions();
        retrieveAdminRightsAndCreateDropdown();
    }

    public void retrieveAdminRightsAndCreateDropdown(){

    }



    public void retrieveBranches(){
        DBManager.getInstance().getBranchNames(this);
    }

    public void populateBranchesDropdown(List<ParseObject> branches){
        ArrayList<String> items = new ArrayList<String>();

        for (ParseObject branch : branches) {
            items.add(branch.get("name").toString());
        }

        Spinner dropdown = (Spinner)findViewById(R.id.spinner_working_at_branch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

    }

    public void retrieveEROFunctions(){
        DBManager.getInstance().getEROFunctions(this);
    }

    public void populateEROFunctionList(List<ParseObject> functions){
        LinearLayout ero_functions_ll = (LinearLayout)findViewById(R.id.ero_functions_ll);
        for (ParseObject function : functions) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setTextColor(Color.BLACK);

            //checkBox.setButtonDrawable(Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android")); // needed to make the button more visible
            if(Locale.getDefault().getLanguage().toString().equals("nl")) {
                checkBox.setText(function.get("dutch_function").toString());
            }
            else {
                checkBox.setText(function.get("english_function").toString());
            }
            ero_functions_ll.addView(checkBox);
        }
    }


    void popupShortToastMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }



    public void createUser(View v){
        if(v.getId() == R.id.addUserButton){
            User userToCreate = new User();

            EditText tempField = (EditText)findViewById(R.id.inputName);
            userToCreate.setName(tempField.getText().toString());
            tempField = (EditText)findViewById(R.id.inputEmail);
            userToCreate.setUsername(tempField.getText().toString()); // username is also email
            userToCreate.setEmail(tempField.getText().toString());
            tempField = (EditText)findViewById(R.id.inputTelephonenumber);
            userToCreate.setTelephoneNumber(tempField.getText().toString());

            Spinner tempSpinner = (Spinner)findViewById(R.id.spinner_adminrights);
            if(tempSpinner.getSelectedItem() != null){
                userToCreate.setRight(tempSpinner.getSelectedItem().toString());
            }

            //userToCreate.setBranch();

            LinearLayout layout = (LinearLayout) findViewById(R.id.ero_functions_ll);
            int count = layout.getChildCount();
            CheckBox checkBox = null;
            String ERO = "";
            for(int i=0; i<count; i++) {
                checkBox = (CheckBox)layout.getChildAt(i);
                if(checkBox != null && checkBox.isChecked()){
                    if(ERO.equals("")){
                        ERO = checkBox.getText().toString();
                    }
                    else{
                        ERO += ",";
                        ERO += checkBox.getText().toString();
                    }

                }
            }

            userToCreate.setEROFunction(ERO);

            DBManager.getInstance().createUser(userToCreate, this);
        }




    }


}