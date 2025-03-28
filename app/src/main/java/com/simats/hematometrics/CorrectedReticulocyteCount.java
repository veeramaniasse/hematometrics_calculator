package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CorrectedReticulocyteCount extends AppCompatActivity {

    private EditText reticulocytePcvEditText;
    private EditText pcvEditText;
    private EditText averagePcvEditText;
    private TextView outputTextView;
    private Button calculateButton;
    private Button saveButton;

    private EditText reticulocyteHbEditText;
    private EditText hbEditText;
    private EditText averageHbEditText;
    private TextView outputTextView2;
    private Button calculateButton2;
    private Button saveButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corrected_reticulocyte_count);

        // For PCV calculation
        reticulocytePcvEditText = findViewById(R.id.reticulocyte1);
        pcvEditText = findViewById(R.id.pcv);
        averagePcvEditText = findViewById(R.id.rbcCount);
        outputTextView = findViewById(R.id.output);
        calculateButton = findViewById(R.id.calculate);
        saveButton = findViewById(R.id.save);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values entered by the user
                String reticulocyteStr = reticulocytePcvEditText.getText().toString();
                String pcvStr = pcvEditText.getText().toString();
                String averagePcvStr = averagePcvEditText.getText().toString();

                // Check if any field is empty
                if (reticulocyteStr.isEmpty() || pcvStr.isEmpty() || averagePcvStr.isEmpty()) {
                    // If any field is empty, don't calculate and show a message
                    outputTextView.setText("Please fill all the fields.");
                    outputTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Parse the input values to double
                double reticulocyte = Double.parseDouble(reticulocyteStr);
                double pcv = Double.parseDouble(pcvStr);
                double averagePcv = Double.parseDouble(averagePcvStr);

                // Calculate corrected reticulocyte count using the PCV formula
                double correctedReticulocyteCount = (reticulocyte * pcv) / averagePcv;

                // Display the result in the output TextView
                outputTextView.setText(String.valueOf(correctedReticulocyteCount));
                outputTextView.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
            }
        });

        // For Hb calculation
        reticulocyteHbEditText = findViewById(R.id.reticulocyte2);
        hbEditText = findViewById(R.id.pcv2);
        averageHbEditText = findViewById(R.id.Rbccount2);
        outputTextView2 = findViewById(R.id.output2);
        calculateButton2 = findViewById(R.id.calculate2);
        saveButton2 = findViewById(R.id.save2);

        calculateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values entered by the user
                String reticulocyteStr = reticulocyteHbEditText.getText().toString();
                String hbStr = hbEditText.getText().toString();
                String averageHbStr = averageHbEditText.getText().toString();

                // Check if any field is empty
                if (reticulocyteStr.isEmpty() || hbStr.isEmpty() || averageHbStr.isEmpty()) {
                    // If any field is empty, don't calculate and show a message
                    outputTextView2.setText("Please fill all the fields.");
                    outputTextView2.setVisibility(View.VISIBLE);
                    return;
                }

                // Parse the input values to double
                double reticulocyte = Double.parseDouble(reticulocyteStr);
                double hb = Double.parseDouble(hbStr);
                double averageHb = Double.parseDouble(averageHbStr);

                // Calculate corrected reticulocyte count using the Hb formula
                double correctedReticulocyteCount = (reticulocyte * hb) / averageHb;

                // Display the result in the output TextView
                outputTextView2.setText(String.valueOf(correctedReticulocyteCount));
                outputTextView2.setVisibility(View.VISIBLE);
                saveButton2.setVisibility(View.VISIBLE);
            }
        });
    }
}
