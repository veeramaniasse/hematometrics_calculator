package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ReticulocyteProductionIndex extends AppCompatActivity {
    private EditText reticulocyteCountEditText;
    private EditText maturationTimeEditText;
    private String x="";
    private TextView outputTextView;
    private Button calculateButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reticulocyte_production_index);
        reticulocyteCountEditText = findViewById(R.id.reticulocytecount);
        maturationTimeEditText = findViewById(R.id.maturationtime);
        outputTextView = findViewById(R.id.output);
        calculateButton = findViewById(R.id.calculate);
        saveButton = findViewById(R.id.save);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateReticulocyteProductionIndex();
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
                data.put("subid", "2.4");
                data.put("catid", "2");
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
                Intent intent = new Intent(ReticulocyteProductionIndex.this, MainActivity2.class);

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
    private void calculateReticulocyteProductionIndex() {
        String reticulocyteCountStr = reticulocyteCountEditText.getText().toString();
        String maturationTimeStr = maturationTimeEditText.getText().toString();

        if (!TextUtils.isEmpty(reticulocyteCountStr) && !TextUtils.isEmpty(maturationTimeStr)) {
            double correctedreticulocyteCount = Double.parseDouble(reticulocyteCountStr);
            double maturationTimeindays = Double.parseDouble(maturationTimeStr);
            // Calculate reticulocyteproductionindex using the formula
            double reticulocyteProductionIndex = correctedreticulocyteCount / maturationTimeindays;
            x=String.format("%.2f", reticulocyteProductionIndex);
            outputTextView.setText(String.format("%.2f", reticulocyteProductionIndex));
            outputTextView.setVisibility(View.VISIBLE); // Show the output TextView
            saveButton.setVisibility(View.VISIBLE); // Show the save button
        } else {
            Toast.makeText(getApplicationContext(), "Enter values", Toast.LENGTH_SHORT).show();
            outputTextView.setText("Please enter values for Corrected Reticulocyte Count and Maturation Time");
            // Hide the output TextView and the save button
            outputTextView.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
    }

}