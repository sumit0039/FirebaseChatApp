package com.encureit.firebasechatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encureit.firebasechatapp.activity.ChatActivity;
import com.encureit.firebasechatapp.databinding.RecentChatRowBinding;
import com.encureit.firebasechatapp.databinding.SearchUserRowBinding;
import com.encureit.firebasechatapp.model.ChatRoomModel;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.AndroidUtils;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;


    public RecentChatAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatRoomModel model) {
        RecentChatRowBinding binding = holder.binding;
        FirebaseUtils.getOtherUserFromChatroom(model.getUserId())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                        binding.userNameTxt.setText(otherUserModel.getUserName());
                        binding.lastMessageTxt.setText(model.getLastMessage());
                        binding.lastMessageTimeTxt.setText(model.getLastMessageTimeStamp().toString());
                    }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecentChatRowBinding binding;
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = RecentChatRowBinding.inflate(layoutInflater);
        return new RecentChatAdapter.ViewHolder(binding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        RecentChatRowBinding binding;
        public ViewHolder(@NonNull RecentChatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
