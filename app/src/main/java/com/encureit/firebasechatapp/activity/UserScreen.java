package com.encureit.firebasechatapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.encureit.firebasechatapp.MainActivity;
import com.encureit.firebasechatapp.R;
import com.encureit.firebasechatapp.databinding.ActivityUserScreenBinding;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.AndroidUtils;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

public class UserScreen extends AppCompatActivity {
    private ActivityUserScreenBinding binding;
    private UserModel userModel;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phoneNumber = getIntent().getStringExtra("phone");
        getUserName();
        binding.btnLetMeIn.setOnClickListener(v -> {
            setUserName();
        });
    }

    private void setUserName() {

        String userName = binding.enterUsername.getText().toString();
        if (userName.isEmpty() || userName.length()<3){
            binding.enterUsername.setError("Username length should be 3 char");
            return;
        }
        setInProgress(true);
        if (userModel != null){
            userModel.setUserName(userName);
        }else {
            userModel = new UserModel(phoneNumber,userName, Timestamp.now(),FirebaseUtils.currentUserId());
        }
        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(UserScreen.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void getUserName() {
        setInProgress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    userModel = task.getResult().toObject(UserModel.class);
                    if (userModel != null){
                        binding.enterUsername.setText(userModel.getUserName());
                    }

                }else {
                    AndroidUtils.showToast(getApplicationContext(),"");
                }
            }
        });
    }


    private void setInProgress(boolean inProgress){
        if (inProgress){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLetMeIn.setVisibility(View.GONE);
        }else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLetMeIn.setVisibility(View.VISIBLE);
        }
    }

}