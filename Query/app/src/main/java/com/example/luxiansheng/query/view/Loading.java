package com.example.luxiansheng.query.view;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.luxiansheng.query.R;

public class Loading extends Activity {
    public static Activity instance;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        instance=this;
        View view=findViewById(R.id.myBg);
        view.setAlpha(0.7f);
        ImageView imageView=(ImageView)findViewById(R.id.anim);
        AnimationDrawable anim= (AnimationDrawable) imageView.getBackground();
        anim.start();
    }
}
