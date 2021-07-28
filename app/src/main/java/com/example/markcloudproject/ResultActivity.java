package com.example.markcloudproject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ResultActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Button imageButton = (Button) findViewById(R.id.server_button);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServerActivity.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("strParamName");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        img.setImageBitmap(adjustedBitmap);


    }

}
