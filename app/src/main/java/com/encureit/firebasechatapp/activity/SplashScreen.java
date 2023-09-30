package com.encureit.firebasechatapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.encureit.firebasechatapp.MainActivity;
import com.encureit.firebasechatapp.R;
import com.encureit.firebasechatapp.utils.FirebaseUtils;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseUtils.isLoggedIn()){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }else {
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                }
                finish();

            }
        },3000);
    }
}