package com.example.testmind;

import com.example.testmind.draw.DataRectangle;
import com.example.testmind.draw.DrawingSurface;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //FrameLayout frame = (FrameLayout)findViewById(R.id.main_view);
        //frame.addView(new DataRectangle(this, 30, 80));
        setContentView(new DrawingSurface(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
}
