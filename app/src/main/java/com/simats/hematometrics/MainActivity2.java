package com.simats.hematometrics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.simats.hematometrics.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView im1 = findViewById(R.id.toolbar_image);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(MainActivity2.this, profile.class);
                startActivity(intent);
            }
        });

        Button rbcIndicesButton = findViewById(R.id.rbc_indices);
        rbcIndicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RBCIndicesActivity
                Intent intent = new Intent(MainActivity2.this, RBCIndices.class);
                startActivity(intent);
            }
        });

        Button reticulocyteButton = findViewById(R.id.reticulocyte);
        reticulocyteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ReticulocyteMenuActivity
                Intent intent = new Intent(MainActivity2.this, ReticulocyteMenu.class);
                startActivity(intent);
            }
        });

        Button correctedWBCButton = findViewById(R.id.corrected_wbc);
        correctedWBCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CorrectedWBCCount
                Intent intent = new Intent(MainActivity2.this, CorrectedWBCCount.class);
                startActivity(intent);
            }
        });

        Button wbcCountButton = findViewById(R.id.wbc_count);
        wbcCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to WBCCountFormulaActivity
                Intent intent = new Intent(MainActivity2.this, WBCcountformula.class);
                startActivity(intent);
            }
        });

        Button plateletCountButton = findViewById(R.id.platelet_count);
        plateletCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PlateletCountActivity
                Intent intent = new Intent(MainActivity2.this, PlateletCountFormula.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) { // Use R.id.profile here
            Log.d("MenuItemClicked", "Profile clicked");
            startActivity(new Intent(this, Previoushistory.class));
            return true;
        } else if (id == R.id.nav_slideshow) { // Use R.id.logout here
            startActivity(new Intent(this, login.class));
            return true;
        }
        else if (id == R.id.nav_gallery) { // Use R.id.logout here
            startActivity(new Intent(this, Aboutus.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) { // Use R.id.profile here
            Log.d("MenuItemClicked", "Profile clicked");
            startActivity(new Intent(this, Previoushistory.class));
            return true;
        } else if (id == R.id.nav_slideshow) { // Use R.id.logout here
            startActivity(new Intent(this, login.class));
            return true;
        }
        else if (id == R.id.nav_gallery) { // Use R.id.logout here
            startActivity(new Intent(this, Aboutus.class));
            return true;
        }
        return false;
    }
}