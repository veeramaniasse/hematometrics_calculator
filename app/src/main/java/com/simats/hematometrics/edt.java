package com.simats.hematometrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class edt extends AppCompatActivity {
    private EditText t1,t2,t3,t4;
    private static final int CAMERA_GALLERY_REQUEST_CODE = 1001;
    private Intent lastActivityResultData;
    private Uri selectedImageUri;
    private String s1,s2,s3,s4;
    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt);
        t1=findViewById(R.id.editTextText);
        t2=findViewById(R.id.editTextText2);
        t3=findViewById(R.id.editTextText3);
        t4=findViewById(R.id.editTextText4);
        im = findViewById(R.id.imageView);
        t1.setText(ip.user);
        t2.setText(ip.cntct);
        t4.setText(ip.dsg);
        t3.setText(ip.gndr);
        String base64Imag=ip.dp;


        if (base64Imag != null && !base64Imag.isEmpty()) {
            byte[] decodedImageBytes = Base64.decode(base64Imag, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
            Bitmap circularBitmap = Utils.getCircleBitmap(decodedBitmap);
            im.setImageBitmap(circularBitmap);
        }

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraOrGallery();
            }
        });

        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(view -> {
            s1=t1.getText().toString().trim();
            s2=t2.getText().toString().trim();
            s3=t3.getText().toString().trim();
            s4=t4.getText().toString().trim();
            sendLoginRequest(s1);
            if (selectedImageUri != null || lastActivityResultData != null) {
                try {
                    Bitmap bitmap;
                    if (selectedImageUri != null && selectedImageUri.toString().startsWith("content://")) {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImageUri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    } else {
                        // Camera image captured
                        Bitmap photo = (Bitmap) lastActivityResultData.getExtras().get("data");
                        bitmap = photo;
                    }

                    String base64Image = convertBitmapToBase64(bitmap);
                    // Execute AsyncTask to send data to the server
                    new SendDataToServer().execute(ip.user, selectedImageUri != null ? selectedImageUri.toString() : "", base64Image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(edt.this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }



        });
    }
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void openCameraOrGallery() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Create a chooser intent to allow the user to select between camera and gallery
        Intent chooser = Intent.createChooser(galleryIntent, "Select Image Source");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });

        startActivityForResult(chooser, CAMERA_GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Gallery image selected
                selectedImageUri = data.getData();

                im.setImageURI(selectedImageUri);
            } else if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                // Camera image captured
                lastActivityResultData = data; // Store the data
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                im.setImageBitmap(photo);
            }
        }
    }


    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String username = params[0];
                String imageUriString = params[1];
                String base64Image = params[2];
                System.out.println(base64Image);
                // Create a JSON object
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("username", username);
                jsonParams.put("base64image", base64Image);

                // Convert JSON object to string
                String jsonData = jsonParams.toString();

                URL url = new URL(ip.ipn+"/img_upld.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.setDoOutput(true);

                // Write JSON data to the server
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonData.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response from the server
                InputStream inputStream = urlConnection.getInputStream();
                int responseCode = urlConnection.getResponseCode();
                System.out.println(responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Data inserted successfully";
                } else {
                    return "Failed to insert data";
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result.equals("Data inserted successfully")) {
                Toast.makeText(edt.this, "success", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendLoginRequest(final String username) {
        String URL = ip.ipn+"editprofile.php";
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

                data.put("phno", s2);

                data.put("gender", s3);
                data.put("designation", s4);
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
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(edt.this, MainActivity2.class);

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