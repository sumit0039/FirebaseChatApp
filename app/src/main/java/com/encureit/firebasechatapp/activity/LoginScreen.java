package com.encureit.firebasechatapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.encureit.firebasechatapp.R;
import com.encureit.firebasechatapp.databinding.ActivityLoginScreenBinding;

public class LoginScreen extends AppCompatActivity {
    private ActivityLoginScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setVisibility(View.GONE);

        binding.countryCode.registerCarrierNumberEditText(binding.mobileNo);
        binding.btnSendOtp.setOnClickListener(v -> {
            if (!binding.countryCode.isValidFullNumber()){
                binding.mobileNo.setError("Phone number not valid");
                return;
            }
            Intent intent = new Intent(this,OTPScreen.class);
            intent.putExtra("phone",binding.countryCode.getFullNumberWithPlus());
            startActivity(intent);
            binding.mobileNo.setText("");
        });
    }
}