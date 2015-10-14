package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

import nl.debhver.debedrijfshulpverlener.enums.UserEROFunction;
import nl.debhver.debedrijfshulpverlener.enums.UserRight;

/**
 * Created by Koen on 10-10-2015.
 */
public class AdminUserFilterActivity extends HomeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_filter_user);
        addUserEROFunctions();
        addUserRights();

    }

    private void addUserEROFunctions(){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutERO);
        CheckBox checkBox = null;
        for(UserEROFunction function : UserEROFunction.values()){
            checkBox = new CheckBox(getApplicationContext());
            checkBox.setTextColor(Color.BLACK);
            checkBox.setText(function.toString());
            linearLayout.addView(checkBox);
        }
    }

    private void addUserRights(){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayoutRights);
        CheckBox checkBox = null;
        for(UserRight function : UserRight.values()){
            checkBox = new CheckBox(getApplicationContext());
            checkBox.setTextColor(Color.BLACK);
            checkBox.setText(function.toString());
            linearLayout.addView(checkBox);
        }
    }

    public void applyFilterOnClick(View V){
        if(V.getId() == R.id.applyFilterButton){
            Bundle bundleObject = new Bundle();
            ArrayList<String> userRightList = new ArrayList<String>();
            ArrayList<String> userEROFunctionList = new ArrayList<String>();
            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutRights);
            int count = layout.getChildCount();
            CheckBox checkBox = null;
            UserRight[] UserRights = UserRight.values();
            for(int i=0; i<count; i++) {
                checkBox = (CheckBox)layout.getChildAt(i);
                if(checkBox != null && checkBox.isChecked()){
                    userRightList.add(UserRights[i].name());
                }
            }

            bundleObject.putStringArrayList(AdminUserDefaultActivity.FILTER_EXTRA_RIGHTS, userRightList);

            layout = (LinearLayout) findViewById(R.id.linearLayoutERO);
            count = layout.getChildCount();
            checkBox = null;
            UserEROFunction[] userEROFunctions = UserEROFunction.values();
            for(int i=0; i<count; i++) {
                checkBox = (CheckBox)layout.getChildAt(i);
                if(checkBox != null && checkBox.isChecked()){
                    userEROFunctionList.add(userEROFunctions[i].name());
                }
            }
            bundleObject.putStringArrayList(AdminUserDefaultActivity.FILTER_EXTRA_ERO, userEROFunctionList);

            returnFilters(bundleObject);
        }
    }

    void returnFilters(Bundle bundle){
        Intent returnIntent = new Intent();
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
