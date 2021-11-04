package com.zeit.hotrocketdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zeit.hotrocketdemo.home.HomeViewCache;

public class MainActivity extends AppCompatActivity {

    String TAG = "zz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View homeView = HomeViewCache.getView(this,"home_layout_key", LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        if (homeView!=null){
            setContentView(homeView, new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        }else{
            setContentView(R.layout.activity_main);
        }
        HomeViewCache.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onResume:");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            Log.i(TAG, "onWindowFocusChanged: 延时操作放在这才有效");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickTest(View view) {
        Toast.makeText(this, "test!", Toast.LENGTH_SHORT).show();
    }
}