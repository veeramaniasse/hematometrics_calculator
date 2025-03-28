package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RBCIndices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbcindices);
        Button mcvButton = findViewById(R.id.mcv);
        mcvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MCVFORMULA
                Intent intent = new Intent(RBCIndices.this, MCVFORMULA.class);
                startActivity(intent);
            }
        });

        Button mchButton = findViewById(R.id.mch);
        mchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MCHActivity
                Intent intent = new Intent(RBCIndices.this, mchformula.class);
                startActivity(intent);
            }
        });

        Button mchcButton = findViewById(R.id.mchc);
        mchcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MCHCFormula
                Intent intent = new Intent(RBCIndices.this, MCHCFORMULA.class);
                startActivity(intent);
            }
        });
    }
}