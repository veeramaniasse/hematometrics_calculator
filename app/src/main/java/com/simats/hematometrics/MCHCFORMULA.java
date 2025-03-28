package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MCHCFORMULA extends AppCompatActivity {
    EditText HbInput, pcvInput;
    Button calculateButton;
    TextView outputText;
    private String x="";
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mchcformula);
        // Initialize UI elements
        HbInput = findViewById(R.id.hb);
        pcvInput = findViewById(R.id.pcv);
        calculateButton = findViewById(R.id.calculate);
        outputText = findViewById(R.id.output);

        saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!x.equals("")){
                    sendLoginRequest(x);
                }

            }
        });

        // Set OnClickListener for the Calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMCHC();
            }
        });
    }

    private void sendLoginRequest(final String val) {
        String URL = ip.ipn+"result.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar calendar = Calendar.getInstance();

                // Create a SimpleDateFormat object with the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Format the current date
                String currentDate = dateFormat.format(calendar.getTime());
                // Send the username and password as POST parameters
                Map<String, String> data = new HashMap<>();
                data.put("date", currentDate);
                data.put("subid", "1.3");
                data.put("catid", "1");
                data.put("resultval", val);
                return data;
            }
        };

        // Customize the retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Initialize the Volley request queue and add the request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // Handle the JSON response
    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        // Handle your JSON response here without assuming a 'status' field
        // You can parse the response and handle success/failure accordingly
        try {
            // Example: Check if the response contains "success"
            if (response.toLowerCase().contains("true")) {
                Toast.makeText(this, "Data saved successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MCHCFORMULA.this, MainActivity2.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(this, "Request timed out. Check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(error.toString().trim());
            Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateMCHC() {
        // Get Hb and PCV Count inputs
        String HbString = HbInput.getText().toString().trim();
        String pcvString = pcvInput.getText().toString().trim();

        // Check if inputs are not empty
        if (!HbString.isEmpty() && !pcvString.isEmpty()) {
            // Parse inputs to double
            double Hb = Double.parseDouble(HbString);
            double pcv = Double.parseDouble(pcvString);

            // Calculate MCHC using the formula
            double mchc = (Hb / pcv) * 100;
            x = String.format("%.2f", mchc);

            // Display the result with units "g/dL"
            String formattedResult = String.format("%.2f g/dL", mchc);
            outputText.setText(formattedResult); // Adjust format as needed

            // Show the output TextView and the save button
            outputText.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getApplicationContext(), "Enter values", Toast.LENGTH_SHORT).show();
            // Handle case when inputs are empty
            outputText.setText("Please enter values for Hb and PCV");
            // Hide the output TextView and the save button
            outputText.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
    }
}
