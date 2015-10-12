package nl.debhver.debedrijfshulpverlener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    private Toolbar toolbar;
//    private NavigationView drawer;
//    private DrawerLayout drawerLayout;
//    ActionBarDrawerToggle drawerToggle;

    private DrawerLayout fullView;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView drawer;
    private String currentActivityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        //initMenu();

    }

    @Override
    public void setContentView(int layoutResID) {

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

    }

    private void addAdminOptions(Menu menu) {
        menu.add(R.string.title_activity_admin_equipment_default);
        MenuItem item = menu.getItem(menu.size()-1);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!currentActivityName.equals(getString(R.string.title_activity_admin_equipment_default))) {
                    Intent i = new Intent(HomeActivity.this, AdminEquipmentDefaultActivity.class);
                    startActivity(i);
                    return true;
                }
                fullView.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        menu.add(R.string.title_activity_admin_equipment_add);
        item = menu.getItem(menu.size()-1);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //if(!currentActivityName.equals(getString(R.string.title_activity_admin_equipment_default))){
                    Intent i = new Intent(HomeActivity.this, AdminDefaultActivity.class);
                    startActivity(i);
                    return true;
                //}
                //fullView.closeDrawer(GravityCompat.START);
                //return false;
            }
        });
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
        Intent intent = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_item_1:
                fullView.closeDrawer(GravityCompat.START);
                //intent down here
                return true;
        }

        return false;
    }

    public void logoutButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setCurrentActivityName(String name) {
        currentActivityName = name;
    }
}
