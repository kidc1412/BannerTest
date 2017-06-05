package com.kidc.bannertest;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> data;
    Banner banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new ArrayList<>();
        banner = (Banner) findViewById(R.id.banner);

        data.add(R.drawable.x1);
        data.add(R.drawable.x2);
        data.add(R.drawable.x3);
        data.add(R.drawable.x4);
        data.add(R.drawable.x5);

        banner.setImageData(data);

    }
}
