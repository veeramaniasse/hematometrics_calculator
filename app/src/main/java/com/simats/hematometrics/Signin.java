package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {

    private Spinner answerSpinner;
    private TextView resultTextView;
    private EditText ename, ecntct, edsg, epass, ecnf;
    private String gender, name, contact, desg, password, cnf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        answerSpinner = findViewById(R.id.answergender);
        ename = findViewById(R.id.name);
        ecntct = findViewById(R.id.mobile_number);
        edsg = findViewById(R.id.designation);
        epass = findViewById(R.id.password);
        ecnf = findViewById(R.id.confirm_password);

        // Define the options for the spinner
        String[] options = {"Select gender ","Male", "Female", "Others"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Ensure that the Spinner is not null before calling setAdapter
        if (answerSpinner != null) {
            answerSpinner.setAdapter(adapter);
        }

        // Set a listener for item selections
        if (answerSpinner != null) {
            answerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedAnswer = answerSpinner.getSelectedItem().toString();
                    gender = selectedAnswer;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Do nothing here
                }
            });
        }

        Button signUpButton = findViewById(R.id.signup);
        Button signinButton = findViewById(R.id.signin);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ename.getText().toString().trim();
                contact = ecntct.getText().toString().trim();
                desg = edsg.getText().toString().trim();
                password = epass.getText().toString().trim();
                cnf = ecnf.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
                } else if (cnf.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Confirm password is required", Toast.LENGTH_SHORT).show();
                } else if (desg.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Designation is required", Toast.LENGTH_SHORT).show();
                } else if (contact.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Contact is required", Toast.LENGTH_SHORT).show();
                } else if (gender.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Gender is required", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendLoginRequest(name, password);
                }
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When "Sign In" button is clicked, navigate to the Login activity
                Intent intent = new Intent(Signin.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void sendLoginRequest(final String username, final String password) {
        String URL = ip.ipn + "signup.php";

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
                // Send the username and password as POST parameters
                Map<String, String> data = new HashMap<>();
                data.put("name", username);
                data.put("password", password);
                data.put("mobilenumber", contact);
                data.put("confirm_password", cnf);
                data.put("gender", gender);
                data.put("designation", desg);
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
            if (response.toLowerCase().contains("success")) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Signin.this, login.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle network request errors
    private void handleError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(this, "Request timed out. Check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(error.toString().trim());
            Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show();
        }
    }
}
