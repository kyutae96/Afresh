package com.example.A_FRESH;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Running extends AppCompatActivity {

    ImageView running_image;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_running);

        running_image = findViewById(R.id.running_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            running_image.setBackground(getDrawable(R.drawable.running));
        }

        Animation fadeout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout);
        running_image.startAnimation(fadeout);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), AfterRunning.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
