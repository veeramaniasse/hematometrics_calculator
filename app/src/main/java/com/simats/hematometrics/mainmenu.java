package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        Button profileButton = findViewById(R.id.profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(mainmenu.this, profile.class);
                startActivity(intent);
            }
        });

        Button rbcIndicesButton = findViewById(R.id.rbc_indices);
        rbcIndicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RBCIndicesActivity
                Intent intent = new Intent(mainmenu.this, RBCIndices.class);
                startActivity(intent);
            }
        });

        Button reticulocyteButton = findViewById(R.id.reticulocyte);
        reticulocyteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ReticulocyteMenuActivity
                Intent intent = new Intent(mainmenu.this, ReticulocyteMenu.class);
                startActivity(intent);
            }
        });

        Button correctedWBCButton = findViewById(R.id.corrected_wbc);
        correctedWBCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CorrectedWBCCount
                Intent intent = new Intent(mainmenu.this, CorrectedWBCCount.class);
                startActivity(intent);
            }
        });

        Button wbcCountButton = findViewById(R.id.wbc_count);
        wbcCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to WBCCountFormulaActivity
                Intent intent = new Intent(mainmenu.this, WBCcountformula.class);
                startActivity(intent);
            }
        });

        Button plateletCountButton = findViewById(R.id.platelet_count);
        plateletCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to PlateletCountActivity
                Intent intent = new Intent(mainmenu.this, PlateletCountFormula.class);
                startActivity(intent);
            }
        });
    }

}