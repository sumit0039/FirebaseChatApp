package com.encureit.firebasechatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encureit.firebasechatapp.activity.ChatActivity;
import com.encureit.firebasechatapp.databinding.SearchUserRowBinding;
import com.encureit.firebasechatapp.model.UserModel;
import com.encureit.firebasechatapp.utils.AndroidUtils;
import com.encureit.firebasechatapp.utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchAdapter extends FirestoreRecyclerAdapter<UserModel, SearchAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;


    public SearchAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserModel model) {
        SearchUserRowBinding binding = holder.binding;
        binding.userNameTxt.setText(model.getUserName());
        binding.userMobileTxt.setText(model.getPhone());
        if (model.getUserId().equals(FirebaseUtils.currentUserId())){
            binding.userNameTxt.setText(model.getUserName()+" (Me)");
        }
        binding.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtils.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchUserRowBinding binding;
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = SearchUserRowBinding.inflate(layoutInflater);
        return new SearchAdapter.ViewHolder(binding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        SearchUserRowBinding binding;
        public ViewHolder(@NonNull SearchUserRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
