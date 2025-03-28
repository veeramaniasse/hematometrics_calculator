package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Previoushistory extends AppCompatActivity {

    private Button selectDateButton;
    private Calendar selectedDateCalendar;
    private Spinner answerSpinner,answerSpinner1;
    private String[] opt = {};
    private String cat,sub,date;
    private ListView listView;
    private TextView resultTextView,resultTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previoushistory);
        selectDateButton = findViewById(R.id.select_date_button);
        selectDateButton.setText("Select Date"); // Set initial button text
        listView= findViewById(R.id.Listview);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        String[] options = {"select category","RBC Indices", "Recticulocyte", "WBC Count", "corrected WBC", "Platelet Count"};


// Spinner 1 setup
        answerSpinner = findViewById(R.id.answergender);
        resultTextView = findViewById(R.id.resultTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (answerSpinner != null) {
            answerSpinner.setAdapter(adapter);
        }

        if (answerSpinner != null) {
            answerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedAnswer = answerSpinner.getSelectedItem().toString();

                    if (selectedAnswer.equals("RBC Indices")) {
                        cat = "1";
                        opt = new String[]{"select subcategory","MCV", "MCH", "MCHC"};
                    } else if (selectedAnswer.equals("Recticulocyte")) {
                        cat = "2";
                        opt = new String[]{"select subcategory","Recticulocyte count", "Absolute Recticulocyte", "Corrected Recticulocyte count", "Recticulocyte production"};
                    } else if (selectedAnswer.equals("WBC Count")) {
                        cat = "3";
                        sub = "0";
                        opt = new String[]{};
                    } else if (selectedAnswer.equals("corrected WBC")) {
                        cat = "4";
                        sub = "0";
                        opt = new String[]{};
                    } else if (selectedAnswer.equals("Platelet Count")) {
                        cat = "5";
                        sub = "0";
                        opt = new String[]{};
                    }

                    // Update the adapter for the second spinner based on the selected options
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Previoushistory.this, android.R.layout.simple_spinner_item, opt);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    if (answerSpinner1 != null) {
                        answerSpinner1.setAdapter(adapter1);
                    }

                    // Set the result text view to the selected answer
                    resultTextView.setText(selectedAnswer);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Handle when nothing is selected
                }
            });
        }

// Spinner 2 setup
        answerSpinner1 = findViewById(R.id.answergender1);
        resultTextView1 = findViewById(R.id.resultTextView1);

// Ensure that the Spinner is not null before setting up its listener
        if (answerSpinner1 != null) {
            answerSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedAnswer = answerSpinner1.getSelectedItem().toString();

                    // Update sub value based on the selected item in the second spinner
                    if (selectedAnswer.equals("MCV")) {
                        sub = "1.1";
                    } else if (selectedAnswer.equals("MCH")) {
                        sub = "1.2";
                    } else if (selectedAnswer.equals("MCHC")) {
                        sub = "1.3";
                    } else if (selectedAnswer.equals("Recticulocyte count")) {
                        sub = "2.1";
                    } else if (selectedAnswer.equals("Absolute Recticulocyte")) {
                        sub = "2.2";
                    } else if (selectedAnswer.equals("Corrected Recticulocyte count")) {
                        sub = "2.3";
                    } else if (selectedAnswer.equals("Recticulocyte production")) {
                        sub = "2.4";
                    }

                    // Set the result text view to the selected answer
                    resultTextView1.setText(selectedAnswer);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Handle when nothing is selected
                }
            });
        }

// Set up a button click listener
        Button gn = findViewById(R.id.generate);
        gn.setOnClickListener(view -> {
            sendLoginRequest(cat,sub,date);
            // Here you can access the variables like date, cat, sub, etc.
            System.out.println(date + " " + cat + " " + sub);
        });

    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // Store selected date
                        selectedDateCalendar = Calendar.getInstance();
                        selectedDateCalendar.set(year, month, dayOfMonth);

                        // Update button text to selected date
                        updateButtonText();

                        // Perform actions with selected date
                        String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateCalendar.getTime());
                        Toast.makeText(Previoushistory.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, dayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    // Method to update button text to selected date
    private void updateButtonText() {
        if (selectedDateCalendar != null) {
            String buttonText = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateCalendar.getTime());
            date=buttonText;
            selectDateButton.setText(buttonText);
        }
    }



    private void sendLoginRequest(final String cat, final String sub, final String date) {
        String URL = ip.ipn + "fetch.php";
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
                // Send the selected date, category, and subcategory as POST parameters
                Map<String, String> data = new HashMap<>();
                data.put("date", date);
                data.put("catid", cat);
                data.put("subid", sub);
                return data;
            }
        };

        // Customize the retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000, // Timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Max number of retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Backoff multiplier
        ));

        // Initialize the Volley request queue and add the request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    // Handle the JSON response
    // Handle the JSON response
    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.optString("status");

            // Check if the response status is "success"
            if ("success".equals(status)) {
                // Get the JSON array "data" from the response
                JSONArray dataArray = jsonResponse.optJSONArray("data");

                if (dataArray != null) {
                    // Create a list to store the result values
                    ArrayList<String> resultList = new ArrayList<>();

                    // Add the result values to the list
                    for (int i = 0; i < dataArray.length(); i++) {
                        String result = dataArray.optString(i);
                        resultList.add(result);
                    }

                    // Add the total count as the first item
                    resultList.add(0, "Count: " + dataArray.length());

                    // Display the list in a ListView or do further processing
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
                    listView.setAdapter(adapter);

                    // Optionally, you can add a click listener to the ListView items
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        // Handle item click if needed
                        String selectedItem = resultList.get(position);
                        Toast.makeText(this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Show a message if the "data" array is null
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show a message if the status is not "success"
                Toast.makeText(this, "Failed to get data", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Data Not Found ", Toast.LENGTH_SHORT).show();
//            error.toString().trim()
        }
    }
}