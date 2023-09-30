package com.encureit.firebasechatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encureit.firebasechatapp.databinding.ChatMessageRowBinding;
import com.encureit.firebasechatapp.databinding.SearchUserRowBinding;
import com.encureit.firebasechatapp.model.ChatMessageModel;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position, @NonNull ChatMessageModel model) {
        ChatMessageRowBinding binding = holder.binding;
        if (model.getSenderId().equals(FirebaseUtils.currentUserId())){
            binding.leftChatLayout.setVisibility(View.GONE);
            binding.rightChatLayout.setVisibility(View.VISIBLE);
            binding.rightChatText.setText(model.getMessage());
        }else {
            binding.rightChatLayout.setVisibility(View.GONE);
            binding.leftChatLayout.setVisibility(View.VISIBLE);
            binding.leftChatText.setText(model.getMessage());
        }

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatMessageRowBinding binding;
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = ChatMessageRowBinding.inflate(layoutInflater);
        return new ChatAdapter.ViewHolder(binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChatMessageRowBinding binding;
        public ViewHolder(@NonNull ChatMessageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
