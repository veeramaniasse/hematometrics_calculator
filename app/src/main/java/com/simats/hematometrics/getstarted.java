package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class getstarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        Button getStartedButton = findViewById(R.id.getstarted);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When "Get Started" button is clicked, navigate to the signin activity
                Intent intent = new Intent(getstarted.this, Signin.class);
                startActivity(intent);
            }
        });
    }
}