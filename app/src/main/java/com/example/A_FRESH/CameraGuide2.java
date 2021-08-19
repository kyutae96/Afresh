package com.example.A_FRESH;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.A_FRESH.Fragment.FragmentMainActivity;

public class CameraGuide2 extends AppCompatActivity {

    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_guide);

        button = (Button) findViewById(R.id.tutorialid);
    }


    public void checklist(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checklist:
                if (checked){
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.INVISIBLE);
                }break;
        }
    }

    public void btn_tutorialcl(View view){
        Intent intent = new Intent(getApplicationContext(), FragmentMainActivity.class);
        startActivity(intent);
        finish();
    }



    public void back_click(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        finish();
    }
}

