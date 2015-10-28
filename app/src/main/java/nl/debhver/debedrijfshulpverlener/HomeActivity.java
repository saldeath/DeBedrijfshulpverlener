package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import android.widget.TextView;

import com.parse.ParseException;

import android.widget.Toast;

import com.parse.ParseUser;

import nl.debhver.debedrijfshulpverlener.enums.UserRight;
import nl.debhver.debedrijfshulpverlener.models.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    private Toolbar toolbar;
//    private NavigationView drawer;
//    private DrawerLayout drawerLayout;
//    ActionBarDrawerToggle drawerToggle;

    private DrawerLayout fullView;
    private ActionBarDrawerToggle drawerToggle;
    private String currentActivityName = "";

    private TextView currentUser, currentBranch;
    public static User getUser() {
        return (User) User.getCurrentUser();
    }

    private boolean finishWarning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivityName = this.getClass().toString();
        //setContentView(R.layout.activity_home);
        //initMenu();

    }

    private void setValuesHeader() throws ParseException {
        currentUser = (TextView)findViewById(R.id.app_header_name);
        currentBranch = (TextView)findViewById(R.id.app_header_branch);

        currentUser.setText(getUser().getName());
        currentBranch.setText(getUser().getBranch().fetchIfNeeded().getString("name"));

    }

    @Override
    public void setContentView(int layoutResID) {
        NavigationView drawer;

        fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_home, null);
        RelativeLayout activityContainer = (RelativeLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        drawer = (NavigationView)findViewById(R.id.main_drawer);
        drawer.setNavigationItemSelectedListener(this);

        addAdminOptions(drawer.getMenu());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this,
                fullView,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);


        fullView.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        try {
            setValuesHeader();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addAdminOptions(Menu menu) {
        UserRight right = getUser().getRight();
        if(right != UserRight.NONE) {
            MenuItem item;
            if(right != UserRight.TRAINING) {
                menu.add(R.string.title_activity_admin_equipment_default);
                item = menu.getItem(menu.size() - 1);
                item.setIcon(getDrawable(R.drawable.ic_add_fire_extinguisher_18dp));
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        fullView.closeDrawer(GravityCompat.START);
                        if (!currentActivityName.equals(AdminEquipmentDefaultActivity.class + "")) {
                            Intent i = new Intent(HomeActivity.this, AdminEquipmentDefaultActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
            if(right != UserRight.EQUIPMENT) {
                menu.add(R.string.title_activity_admin_training_default);
                item = menu.getItem(menu.size() - 1);
                item.setIcon(getDrawable(R.drawable.ic_add_file_document_box_white_18dp));
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        fullView.closeDrawer(GravityCompat.START);
                        if (!currentActivityName.equals(TrainingActivity.class + "")) {
                            Intent i = new Intent(HomeActivity.this, TrainingActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
            if(right == UserRight.ADMIN) {
                menu.add(R.string.title_activity_admin_user_default);
                item = menu.getItem(menu.size() - 1);
                item.setIcon(getDrawable(R.drawable.ic_add_account_white_18dp));
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        fullView.closeDrawer(GravityCompat.START);
                        if (!currentActivityName.equals(AdminUserDefaultActivity.class + "")) {
                            Intent i  = new Intent(HomeActivity.this, AdminUserDefaultActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
                menu.add(R.string.title_activity_admin_branch_default);
                item = menu.getItem(menu.size() - 1);
                item.setIcon(getDrawable(R.drawable.ic_add_branch_18dp));
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        fullView.closeDrawer(GravityCompat.START);
                        if (!currentActivityName.equals(AdminBranchDefaultActivity.class + "")) {
                            Intent i  = new Intent(HomeActivity.this, AdminBranchDefaultActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            logoutButtonClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void initMenu() {
//        toolbar = (Toolbar)findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//
//        drawer = (NavigationView)findViewById(R.id.main_drawer);
//        drawer.setNavigationItemSelectedListener(this);
//        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//        drawerToggle = new ActionBarDrawerToggle(this,
//                drawerLayout,
//                toolbar,
//                R.string.drawer_open,
//                R.string.drawer_close);
//
//        drawerLayout.setDrawerListener(drawerToggle);
//        drawerToggle.syncState();
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_0:
                fullView.closeDrawer(GravityCompat.START);
                if (!currentActivityName.equals(HomeUserActivity.class + "")) {
                    intent = new Intent(HomeActivity.this, HomeUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

        }
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_1:
                fullView.closeDrawer(GravityCompat.START);
                if (!currentActivityName.equals(HomeUserActivity.class + "")) {

                    return true;
                }

        }
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_2:
                fullView.closeDrawer(GravityCompat.START);
                if (!currentActivityName.equals(UserEquipmentDefaultActivity.class + "")) {
                    intent = new Intent(HomeActivity.this, UserEquipmentDefaultActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

        }
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_3:
                fullView.closeDrawer(GravityCompat.START);
                if (!currentActivityName.equals(UserIncidentDefaultActivity.class + "")) {
                    intent = new Intent(HomeActivity.this, UserIncidentDefaultActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

        }
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_4:
                fullView.closeDrawer(GravityCompat.START);
                if (!currentActivityName.equals(EmergencyManualsActivity.class + "")) {
                    intent = new Intent(HomeActivity.this, EmergencyManualsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

        }

        return false;
    }

    public void logoutButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.warning_logout);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ParseUser.logOut();
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void popupShortToastMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void setBackButtonOnToolbar(Boolean finishWarning) {
        ActionBar t = getSupportActionBar();
        t.setDisplayHomeAsUpEnabled(true);
        this.finishWarning = finishWarning;
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    protected void setSaved(Boolean saved) {
        finishWarning = !saved;
    }

    @Override
    public void finish() {
        if(finishWarning) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    HomeActivity.this);
            alertDialogBuilder
                    .setTitle(R.string.warning)
                    .setMessage(getString(R.string.warning_exit_without_save))
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HomeActivity.super.finish();
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
        } else {
            HomeActivity.super.finish();
        }
    }

    public String getBranchObjectId(){
        return getUser().getBranch().getObjectId();
    }
}
