package com.example.markcloudproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ServerActivity extends AppCompatActivity {

    EditText etName,etMsg;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        etName=findViewById(R.id.et_name);
        etMsg=findViewById(R.id.et_msg);
        iv=findViewById(R.id.iv);
    }

    public void clickBtn(View view) {

        //갤러리 or 사진 앱 실행하여 사진을 선택하도록..
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    //선택한 사진의 경로(Uri)객체 얻어오기
                    Uri uri= data.getData();
                    if(uri!=null){
                        iv.setImageURI(uri);
                    }

                }else
                {
                    Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //String getRealPathFromUri(Uri uri){
      //  String[] proj = {MediaStore.Images.Media.DATA};
        //CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        //Cursor cursor = loader.loadInBackground();
        //int colimn_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //cursor.moveToFirst();
        //String result = cursor.getString(colimn_index);
        //cursor.close();
        //return  result;
   // }
//2021-07-80 [https://stickode.com/detail.html?no=1631] 이어서 하기


    public void clickUpload(View view) {
    }

    public void clickLoad(View view) {
    }
}
