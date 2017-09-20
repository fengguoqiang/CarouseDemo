package com.example.carouselpicandvideodemo;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carouselpicandvideodemo.activity.CarouselActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_click = (Button) findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, CarouselActivity.class);
                intent.putExtra("path", Environment.getExternalStorageDirectory().getAbsoluteFile()+"/hello");
                startActivity(intent);
            }
        });
    }
}
