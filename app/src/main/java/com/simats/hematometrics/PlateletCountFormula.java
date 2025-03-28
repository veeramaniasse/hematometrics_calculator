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

public class PlateletCountFormula extends AppCompatActivity {

    private EditText plateletEditText, valueEditText;
    private TextView outputTextView;
    private String x="";
    private Button calculateButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platelet_count_formula);
        // Initialize UI elements
        plateletEditText = findViewById(R.id.platelet);

        outputTextView = findViewById(R.id.output);
        calculateButton = findViewById(R.id.calculate); // Corrected the ID
        saveButton = findViewById(R.id.save);

        // Set OnClickListener for the Calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePlateletCount();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!x.equals("")){
                    sendLoginRequest(x);
                }

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
                data.put("subid", "0");
                data.put("catid", "5");
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
                Intent intent = new Intent(PlateletCountFormula.this, MainActivity2.class);

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
    private void calculatePlateletCount() {
        // Get user input from EditText fields
        String plateletStr = plateletEditText.getText().toString().trim();
        String valueStr = "10";

        // Check if the input fields are not empty
        if (!plateletStr.isEmpty() && !valueStr.isEmpty()) {
            // Parse input to double
            double Numberofplateletin10oilimmersionfield= Double.parseDouble(plateletStr);
            double value = Double.parseDouble(valueStr);

            // Calculate platelet count
            double plateletCount = (Numberofplateletin10oilimmersionfield / 10) * 20000;
            x=String.format("%.2f", plateletCount);
            // Display the result in the output TextView
            outputTextView.setText(String.format("%.2f", plateletCount));
            outputTextView.setVisibility(View.VISIBLE); // Make the output visible

            // Make the save button visible
            saveButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "Enter values", Toast.LENGTH_SHORT).show();
            // If any of the input fields are empty, show a message or handle it as needed
            outputTextView.setText("Please enter values in both fields.");
            outputTextView.setVisibility(View.VISIBLE); // Make the output visible
            saveButton.setVisibility(View.GONE); // Hide the save button
        }
    }
}