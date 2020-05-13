package com.example.warehouse.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.warehouse.R;
import com.example.warehouse.scanner.ScannerClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Activity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    public void onScanButtonClick(View v) {
        Intent scannerIntent = new Intent(this, ScannerClass.class);
        startActivity(scannerIntent);
    }

    public void onScanButtonClick(MenuItem menuItem) {
        Intent scannerIntent = new Intent(this, ScannerClass.class);
        startActivity(scannerIntent);
    }

    public void onSettingsManagementButtonClick(View v) {
        Intent settingsListIntent = new Intent(this, SettingsList.class);
        startActivity(settingsListIntent);
    }

    public void onSettingsManagementButtonClick(MenuItem menuItem) {
        Intent settingsListIntent = new Intent(this, SettingsList.class);
        startActivity(settingsListIntent);
        finish();
    }

    public void onItemManagementButtonClick(MenuItem menuItem) {
        Intent itemManagementIntent = new Intent(this, MainActivity.class);
        startActivity(itemManagementIntent);
        finish();
    }

    public void onCategoryManagementButtonClick(MenuItem menuItem) {
        Intent categoryManagementIntent = new Intent(this, CategoryList.class);
        startActivity(categoryManagementIntent);
        finish();
    }

    public void onAttributeManagementButtonClick(MenuItem menuItem) {
        Intent attributeManagementIntent = new Intent(this, AttributeList.class);
        startActivity(attributeManagementIntent);
        finish();
    }

    public void onUserManagementButtonClick(MenuItem menuItem) {
        Intent userManagementIntent = new Intent(this, UserList.class);
        startActivity(userManagementIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
