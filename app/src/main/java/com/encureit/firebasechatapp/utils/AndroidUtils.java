package com.encureit.firebasechatapp.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.encureit.firebasechatapp.model.UserModel;

public class AndroidUtils {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel userModel){
        intent.putExtra("username",userModel.getUserName());
        intent.putExtra("phone",userModel.getPhone());
        intent.putExtra("userId",userModel.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUserName(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }
}
