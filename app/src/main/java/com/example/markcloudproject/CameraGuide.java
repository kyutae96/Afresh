package com.example.markcloudproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.markcloudproject.Fragment.Fragment1;
import com.example.markcloudproject.Fragment.FragmentMainActivity;

public class CameraGuide extends AppCompatActivity {

    Button button;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_guide);

        button = (Button) findViewById(R.id.btn_tutorialid);
        imageView = (ImageView) findViewById(R.id.tutorialid);


    }


    public void checklist(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checklist:
                if (checked){
                    button.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }break;
        }
    }

    public void btn_tutorialcl(View view){
        Intent intent = new Intent(getApplicationContext(), FragmentMainActivity.class);
        startActivity(intent);
        finish();
    }


    public void back_click(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

