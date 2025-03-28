package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReticulocyteMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reticulocyte_menu);
        Button reticulocyteCountButton = findViewById(R.id.reticulocytecount);
        reticulocyteCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReticulocyteMenu.this, reticulocytecount.class));
            }
        });

        Button absoluteReticulocyteCountButton = findViewById(R.id.absoluteretic);
        absoluteReticulocyteCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReticulocyteMenu.this, AbsoluteReticulocyteCount.class));
            }
        });

        Button correctedReticulocyteCountButton = findViewById(R.id.corrected_reticulocyte);
        correctedReticulocyteCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReticulocyteMenu.this, CorrectedReticulocyteCount.class));
            }
        });


        Button reticulocyteProductionIndexButton = findViewById(R.id.reticulocyte_production);
        reticulocyteProductionIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReticulocyteMenu.this, ReticulocyteProductionIndex.class));
            }
        });
    }
}