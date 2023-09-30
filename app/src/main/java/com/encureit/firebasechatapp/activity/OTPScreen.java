package com.encureit.firebasechatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.encureit.firebasechatapp.R;
import com.encureit.firebasechatapp.databinding.ActivityOtpscreenBinding;
import com.encureit.firebasechatapp.utils.AndroidUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OTPScreen extends AppCompatActivity {

    private ActivityOtpscreenBinding binding;
    private String phoneNumber;
    Long timeOutSecond = 60L;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phoneNumber = getIntent().getStringExtra("phone");

        sendOTP(phoneNumber,false);

        binding.btnNext.setOnClickListener(v -> {
            String enteredOTP = binding.otp.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOTP);
            signIn(credential);
            setInProgress(true);
        });

        binding.btnResendOTP.setOnClickListener(v -> {
            sendOTP(phoneNumber,true);
        });


    }

    private void sendOTP(String phoneNumber, boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeOutSecond, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtils.showToast(getApplicationContext(),"OTP Verification failed");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode =s;
                                mForceResendingToken = forceResendingToken;
                                AndroidUtils.showToast(getApplicationContext(),"OTP send successfully");
                                setInProgress(false);
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(mForceResendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }



    private void setInProgress(boolean inProgress){
        if (inProgress){
            binding.progrssBar.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
        }else {
            binding.progrssBar.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Intent intent = new Intent(OTPScreen.this,UserScreen.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                    binding.otp.setText("");
                }else {
                    AndroidUtils.showToast(getApplicationContext(),"OTP verification failed");
                }
            }
        });
    }
    private void startResendTimer() {
        binding.btnResendOTP.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOutSecond--;
                binding.btnResendOTP.setText("Resend OTP in" +timeOutSecond+"second");
                if (timeOutSecond<=0){
                    timeOutSecond = 60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        binding.btnResendOTP.setEnabled(true);
                    });

                }
            }
        },0,1000);
    }

}