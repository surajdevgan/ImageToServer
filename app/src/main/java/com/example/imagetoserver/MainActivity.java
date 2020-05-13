package com.example.imagetoserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnChoose, btnUpload;
    EditText Title;

    ImageView imageView;
    String urlUpload = "http://192.168.0.104/api_demo/upload.php";
    static final int CAMERA_REQUEST = 1;
    Bitmap bitmap, bitmap1;
    RequestQueue requestQueue;
    String encodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imageView);
        Title = findViewById(R.id.stitle);
        bitmap1 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        imageView.setImageBitmap(bitmap1);
        imageToString(bitmap1);
        requestQueue = Volley.newRequestQueue(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }



        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);



            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String HTitle = Title.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error: "+error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("imgtits",HTitle);

                            params.put("image",encodeImage);
                            Log.w("ddf",encodeImage);


                     //   Log.w("ddf",encodeImage);

                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
            imageToString(bitmap);
        }

    }

    private void imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        encodeImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }

    void imagenew()
    {
        bitmap1 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        //imageView.setImageBitmap(bitmap1);
        imageToString(bitmap1);
    }
}
