package com.example.A_FRESH;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Upload extends AppCompatActivity {
    private static final String TAG = "MAIN";
    private TextView class_text;
    private TextView name_text;
    private String img_class;
    private String img_name;
    private String img_result;
    ImageView image;
    Button choose, upload;
    int PICK_IMAGE_REQUEST = 111;
    String URL = "http://192.168.0.13:5000/image";
    Bitmap bitmap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        image = (ImageView) findViewById(R.id.image);
        choose = (Button) findViewById(R.id.choose);
        upload = (Button) findViewById(R.id.upload);
        class_text = findViewById(R.id.class_text);
        name_text = findViewById(R.id.name_text);


        Bitmap bm = BitmapFactory.decodeStream(null);

        //opening image chooser option
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Upload.this);
                progressDialog.setMessage("결과가 나오고있습니다. 잠시만 기다려주세요.");
                progressDialog.show();

                //converting image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("image", imageString);
                JSONObject jsonObj = new JSONObject(params);

                //sending image to server
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "녕안");
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            img_class = jsonObj.getString("beef_grade");
                            img_name = jsonObj.getString("beef_part");
                            byte[] byteArray = Base64.decode(jsonObj.getString("result_image"), Base64.DEFAULT);
                            System.out.println("byte[]:" + byteArray);
                            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                            image.setImageBitmap(bm);
                            class_text.setText(img_class);
                            name_text.setText(img_name);

                            if (img_name == "null"){
                                name_text.setText("-");
                                class_text.setText("-");
//                                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();

                                FancyToast.makeText(getApplicationContext(), "실패", FancyToast.LENGTH_SHORT, FancyToast.INFO, R.drawable.cutecow, false).show();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "성공", FancyToast.LENGTH_SHORT, FancyToast.INFO, R.drawable.cutecow, false).show();

                            }


                            Log.d(TAG, "안녕" + img_class + img_name + img_result);
                        } catch (JSONException e) {
                            Log.d(TAG, "안녕에러" + e);
                            e.printStackTrace();
                        }
                        Log.d(TAG, "test");
                        //textView.setText(response);
//                        Toast.makeText(Upload.this, "성공", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Upload.this, "서버 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Upload.class);
                        startActivity(intent);
                    }
                }) {
                    //adding parameters to send
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("image", imageString);
                        return parameters;
                    }
                };
                request.setTag(TAG);
                RequestQueue rQueue = Volley.newRequestQueue(Upload.this);
                rQueue.add(request);

            }

        });

        if (choose.callOnClick()) {
            upload.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

}