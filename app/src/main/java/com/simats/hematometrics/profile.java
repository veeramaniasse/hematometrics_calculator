package com.simats.hematometrics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {
    private TextView euser,ecntct,edsg,egndr;
    private ImageView im;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        im = findViewById(R.id.imageView);
        euser= findViewById(R.id.userid);
        ecntct= findViewById(R.id.contact);
        egndr=findViewById(R.id.gndr);
        edsg=findViewById(R.id.dsg);
        fetchData(ip.user);
        fetchData1(ip.user);
        Button edt = findViewById(R.id.button);
        edt.setOnClickListener(view -> {
            Intent it = new Intent(profile.this,com.simats.hematometrics.edt.class);
            startActivity(it);

        });

    }
    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"viewprofile.php";

        // Append the username as a parameter to the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
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
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                System.out.println(username);
                data.put("name", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.optString("status");

            if ("success".equals(status)) {
                JSONObject profileData = jsonResponse.optJSONObject("profileData");
                if (profileData != null) {
                    String doctorName = profileData.optString("doctorName");
                    String doctorMobile = profileData.optString("doctorMobile");
                    String doctorGender = profileData.optString("doctorGender");
                    String doctorDesignation = profileData.optString("doctorDesignation");
                    ip.cntct=doctorMobile;
                    ip.dsg=doctorDesignation;
                    ip.gndr=doctorGender;
                    euser.setText("Username           :     "+doctorName);
                    egndr.setText("Gender                :      "+doctorGender);
                    ecntct.setText("Mobile number  :     "+doctorMobile);
                    edsg.setText("Designation          :     "+doctorDesignation);
                } else {
                    // Handle missing profileData object
                    Log.e("Error", "profileData is null");
                }
            } else {
                String message = jsonResponse.optString("message");
                // Handle error message if needed
                Log.e("Error", "Failed: " + message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }
    }


    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }
    private void fetchData1(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"show_photo.php";

        // Append the username as a parameter to the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse1(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                System.out.println(username);
                data.put("name", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void handleResponse1(String response) {
        Log.d("JSON Response", response);

        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.optString("status");

            if ("success".equals(status)) {
                // If status is success, retrieve the data object
                JSONObject dataObject = jsonResponse.optJSONObject("data");
                if (dataObject != null) {
                    // Extract the profile photo URL from the data object
                    String base64Image = dataObject.optString("profile_photo_base64");
                    ip.dp=base64Image;

                    if (base64Image != null && !base64Image.isEmpty()) {
                        byte[] decodedImageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
                        Bitmap circularBitmap = Utils.getCircleBitmap(decodedBitmap);
                        im.setImageBitmap(circularBitmap);
                    }
                } else {
                    // Handle missing or null data object
                    Log.e("handleResponse1", "No data object found in the response");
                }
            } else {
                // Handle if status is not success
                Log.e("handleResponse1", "Status is not success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }
    }




}