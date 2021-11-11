package com.example.A_FRESH;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Upload extends AppCompatActivity {
    private static final String TAG = "MAIN";
    private TextView class_text;
    private TextView name_text;
    private String img_class;
    private String img_name;
    private String eng_name;
    private Uri photoUri;
    private static final int CROP_FROM_IMAGE = 2;
    ImageView image;
    Button choose, upload, info;
    int PICK_IMAGE_REQUEST = 111;
    String URL = "http://52.78.70.39:5000/image";
//    String URL = "http://192.168.0.25:5000/image";
    Bitmap bitmap, bitmapImg;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        image = (ImageView) findViewById(R.id.image);
        choose = (Button) findViewById(R.id.choose);
        upload = (Button) findViewById(R.id.upload);
        info = (Button) findViewById(R.id.info_upload_error);
        class_text = findViewById(R.id.class_text);
        name_text = findViewById(R.id.name_text);


        if (getIntent() != null) {
            Intent imgIntent = getIntent();
            Uri intentImg = imgIntent.getParcelableExtra("output");
//    Log.d("이미지테스트", intentImg.toString());
            image.setImageURI(intentImg);


//        //intent로 받은 이미지 판별하기 버튼에 적용안됨

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


            if (choose.callOnClick()) {
                upload.setVisibility(View.VISIBLE);
            }

        }
    }

//
//    public void choose(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
//    }


//    public class BitmapConverter {
//        public Bitmap StringToBitmap(String encodedString) {
//            try {
//                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                return bitmap;
//            } catch (Exception e) {
//                e.getMessage();
//                return null;
//            }
//        }
//    }

//    private Uri getImageUri(Context context, Bitmap inImage){
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ExifInterface exif = null;
                try {
                    InputStream in = getContentResolver().openInputStream(filePath);
                    exif = new ExifInterface(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
//                갤러리에서 이미지 가져옴
                Bitmap bmRotated = rotateBitmap(bitmap, orientation);
//                이미지뷰에 이미지 세팅
                Glide.with(getApplicationContext()).load(bmRotated).into(image);
//                image.setImageBitmap(bitmap);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (image.getDrawable() == null) {
                            Toast.makeText(getApplicationContext(), "앨범에서 사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog = new ProgressDialog(Upload.this);
                            progressDialog.setMessage("결과가 나오고있습니다. 잠시만 기다려주세요.");
                            progressDialog.show();

                            //converting image to base64 string
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
//                            image.setImageBitmap(bm);

                                        Glide.with(getApplicationContext()).load(bm).into(image);
                                        Log.d("승환님줄거", jsonObj.getString("result_image"));

                                        class_text.setText(img_class);
//                            name_text.setText(img_name);

                                        switch (img_name) {
                                            case "ansim":
                                                name_text.setText("안심");
                                                break;
                                            case "chaeggeut":
                                                name_text.setText("채끝");
                                                break;
                                            case "deungsim":
                                                name_text.setText("등심");
                                                break;
                                            case "samgak":
                                                name_text.setText("삼각살");
                                                break;
                                            case "boochae":
                                                name_text.setText("부채살");
                                                break;
                                            case "chima":
                                                name_text.setText("치마살");
                                                break;
                                            case "galbi":
                                                name_text.setText("갈비");
                                                break;
                                            case "ubgin":
                                                name_text.setText("업진살");
                                                break;

                                        }


                                        if (img_name == "null") {
                                            name_text.setText("-");
                                            class_text.setText("-");
//                                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                                            info.setVisibility(View.VISIBLE);
                                            info.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), CameraGuide2.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            FancyToast.makeText(getApplicationContext(), "실패", FancyToast.LENGTH_SHORT, FancyToast.INFO, R.drawable.cutecow, false).show();
                                        } else {
                                            FancyToast.makeText(getApplicationContext(), "성공", FancyToast.LENGTH_SHORT, FancyToast.INFO, R.drawable.cutecow, false).show();

                                        }

                                        progressDialog.dismiss();


//                            Log.d(TAG, "안녕" + img_class + img_name + img_result);
                                    } catch (JSONException e) {
                                        Toast.makeText(Upload.this, "테스트테스트", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "안녕에러" + e);
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "test");
//                        textView.setText(response);
//                        Toast.makeText(Upload.this, "성공", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(Upload.this, "서버 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), Upload.class);
                                    startActivity(intent);
                                }
                            }) {
                                //                    adding parameters to send
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
                    }

                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "앨범에서 사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
            upload.setVisibility(View.INVISIBLE);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                    upload.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        finish();
    }


}