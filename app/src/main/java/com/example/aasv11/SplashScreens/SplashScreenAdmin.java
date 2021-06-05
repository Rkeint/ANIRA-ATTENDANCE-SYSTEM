package com.example.aasv11.SplashScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aasv11.Admin.Admin;
import com.example.aasv11.Login.Login;
import com.example.aasv11.R;

public class SplashScreenAdmin extends AppCompatActivity {

Animation topAnim, bottomAnim;
ImageView logo;
TextView Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_splash_screen);

       */


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.imgLogo);
        Name = findViewById(R.id.LogoName);

        logo.setAnimation(topAnim);
        Name.setAnimation(bottomAnim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenAdmin.this, Login.class));
                finish();
            }
        },2500 );


    }
}